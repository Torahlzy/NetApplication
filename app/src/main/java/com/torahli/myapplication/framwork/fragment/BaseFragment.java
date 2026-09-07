package com.torahli.myapplication.framwork.fragment;

import androidx.appcompat.app.ActionBar;
import android.text.TextUtils;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;

import javax.annotation.Nonnull;

/**
 * todo 所有子类在fragment初始化时必须设置{@link #setNoneNullActivity(BaseActivity)}
 */
public abstract class BaseFragment extends BaseNavFragment {

    public void showTips(String msg) {
        getNoneNullActivity().showTips(msg);
    }

    public void showToast(String msg) {
        getNoneNullActivity().showToast(msg);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //保留：便于排查时确认“当前在哪个Fragment页面”
        Tlog.i("页面跳转", "【当前页面(Fragment)】" + getClass().getSimpleName() + "，标题=" + getTitle());
        if (!TextUtils.isEmpty(getTitle())) {
            setTitle(getTitle());
        }
    }

    /**
     * 如果返回空，则不设置标题
     * @return
     */
    public abstract String getTitle();

    /**
     * 设置activity的标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        BaseActivity activity = getNoneNullActivity();
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    public BaseActivity mNoneNullActivity;

    @Nonnull
    public BaseActivity getNoneNullActivity() {
        return getActivity() == null ? mNoneNullActivity : (BaseActivity) getActivity();
    }

    public void setNoneNullActivity(BaseActivity activity) {
        this.mNoneNullActivity = activity;
    }
}
