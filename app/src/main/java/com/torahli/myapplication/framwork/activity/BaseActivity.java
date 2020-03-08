package com.torahli.myapplication.framwork.activity;

import androidx.appcompat.app.ActionBar;
import android.widget.Toast;

/**
 * 所有常规activity都应该继承此类
 */
public abstract class BaseActivity extends BaseNavActivity {
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

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }
}
