package com.loginair.gshopping.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by PineChang on 2017/2/17.
 */

public class IOUtils {




    //用来关闭流
    public static boolean close(Closeable io){
        if(io!=null){
           try {
               io.close();

           }catch (IOException e){
               LogUtils.e(e);
           }


        }
        return true;
    }
}
