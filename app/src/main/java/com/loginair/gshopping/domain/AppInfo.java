package com.loginair.gshopping.domain;

import java.util.ArrayList;

/**
 * Created by PineChang on 2017/2/21.
 */

public class AppInfo {
    //app的名字描述
    public  String des;
    //app的下载url
    public  String  downloadUrl;
    //app的iconUrl
    public  String iconUrl;
    //app的id
    public String id;

    public String name;

    public String packageName;//安装包的名字
    public  long size ;//安装包的大小

    public  float stars //app的星级

    //应用详情页要用到的属性
    public String author ;
    public String date;
    public String downloadNum;
    public String version;
    public ArrayList<SafeInfo> safe;
    public ArrayList<String> screen;

    //当一个类是静态内部类的时候,这个静态内部类说白了和外部类没有区别
    public static  class SafeInfo{
        public String safeDes;
        public String safeDesUrl;
        public String safeUrl;
    }


}
