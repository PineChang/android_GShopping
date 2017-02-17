package com.loginair.gshopping.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loginair.gshopping.ui.view.ConditionsPage;
import com.loginair.gshopping.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by PineChang on 2017/2/15.
 */

public abstract  class BaseFragment extends Fragment {

    //拿到基础页面
    private ConditionsPage mConditionsPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //初始化这个ConditionsPage基础页面
        mConditionsPage = new ConditionsPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();

            }

            @Override
            public ResultState onload() {
                return BaseFragment.this.onLoad();
            }
        }

        return mConditionsPage;
    }
    //其实BaseFragment也不知道到底要什么成功页面和怎么发送数据,那么也把这两个方法抽象了,然后做成抽象类
    public  abstract  View onCreateSuccessView();
    public abstract ConditionsPage.ResultState onLoad();

    //子类都要用到的加载数据
    public  void loadData(){
        if(mConditionsPage!=null){
            mConditionsPage.loadData();
        }
    }
    //子类都要用到的对返回的数据合法性进行校验

    public ConditionsPage.ResultState check(Object obj){
        if(obj!=null){
            if(obj instanceof ArrayList){
                ArrayList list = (ArrayList)obj;
                //返回的结果有值,那么如果是个集合,那么如果集合没有元素,那么就是空数据,如果有就不是空数据
                return  list.isEmpty()?ConditionsPage.ResultState.STATE_EMPTY: ConditionsPage.ResultState.STATE_SUCCESS;

            }
        }
        //如果什么页面有返回,说明是有错
        return ConditionsPage.ResultState.STATE_ERROR;
    }
}
