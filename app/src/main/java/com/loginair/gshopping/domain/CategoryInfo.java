package com.loginair.gshopping.domain;

/**
 * Created by PineChang on 2017/2/24.
 */
//分类有两种情况
// 第一种情况仅仅简单是个某一分类的标题
// 第二种情况是某一分类的内容,
// 内容的结构为,的一行有三个,每个是正方形,正方形里面上方是图片,下面是名字
public class CategoryInfo {
    //第一种情况;
    public  String  title;
    // 第一种情况添加是否是标题的判断
    public  boolean isTitle;

    //第二种情况
    public  String name1;
    public  String url1;

    public String name2;
    public String url2;

    public  String name3;
    public String url3;


}
