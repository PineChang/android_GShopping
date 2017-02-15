package com.loginair.gshopping.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.loginair.gshopping.R;
import com.loginair.gshopping.utils.UIUtils;

/**
 * Created by PineChang on 2017/2/15.
 */

public class ConditionsPage extends FrameLayout {


    private  static final int STATE_LOAD_UNDO = 1;//未加载

    private static final int STATE_LOAD_LOADING = 2;//正在加载

    private static final int STATE_LOAD_ERROR = 3;//加载失败

    private static final int STATE_LOAD_EMPTY = 4;//数据为空

    private  static final int STATE_LOAD_SUCCESS = 5; //加载成功

    private int mCurrentState = STATE_LOAD_UNDO;//当前状态

    private View mLoadingPage;
    private View mErrorPage;
    private View mEmptyPage;
    private View mSuccessPage;

    public ConditionsPage(Context context) {
        super(context);
        initView();
    }

    public ConditionsPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ConditionsPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public void initView(){
        //初始化这几个界面的布局
        if(mLoadingPage==null){
            mLoadingPage= UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }
        if(mErrorPage==null){
            mErrorPage = UIUtils.inflate(R.layout.page_error);

            Button btnRetry  = (Button)mErrorPage.findViewById(R.id.btn_retry);
            btnRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
            addView(mErrorPage);
        }

        if(mEmptyPag==null){
            mEmptyPage = UIUtils.inflate(R.layout.page_empty);
        }

        //根据当前状态决定显示那个布局
        showCurrentPage();


    }
}
