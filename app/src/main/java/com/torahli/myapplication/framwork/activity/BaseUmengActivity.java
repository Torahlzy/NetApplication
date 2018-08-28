package com.torahli.myapplication.framwork.activity;

import com.umeng.analytics.MobclickAgent;

/**
 * 友盟的功能
 */
abstract class BaseUmengActivity extends AppActivity {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
