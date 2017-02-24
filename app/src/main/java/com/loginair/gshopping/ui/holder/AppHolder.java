package com.loginair.gshopping.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loginair.gshopping.R;
import com.loginair.gshopping.domain.AppInfo;
import com.loginair.gshopping.http.HttpHelper;
import com.loginair.gshopping.utils.BitmapHelper;
import com.loginair.gshopping.utils.UIUtils;


/**
 * Created by PineChang on 2017/2/24.
 */

public class AppHolder extends BaseHolder<AppInfo>{
    private TextView  tvName,tvSize,tvDes;
    private ImageView  ivIcon;
    private RatingBar  rbStar;

    @Override
    public View initView() {
        View  view  = UIUtils.inflate (R.layout.list_item_home);
        //初始化控件
        tvName = (TextView)view.findViewById(R.id.tv_name);
        tvSize = (TextView)view.findViewById(R.id.tv_size);
        tvDes =   (TextView)view.findViewById(R.id.tv_des);
        ivIcon = (ImageView)view.findViewById(R.id.iv_icon);
        rbStar = (RatingBar)view.findViewById(R.id.rb_star);

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tvName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(),data.size));
        //让ivIcon中显示url指向的图片
        BitmapHelper.getBitmapUtils().display(ivIcon, HttpHelper.URL+"iamge?name="+data.iconUrl);
    }
}
