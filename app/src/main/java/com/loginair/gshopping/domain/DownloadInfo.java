package com.loginair.gshopping.domain;

import android.app.DownloadManager;
import android.os.Environment;

import com.loginair.gshopping.manager.DownLoadManager;

import java.io.File;

/**
 * Created by PineChang on 2017/2/17.
 */

public class DownloadInfo {
    public String id;
    public String name;
    public String downloadUrl;
    public long   size;
    public String packageName;

    public long currentPos;//当前下载的位置
    public int currentState;//当前下载的状态
    public String path;//下载到本地文件的位置

    public  static final String GOOGLE_MARKET = "GOOGLE_MARKET"//sdcard下的根目录名
    public  static final String DOWNLOAD = "download";//根目录下用来存放下载的apk的子文件名;

    //获取下载进度
    public  float  getProgress(){
        if(size==0){
            return 0 ;
        }
        float progerss = currentPos/(float)size;
        return progerss;

    }

    //文件夹不存在或者不是文件夹创建文件夹的方法
    private boolean createDir(String dir){
        File dirFile = new File(dir);
        if(!dirFile.exists() || !dirFile.isDirectory()){
            return dirFile.mkdir();
        }
        return true;
    }

    public String getFilePath(){
        StringBuffer sb  = new StringBuffer();
        //拿到sdcard的位置路径
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        sb.append(sdcard);
        sb.append(File.separator);
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DOWNLOAD);//.../download
        if(createDir(sb.toString())){
            return sb.toString()+File.separator+name+".apk";
        }
    }

    //把一个AppInfo转化为一个DownlaodInfo
    public  static  DownloadInfo copy(AppInfo info){
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id  = info.id;
        downloadInfo.name = info.name;
        downloadInfo.downloadUrl = info.downloadUrl;
        downloadInfo.packageName = info.packageName;
        downloadInfo.size = info.size;

        downloadInfo.currentPos = 0;
        downloadInfo.currentState = DownLoadManager.STATE_UNDO;
        downloadInfo.path = downloadInfo.getFilePath();

        return downloadInfo;


    }
}
