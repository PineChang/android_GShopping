package com.loginair.gshopping.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by PineChang on 2017/2/14.
 */

public class GooglePlayApplication extends Application{

    private static Context context;

    private static Handler handler;

    private static int mainThreadId;

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //在对象里访问静态属性
        context  = getApplicationContext();
        //在主线程里创建Handler
        handler = new Handler();
        //获取主线程的Id
        mainThreadId = android.os.Process.myTid();


    }
}
