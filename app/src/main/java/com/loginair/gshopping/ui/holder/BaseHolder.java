package com.loginair.gshopping.ui.holder;

import android.view.View;

/**
 * Created by PineChang on 2017/2/17.
 */

public abstract class BaseHolder<T> {
    //holder里面有两个重要的属性,第一个是item的布局,一个是item的data
    private View mRootView;//这个布局肯定是View的子类
    private T   data;//这个data类型不确定,那么就用泛型表示
    //在初始化的时候就将View初始化好
    public BaseHolder(){
        mRootView = initView();
        mRootView.setTag(this);
    }
    //在new Baseholder的时候返回cell视图
    public abstract  View initView();

    //在传递进去数据的时候就将数据绑定的方法写好
    public Void setData(T data){
        this.data = data;
        refreshView(data);
    }
    public  abstract  void refreshView(T data);
    //提供拿到这个View和数据data的方法
    public View getRootView(){
        return mRootView;
    }
    public  T getData(){
        return data;
    }
}
