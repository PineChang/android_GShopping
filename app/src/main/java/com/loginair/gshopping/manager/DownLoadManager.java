package com.loginair.gshopping.manager;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;

import com.loginair.gshopping.domain.AppInfo;
import com.loginair.gshopping.domain.DownloadInfo;
import com.loginair.gshopping.http.HttpHelper;
import com.loginair.gshopping.utils.IOUtils;
import com.loginair.gshopping.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by PineChang on 2017/2/17.
 */

public class DownLoadManager {
    public static final int STATE_UNDO = 1;//未下载
    public static final int STATE_WAITING = 2;//等待下载
    public static final int STATE_DOWNING = 3; //正在下载;

    public static final int STATE_PAUSE = 4;//暂停下载
    public  static final int STATE_ERROR = 5;//下载失败

    public  static final int STATE_SUCCESS = 6;//下载成功

    private DownLoadManager(){

    }

    //类对象持有一个实例对象
    private static  DownLoadManager mDM= null;

    //获取DownLoadManager
    public  static  DownLoadManager getInstance(){
         if(mDM==null){
             synchronized (DownLoadManager.class){
                 if(mDM==null){
                     mDM = new DownLoadManager();
                 }
             }
         }
    }

//----------------------------------------------------------------------------------------------

    //观察者集合
    private ArrayList<DownLoadObserver> mObservers= new ArrayList<DownLoadObserver>();
    //制订想成为观察者的标准
    public interface DownLoadObserver{
        //下载进度和下载状态发生改变都会回调以下方法
        public void onDownLoadStateChanged(DownloadInfo info);
        public void onDownLoadPrgressChanged(DownloadInfo info);
    }

    //注册观察者
    public  synchronized  void registerObserver(DownLoadObserver observer){
        //如果传递进来的观察者不为null,并且观察者集合中没有此观察者,才将其添加进观察者集合
        if(observer!=null && !mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }
    //注销观察者
    public  synchronized  void  unregisterObserver(DownLoadObserver observer){
        //如果传递进来的观察者不为null,并且观察者集合中有此观察者,才将其删除
        if(observer !=null && mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    //每个DownLoadInfo状态改变,都会通知所有的观察者们下载状态发生变化了
  public synchronized void notifyDownLoadStateChanged(DownloadInfo info){
      for(DownLoadObserver observer :mObservers){
          observer.onDownLoadStateChanged(info);
      }

  }
   //每个DownLoadInfo状态改变,都会通知所有的观察者们下载的状态发生变化了

    public  synchronized void  notifyDowmLoadProgressChanged(DownloadInfo info){
        for (DownLoadObserver observer:mObservers){
            observer.onDownLoadPrgressChanged(info);
        }
    }

//---------------------------------------------------------------------------------------------------------------
    //要下载对象的集合 第一个参数为要下载AppInfo的id;第二个参数为要下载AppInfo的转化为DownloadInfo
    private ConcurrentHashMap<String,DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String,DownloadInfo>();
    //要下载的任务集合
    private ConcurrentHashMap<String,DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String,DownLoadTask>();

    //传递进去一个AppInfo,就开始这个AppInfo的下载任务

    public  synchronized void  download(AppInfo info){
        //如果对象是第一次下载,需要创建一个新的DownloadInfo对象,从头下载
        //如果之前下载过,要接着下载,实现断点续传

         //去mDownloadInfoMap中拿出找一找有没有这个Info的下载记录
        DownloadInfo  downloadInfo = mDownloadInfoMap.get(info.id);
        if(downloadInfo==null){
            downloadInfo = DownloadInfo.copy(info);
        }

        //将downloadInfo的状态切换为等待下载状态
        downloadInfo.currentState = DownLoadManager.STATE_WAITING;
        //通知所有的观察者开始下载了
        notifyDownLoadStateChanged(downloadInfo);


        //接下来将DownLoadInfo加入到DowmLaodInfo集合中
        mDownloadInfoMap.put(downloadInfo.id,downloadInfo);

        //接下载就开始下载了



    }
    //根据应用信息返回下载对象
    public DownloadInfo getDownloadInfo(AppInfo info){
        return mDownloadInfoMap.get(info.id);
    }

    //一个task就是一个Runnable
    class DownloadTask implements Runnable{
        private DownloadInfo downloadInfo;
        public DownloadTask(DownloadInfo downloadInfo){
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            //一run,代表这个Runnable就正在下载了
            downloadInfo.currentState = STATE_DOWNING;
            notifyDownLoadStateChanged(downloadInfo);

            File file = new File(downloadInfo.path);

            HttpResult  httpResult;
            //如果这个文件不存在,或者存在但是下载的位置和记录的位置不一致,或者记录的位置为0,那么就删除了重新下载
            if(!file.exists()||file.length()!=downloadInfo.currentPos || downloadInfo.currentPos==0){
                file.delete();
                //删除后,将文件的下载位置改为0
                downloadInfo.currentPos=0;
                httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadInfo.downloadUrl);
            }else{
                //此时就是断点续传
                httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadInfo.downloadUrl+"&range=file.length()");


            }
            //边下载边将下载的结果写入硬盘

            if(httpResult!=null && httpResult.getInputStream()!=null) {
                InputStream in = httpResult.getInputStream();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file, true);//要在原有的的文件基础上添加数据
                    //所以必须设置为true;
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = in.read(buffer)) != -1 && downloadInfo.currentState == STATE_DOWNING) {
                        out.write(buffer, 0, len);
                        out.flush();
                        //读的len就可以当做下载的进度值;
                        downloadInfo.currentPos += len;
                        notifyDowmLoadProgressChanged(downloadInfo);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(in);
                    IOUtils.close(out);

                }


                //当上面的下载过程结束时,那么有两种情况,第一文件真的下载完成,第二文件中途暂停,第三文件下载失败

                if (file.length() == downloadInfo.size) {
                    //文件下载成功
                    downloadInfo.currentState = STATE_SUCCESS;
                    notifyDownLoadStateChanged(downloadInfo);
                    //下载中途暂停
                } else if (downloadInfo.currentState == STATE_PAUSE) {
                    notifyDownLoadStateChanged(downloadInfo);

                } else {
                    //下载失败
                    file.delete();
                    downloadInfo.currentState = STATE_ERROR;
                    downloadInfo.currentPos = 0;
                    notifyDownLoadStateChanged(downloadInfo);

                }
            }else{
                //下载失败
                file.delete();
                downloadInfo.currentState = STATE_ERROR;
                downloadInfo.currentPos = 0;
                notifyDownLoadStateChanged(downloadInfo);

            }
            //从下载集合中移除下载任务;
            mDownloadTaskMap.remove(downloadInfo.id)

        }



        }

      //下载暂停的业务逻辑
    public  synchronized  void pause(AppInfo info){
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if(downloadInfo!=null){
            //只有正在下载和等待下载时才需要暂停业务
            if(downloadInfo.currentState==STATE_DOWNING || downloadInfo.currentState == STATE_WAITING){
                DownloadTask task = mDownloadTaskMap.get(downloadInfo.id);
                if(task!=null){
                    ThreadManager.getmThreadPool().cancel(task);
                }
                //将下载状态切换为暂停
                downloadInfo.currentState=STATE_PAUSE;
                notifyDownLoadStateChanged(downloadInfo);
            }
        }
    }

      //下载后的安装

    public  synchronized void install(AppInfo info){
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if(downloadInfo!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://"+downloadInfo.path),"application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }
    }

