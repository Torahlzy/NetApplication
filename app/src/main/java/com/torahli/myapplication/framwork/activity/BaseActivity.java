package com.torahli.myapplication.framwork.activity;

import android.widget.Toast;

/**
 * 所有常规activity都应该继承此类
 */
public abstract class BaseActivity extends BaseUmengActivity {
    /**
     * 子类有SnakeBar的应该替换掉实现
     *
     * @param msg
     */
    public void showTips(String msg) {
        showToast(msg);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
