package com.loginair.gshopping.ui.fragment;

import com.lidroid.xutils.db.converter.IntegerColumnConverter;

import java.util.HashMap;

/**
 * Created by PineChang on 2017/2/15.
 */
//用工厂模式的好处是,懒加载,以及不会重复创建,可以复用原来的已经创建好的Fragment
public class FragmentFactory {

FragmentFactory
    private static HashMap<Integer,BaseFragment> mFragments = new HashMap<Integer,BaseFragment>();

    public static BaseFragment createFragment(int pos){
        //先从集合中国取,如果没有,才创建对象,提高了性能
        BaseFragment fragment = mFragments.get(pos);



        if(fragment == null){
            switch (pos){

            }

            mFragments.put(pos,fragment)
        }

        return fragment;
    }
}
