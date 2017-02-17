package com.loginair.gshopping.manager;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by PineChang on 2017/2/17.
 */

public class ThreadManager {
    //以下将线程池做成单粒
    private static  ThreadPool mThreadPool;

    public static  ThreadPool getmThreadPool(){
        if(mThreadPool==null){
            synchronized (ThreadManager.class){
                if(mThreadPool == null){
                    int cpuCount = Runtime.getRuntime().availableProcessors();//获取系统自带的cpu数量
                    Log.i("cpu","cpu的个数为"+cpuCount);
                    int threadCount  = 2*cpuCount+1;
                    mThreadPool = new ThreadPool(threadCount,threadCount,1L);

                }
            }
        }

        return mThreadPool;
    }
    // 以下为线程池类
    public static class ThreadPool{

        private int corePoolSize;//核心线程数

        private int maximumPoolSize//最大线程数

        private long keepAliveTime;//保持活跃的时间

        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }
        //用线程执行者执行一个Runnable对象
        public void execute(Runnable r){
            if(executor==null){
                executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime, TimeUnit.SECONDS,new LinkedBlockingDeque<runnable>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
            }
            executor.execute(r);
        }
        //用线程执行者取消一个执行对象

        public void cancel(Runnable r){
            if(executor != null){
                executor.getQueue().remove(r);
            }

        }


    }


}
