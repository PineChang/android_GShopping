package com.loginair.gshopping.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.loginair.gshopping.manager.ThreadManager;
import com.loginair.gshopping.ui.holder.BaseHolder;
import com.loginair.gshopping.ui.holder.MoreHolder;
import com.loginair.gshopping.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by PineChang on 2017/2/23.
 */

public abstract class  MyBaseAdapter<T> extends BaseAdapter {
    //Adapter的cell有两种类型;
    // 最后一行为加载更多的类型;
    //其他的行为正常的cell类型
     public static final int  TYPE_NORMAL = 1;
     public static final int  TYPE_MORE   = 0;

    //在创建Adapter的时候就将数据传递进去
    private ArrayList<T> data;

    public MyBaseAdapter(ArrayList<T> data){
        this.data = data;
    }
    //获取当前的集合的大小
    public  int getListSize(){
        return data.size();
    }

    @Override
    public int getCount() {
        //加上最后面的加载更多cell
        return this.data.size()+1;
    }

    @Override
    //每个cell提供的数据
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        //返回cell的种类数量
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        //返回在哪一个位置,返回哪一种cell;
        if (position==getCount()-1){

            return TYPE_MORE;
        }else{
            //在前n-1中用正常的类型,在最后一个用加载更多类型;

          return getInnerType(position);

        }

    }
    //为什么要把正常的类型提取出来是因为,在前n-1个cell中子类也可能把正常的类型
    //再细分为其他类型,所以需要将吧写死的东西,提取成为方法,便于子类重写;
    public  int  getInnerType(int position){
        return TYPE_NORMAL;
    }
    //子类必须实现的获取holder的方法,holder里面有View,有传递进去data后怎么绑定到View上的逻辑
    public  abstract  BaseHolder<T> getHolder(int position);
    private boolean isLoadMore = false;
    //子类
    public boolean hasMore(){
        return true;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BaseHolder holder;
        //1,怎么拿到holder
        if(view == null){
            //如果view为null说明创建的cell还是前面几个,没有占满屏幕,还没有多的用复用cell的机制,
            //所以需要创建cell
            if(getItemViewType(i)==TYPE_MORE){
                holder = new MoreHolder(hasMore())
            }else{
                holder = getHolder(i);
            }

        }else{
            //如果不为null,说明能够复用
            holder = (BaseHolder)view.getTag();//如果不为空那么就通过Tag拿到这个holder
        }

        //拿到holder后开始对holder中的view绑定数据
        if(getItemViewType(i)!=TYPE_MORE){
            //如果此时拿到的holder不是最后一个要加载更多的holder,那么就直接data赋值进去
            holder.setData(getItem(i))
        }else{
            //如果是加载更多的holder,那么就转化为加载更多的holder
            MoreHolder  moreHolder = (MoreHolder)holder;
            if(moreHolder.getData()==MoreHolder.STATE_MORE_MORE){
                //注意这里为什么先加载数据,然后,再返回View呢?
                //可能原因在在加载的时候,是把loadMore中的加载放进了线程中
                //主线程也可以返回holder.getRootView();
                loadMore(moreHolder);
            }
        }

        return holder.getRootView();
    }

    public  void loadMore(final MoreHolder holder){
        if(!isLoadMore){
            isLoadMore = true;
        }
        //扔进线程池里进行加载,扔进去后此时,loadMore方法如果抢到执行权
        //就会执行完,这个有点像回调一样,此时就会返回正在加载的cell;
        //注意在java中看见线程就把它看成异步,这样就好理解了
        //注意在两种情况同时发生的情况下,推荐使用线程,比如这里,
        //第一要出现加载的cell视图,又要出现的同时加载数据,就要用到线程;
        ThreadManager.getmThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //这个onloadMore子类要实现的方法应该传进此时ArrayList中最后一个对象的编号,也就是最大编号吧?待确定;
                final ArrayList<T> moreData = onLoadMore();
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果加载的data为空说明,加载失败;不为null说明加载成功
                        if(moreData != null){
                            // 每一页有20条数据, 如果返回的数据小于20条, 就认为到了最后一页了
                            if (moreData.size()<20){
                                holder.setData(MoreHolder.STATE_MORE_NONE);
                                //如果此次加载的数据小于20条,那么再加载就是没有数据了,那么就给用户
                                //指示一下,刷新加载更多cell的状态为None;并且提示一下
                                Toast.makeText(UIUtils.getContext(), "没有更所数据了", Toast.LENGTH_SHORT).show();

                            }else{
                                //否则的话说明还有更多数据;给用户还有更多数据的cell视图
                                holder.setData(MoreHolder.STATE_MORE_MORE)
                            }
                            //刷新完后,就将得到的数据放进数组中
                            data.addAll(moreData);
                            //此时将数据加进去后,要通知Adapter数据变了,此时listView会从新
                            //从Adapter中拿到数据;
                            MyBaseAdapter.this.notifyDataSetChanged();

                        }else{
                            holder.setData(MoreHolder.STATE_MORE_ERROR);
                        }
                        //加载完后,就把标志是否正在加载改为false
                        isLoadMore = false;
                    }
                });
            }
        });

    }
    //子类必须实现的onLoadMore抽象方法
    public abstract  ArrayList<T> onLoadMore();
}
