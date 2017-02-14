package com.loginair.gshopping.utils;

/**
 * Created by PineChang on 2017/2/14.
 */

public class StringUtils {
    public static boolean isEmptyString(String value){

        //判断是否是空串;
       return  value!=null && !"".equalsIgnoreCase(value) && !"null".equalsIgnoreCase(value);


    }
}
