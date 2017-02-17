package com.loginair.gshopping.manager;

import android.app.DownloadManager;

import com.loginair.gshopping.domain.DownloadInfo;

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
    private static  DownLoadManager = new DownLoadManager();



    //观察者集合
    private ArrayList<DownLoadObserver> mObservers= new ArrayList<DownLoadObserver>();
    //要下载对象的集合
    private ConcurrentHashMap<String,DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String,DownloadInfo>();
    //要下载的任务集合
    private ConcurrentHashMap<String,DownLoadTask> mDownloadTaskMap = new ConcurrentHashMap<String,DownLoadTask>();

    public interface DownLoadObserver{
        //下载进度和下载状态发生改变都会回调以下方法
        public void onDownLoadStateChanged(DownloadInfo info);
        public void onDownLoadPrgressChanged(DownloadInfo info);
    }

    class DownloadTask implements Runnable{
        private DownloadInfo downloadInfo;
        public DownloadTask(DownloadInfo downloadInfo){
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            //开始下载
            downloadInfo.currentState = STATE_DOWNING;
            notifyDownloadState
        }
    }
}
