package com.loginair.gshopping.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loginair.gshopping.R;
import com.loginair.gshopping.utils.UIUtils;

/**
 * Created by PineChang on 2017/2/23.
 */

public class MoreHolder extends BaseHolder<Integer> {
    //加载更多的几种状态
    //正在加载更多
    //加载更多失败
    //没有更所数据;
    public  static  final int  STATE_MORE_MORE = 1;
    public static final int  STATE_MORE_ERROR = 2;
    public  static final int STATE_MORE_NONE = 3;
   //用来保存RootView中的子View;
    private LinearLayout llLoadMore;
    private TextView tvLoadError;

    public MoreHolder(boolean hasMore){
        super();//这里面调用父类的初始化方法,将视图View初始化好

        //子类重写,不仅把视图初始化好,还把数据传递减去
        setData(hasMore?STATE_MORE_MORE:STATE_MORE_NONE)

    }
    //父类初始化的时候将View转备好
    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_more);
        llLoadMore = (LinearLayout)view.findViewById(R.id.ll_load_more);
        tvLoadError = (TextView)view.findViewById(R.id.tv_load_error);
        return view;
    }
    //子类在在构造方法中就已经把数据准备好了//根据数据去驱动视图的表现
    @Override
    public void refreshView(Integer data) {

        switch(data){
            case STATE_MORE_MORE:
                llLoadMore.setVisibility(View.VISIBLE);
                tvLoadError.setVisibility(View.GONE);
                break;
            case STATE_MORE_NONE:
                llLoadMore.setVisibility(View.GONE);
                tvLoadError.setVisibility(View.GONE);
                break;
            case STATE_MORE_ERROR;
                llLoadMore.setVisibility(View.GONE);
                tvLoadError.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }

    }
}
