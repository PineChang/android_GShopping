package com.loginair.gshopping.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.loginair.gshopping.R;
import com.loginair.gshopping.domain.CategoryInfo;
import com.loginair.gshopping.utils.UIUtils;

import org.w3c.dom.Text;

/**
 * Created by PineChang on 2017/2/24.
 */

public class TitleHolder extends BaseHolder<CategoryInfo> {
    public TextView tvTitle;
    @Override
    public View initView() {
        View view  = UIUtils.inflate(R.layout.list_item_title);
        tvTitle = (TextView)view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tvTitle.setText(data.title);

    }
}
