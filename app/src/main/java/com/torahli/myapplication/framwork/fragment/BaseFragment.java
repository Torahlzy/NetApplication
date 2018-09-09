package com.torahli.myapplication.framwork.fragment;

import com.torahli.myapplication.framwork.activity.BaseActivity;

public class BaseFragment extends BaseUmengFragment {

    public void showTips(String msg) {
        ((BaseActivity) getActivity()).showTips(msg);
    }
}
