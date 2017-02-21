package com.loginair.gshopping.manager;

import android.app.DownloadManager;

import com.loginair.gshopping.domain.AppInfo;
import com.loginair.gshopping.domain.DownloadInfo;

import java.io.File;
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
    private ConcurrentHashMap<String,DownLoadTask> mDownloadTaskMap = new ConcurrentHashMap<String,DownLoadTask>();

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


        //接下来将DownLoadInfo加入到DowmLaodInfo集合中国
        mDownloadInfoMap.put(downloadInfo.id,downloadInfo);

        //接下载就开始下载了



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


        }
    }
}
