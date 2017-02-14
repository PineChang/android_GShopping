package com.loginair.gshopping.utils;

import android.util.Log;

/**
 * Created by PineChang on 2017/2/14.
 */

public class LogUtils {

    //定义日志输出的级别

    public static final int LEVEL_NONE = 0;//全部都不输出

    public static final int LEVEL_ERROR = 1;

    public static final int LEVEL_WARN = 2;

    public static  final int LEVEL_INFO = 3;

    public static final int LEVEL_DEBUG = 4;

    public static final int LEVEL_VERBOSE = 5;


    //给输出的内容添加一个标志;
    private static String mTag = "LogUtils";

    //设置输出的等级,只有输出的等级大于当前输出,也就是以更宽松的要求来
    //来约束当前输出,才能输出当前正要打印的输出;
    private static int mDebuggable  =  LEVEL_VERBOSE;//如果设置为LEVEL_NONE全部都不输出;


    public static void setmTag(String mTag) {
        LogUtils.mTag = mTag;
    }

    public static void setmDebuggable(int mDebuggable) {
        LogUtils.mDebuggable = mDebuggable;
    }

    //以下在当前正在输出之前进行校验,
    // 如果设置的输出标准宽松程度大于当前方法对应的输出宽松程度,那么才能进行当前输出,否则不予输出

    public static void v(String msg){
        if(mDebuggable>=LEVEL_VERBOSE){
            Log.v(mTag,msg);
        }
    }

    public static void d(String msg){
        if(mDebuggable>=LEVEL_DEBUG){
            Log.d(mTag,msg);
        }
    }

    public static void i(String msg){
        if(mDebuggable>=LEVEL_INFO){
            Log.i(mTag,msg);
        }
    }

    //如果要打印大于等于WARNING的的信息那么,还要考虑此时程序会抛出
    //异常,所以必须要把异常进行打印
    public static void w(String msg){
        if(mDebuggable>=LEVEL_WARN){
            Log.w(mTag,msg);
        }
    }

    public static void w(Throwable tr){
        if(mDebuggable>LEVEL_WARN){
            Log.w(mTag,"",tr);
        }
    }

    public static void w(String msg,Throwable tr){
        if(mDebuggable>LEVEL_WARN){
            Log.w(mTag,msg,tr);
        }
    }

    /** 以级别为 e 的形式输出LOG */
    public static void e(String msg) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, msg);
        }
    }

    /** 以级别为 e 的形式输出Throwable */
    public static void e(Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr);
        }
    }

    /** 以级别为 e 的形式输出LOG信息和Throwable */
    public static void e(String msg, Throwable tr) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            Log.e(mTag, msg, tr);
        }
    }
}
