package com.loginair.gshopping.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by PineChang on 2017/2/24.
 */

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    public  void initView(){
        //设置选中某一行的时候,为透明色颜色
        this.setSelector(new ColorDrawable());
        this.setDivider(null);//去掉分割线
        //有时候滑动ListView看到会无意间看到背景,
        //那么下面的方法是让背景变为全透明;
        this.setCacheColorHint(Color.TRANSPARENT)

    }
}
