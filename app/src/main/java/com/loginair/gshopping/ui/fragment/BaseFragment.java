package com.loginair.gshopping.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loginair.gshopping.ui.view.ConditionsPage;

/**
 * Created by PineChang on 2017/2/15.
 */

public class BaseFragment extends Fragment {

    private ConditionsPage mConditionsPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
