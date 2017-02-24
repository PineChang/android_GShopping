package com.loginair.gshopping.ui.fragment;

import android.view.View;

import com.loginair.gshopping.domain.AppInfo;
import com.loginair.gshopping.http.protocol.AppProtocol;
import com.loginair.gshopping.ui.adapter.MyBaseAdapter;
import com.loginair.gshopping.ui.holder.AppHolder;
import com.loginair.gshopping.ui.holder.BaseHolder;
import com.loginair.gshopping.ui.view.ConditionsPage;
import com.loginair.gshopping.ui.view.MyListView;
import com.loginair.gshopping.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by PineChang on 2017/2/17.
 */

public class AppFragment extends BaseFragment {
    private ArrayList<AppInfo> data;
    //先根据加载数据的情况来判定,到底要显示那种状态的页面
    @Override
    public ConditionsPage.ResultState onLoad() {
        AppProtocol  protocol = new AppProtocol();
        //传递0代表取前20条数据
        data = protocol.getData(0);//此时会发送请求
        return check(data);//此时会对请求的数据,进行结果状态封装;然后返回

    }
    //只有加载成功,此时成员属性data就有值了,并且是前20条数据,此时,才会加载下面成功视图
    //此成功视图的Adapter就需要创建的时候传递进去前20条数据
    @Override
    public View onCreateSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        //如果执行这个方法代表状态ResultState成功,并且data已经是前20条数据了,那么再Adapter创建
        //的时候传递进去
        myListView.setAdapter(new AppAdapter(data));
        return myListView;

    }


    //在这里用AppAdatpter
    class AppAdapter extends MyBaseAdapter<AppInfo>{
        //再创建Adapter的时候就要将前20条数据传递进去
        public AppAdapter(ArrayList<AppInfo> data){
            super(data);
        }
        @Override
        //拿到视图的方法由子类提供
        public BaseHolder<AppInfo> getHolder(int position) {
            //所有位置都用AppHolder();
           return new AppHolder();
        }
        //当加载更所的方法由子类提供
        @Override
        public ArrayList<AppInfo> onLoadMore() {
            AppProtocol  protocol  = new AppProtocol();
            //注意getListSize()是拿到当前ArrayList中的的对象数量,请求的顺序为为0-19,20-39,40-59.....
            ArrayList<AppInfo> moredata = protocol.getData(getListSize());
            return null;
        }
    }
}
