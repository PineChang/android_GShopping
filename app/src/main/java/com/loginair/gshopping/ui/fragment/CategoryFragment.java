package com.loginair.gshopping.ui.fragment;

import android.view.View;

import com.loginair.gshopping.domain.CategoryInfo;
import com.loginair.gshopping.http.protocol.CategoryProtocal;
import com.loginair.gshopping.ui.adapter.MyBaseAdapter;
import com.loginair.gshopping.ui.holder.BaseHolder;
import com.loginair.gshopping.ui.view.ConditionsPage;
import com.loginair.gshopping.ui.view.MyListView;
import com.loginair.gshopping.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by PineChang on 2017/2/24.
 */

public class CategoryFragment extends  BaseFragment {
    //用来第一次请求的时候,请求的前20条数据
    private ArrayList<CategoryInfo> data;
    //根据网络返回的情况来选择显示哪一种视图的方法判断
    @Override
    public ConditionsPage.ResultState onLoad() {
        CategoryProtocal protocal = new CategoryProtocal();
        data = protocal.getData(0);
        //check(data)将会返回成功或者失败的结果状态封装即ResultState
        return check(data);
    }
   //如果请求的返回状态成功,那么要返回的成功视图
    @Override
    public View onCreateSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        myListView.setAdater(new CategoryAdapter(data))
    }

    class CategoryAdapter extends MyBaseAdapter<CategoryInfo>{
        /*java：为什么子类一定要实现父类的有参构造方法？0
        Java代码  收藏代码
        public class Panda extends Xx{
            //报错:Implicit super constructor Xx() is undefined for default constructor. Must define an explicit constructor
        }
        class Xx
        {
            public Xx(int i) {
                System.out.println(12);
            }
        }  */

         /*你在Xx类里定义了一个带参数的构造方法，那么这个Xx类就没有无参数的构造方法了。也就是默认的无惨
         构造方法就没有了;

    子类在继承父类时，如果没有相同的带参构造方法，那么他就需要在其构造方法中明确的通过super()调用父类的带参构造方法，否则构造不出父类，从而也构造不出他自己了。

    你如果在父类中写个不带参数的构造方法，就可以不用实现父类的带参构造方法了。*/

         //子类一定要实现父类带惨的构造方法
        public  CategoryAdapter(ArrayList<CategoryInfo> data){
            super(data);
        }
        //因为是返回两种正常类型加上一种加载更多类型,所以要重写父类,返回cell种类数量的方法

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        //同理要重写父类返回哪一种类型的方法


        @Override
        public int getInnerType(int position) {
            //拿到当前位置的数据,由数据决定由哪一种类型
            CategoryInfo info = data.get(position);
            if(info.isTitle){
                return super.getInnerType(position)+1;
            }else{
                return super.getInnerType(position)
            }

        }

        @Override
        public BaseHolder<CategoryInfo> getHolder(int position) {
           CategoryInfo info = data.get(position);
            if(info.isTitle){
                return new TitleHolder();
            }else {
                return new  CategoryHolder();
            }
        }

        @Override
        public boolean hasMore() {
            //return false代表吟唱加载更所的布局;
            return false;
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            //不需要加载更多,那么就不用loadMore方法了
            return null;
        }
    }



}
