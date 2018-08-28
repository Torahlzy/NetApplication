package com.torahli.myapplication.framwork.fragment;

import com.umeng.analytics.MobclickAgent;

abstract class BaseUmengFragment extends AppFragment {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
}
