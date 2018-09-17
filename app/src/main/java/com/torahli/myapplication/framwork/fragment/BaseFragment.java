package com.torahli.myapplication.framwork.fragment;

import com.torahli.myapplication.framwork.activity.BaseActivity;

public class BaseFragment extends BaseNavFragment {

    public void showTips(String msg) {
        ((BaseActivity) getActivity()).showTips(msg);
    }

    public void showToast(String msg) {
        ((BaseActivity) getActivity()).showToast(msg);
    }
}
