package com.loginair.gshopping.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by PineChang on 2017/2/17.
 */

public class BitmapHelper {
    //在xUtils外面包一层
    private static BitmapUtils mBitmapUtils = null;

    //单粒模式
    public static BitmapUtils getBitmapUtils(){
        if(mBitmapUtils==null){
            synchronized (BitmapHelper.class){
                if(mBitmapUtils==null){
                    mBitmapUtils = new BitmapUtils(UIUtils.getContext());
                }
            }
        }

        return mBitmapUtils;
    }
}
