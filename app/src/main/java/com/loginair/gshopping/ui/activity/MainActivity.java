package com.loginair.gshopping.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.loginair.gshopping.R;
import com.loginair.gshopping.ui.view.PagerTab;
import com.loginair.gshopping.utils.UIUtils;

/**
 * Created by PineChang on 2017/2/14.
 */

public class MainActivity extends BaseActivity {
    //上面的标签
    private PagerTab mPagerTab;
    //中间的标签页;
    private ViewPager mViewPager;
    //标签页的adatper
    private MyAdpater myAdpater;

    private ActionBarDrawerToggle toggle;

    private
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerTab = (PagerTab)findViewById(R.id.pager_tab);
        mViewPager= (ViewPager)findViewById(R.id.viewpager);
        //将adapter设置进viewPager,将ViewPager设置进PagerTab
        myAdpater = new MyAdpater(getSupportFragmentManager());

        mViewPager.setAdapter(myAdpater);

        mPagerTab.setViewPager(mViewPager);

        //对PagerTab的事件进行重用
        mPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //加载actionBar
        initActionbar();


    }
    /**
     * FragmentPagerAdapter是PagerAdapter的子类, 如果viewpager的页面是fragment的话,就继承此类
     */
    class MyAdpater extends FragmentPagerAdapter{
        private String[] mTabNames;

        //在初始化Adapter的时候获得每个标签页的标签名字
        public MyAdpater(FragmentManager fm){
            super(fm);
            mTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }

        @Override
        public int getCount() {

            return mTabNames.length;
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }
    }
    //以下代码为控制ActionBar和侧边栏的交互逻辑
    //初始化actionBar
    private void initActionbar(){
        //拿到ActionBar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);//home处可以点击
        actionBar.setDisplayHomeAsUpEnabled(true);//显示左上角的返回键,当和侧边栏结合使用的时候展示三个杠图片


        //以下将DrawerLayout托付给toggle,然后
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawer,actionBar,R.string.drawer_open,R.string.drawer_close);
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.drawable.ic_drawer_am, R.string.drawer_open,
                R.string.drawer_close);
        //此时toggle就能控制drawer的开和关两种状态
        toggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //监听ActionBar上面的菜单选项点击
        switch(item.getItemId()){
            case android.R.id.home:
                toggle.onOptionsItemSelected(item);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
