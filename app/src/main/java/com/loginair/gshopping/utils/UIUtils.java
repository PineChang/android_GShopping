package com.loginair.gshopping.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import android.view.View;

import com.loginair.gshopping.global.GooglePlayApplication;

/**
 * Created by PineChang on 2017/2/15.
 */

public class UIUtils {
    //拿到Context
    public static Context getContext(){ return GooglePlayApplication.getContext();}
    public static Handler getHandler(){return GooglePlayApplication.getHandler();}
    public static int getMainThreadId(){return  GooglePlayApplication.getMainThreadId();}


    //获取资源字符串

    public static String getString(int id){ return getContext().getResources().getString(id);}


    //获取字符串数组
    public static String[] getStringArray(int id){return getContext().getResources().getStringArray(id);}

    //获取资源图片

    public static Drawable getDrawable(int id){return getContext().getResources().getDrawable(id);}


    //获取资源颜色

    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    //获取颜色状态器资源

    public static ColorStateList getColorStateList(int id){
        return getContext().getResources().getColorStateList(id);
    }
    //获取尺寸
    public static int  getDimen(int id){

        return getContext().getResources().getDimensionPixelSize(id);
    }

    //dip和px相互转化,
    public static int dip2px(float dip){
        //拿到一个dip相当于多少个px
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip*density+0.5f);

    }

    //px和dip的相互转化
    public static float px2dip(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return  px/density;

    }

    //加载布局文件
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }
    //判断是否运行在主线程
    public static boolean isRunOnUIThread(){
        int mTid = android.os.Process.myTid();
        if(mTid==getMainThreadId()){
            return true;
        }
        return false;
    }

    //让传进的Runnable无论在主线程还是子线程都运行在主线程
    public static void runOnUIThread(Runnable r){
        if(isRunOnUIThread()){
            r.run();
        }else{
            getHandler().post(r);
        }

    }
}
