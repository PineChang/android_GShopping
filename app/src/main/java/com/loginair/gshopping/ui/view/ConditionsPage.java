package com.loginair.gshopping.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.loginair.gshopping.R;
import com.loginair.gshopping.manager.ThreadManager;
import com.loginair.gshopping.utils.UIUtils;

/**
 * Created by PineChang on 2017/2/15.
 */

public abstract class ConditionsPage extends FrameLayout {


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
        //初始化正在加载页面
        if(mLoadingPage==null){
            mLoadingPage= UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }
        //初始化错误页面
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
        //初始化空数据页面
        if(mEmptyPag==null){
            mEmptyPage = UIUtils.inflate(R.layout.page_empty);
        }

        //根据当前状态决定显示那个布局
        showCurrentPage();


    }
    //状态设计模式必备的当前条件判断驱动某一情况出现
    private void showCurrentPage(){
        mLoadingPage .setVisibility((mCurrentState==STATE_LOAD_UNDO||mCurrentState==STATE_LOAD_LOADING)?View.VISIBLE:View.GONE);
        mErrorPage.setVisibility((mCurrentState==STATE_LOAD_ERROR)?View.VISIBLE:View.GONE);
        mEmptyPage.setVisibility(mCurrentState==STATE_LOAD_EMPTY?View.VISIBLE:View.GONE);
        //成功页面比较特殊,不进行初始化,因为成功页面是由子类实现的并且如果提前加载会消耗性能
        if(mSuccessPage==null && mCurrentState==STATE_LOAD_SUCCESS){
            mSuccessPage = onCreateSuccessView();
            if(mSuccessPage!=null){
                addView(mSuccessPage);
            }

        }
        //如果成功页面不为空,那么就直接进行判断
        if(mSuccessPage!=null){
            mSuccessPage.setVisibility(mCurrentState==STATE_LOAD_SUCCESS?View.VISIBLE:View.GONE);

        }
    }

    public void loadData(){
        //在加载的时候判断一下当前的状态是否是加载状态,如果不是,那么就把当前状态改为加载状态
        if(mCurrentState!=STATE_LOAD_LOADING){
            mCurrentState = STATE_LOAD_LOADING;
        }
        //出现加载页面
        showCurrentPage();

        //开始真的加载数据

        ThreadManager.getmThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //开始网络请求
                 final ResultState resultState = onload();

                //根据网络请求的结果,在主线程中跟新UI
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resultState != null){
                            mCurrentState = resultState.getState;
                            showCurrentPage();
                        }
                    }
                });

            }
        });

    }

    public abstract  View onCreateSuccessView();
    //根据网络请求的结果返回一个合适的枚举
    public abstract ResultState onload();

    //用枚举将要出现的网络请求结果列出来
    public enum ResultState{
        STATE_SUCCESS(STATE_LOAD_SUCCESS),STATE_EMPTY(STATE_LOAD_EMPTY),STATE_ERROR(STATE_LOAD_ERROR);

        private int state;
        private ResultState(int state){this.state = state;}
    }
}















