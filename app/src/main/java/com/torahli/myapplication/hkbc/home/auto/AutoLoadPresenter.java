package com.torahli.myapplication.hkbc.home.auto;

import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.torahli.myapplication.framwork.bean.IResultListener;
import com.torahli.myapplication.hkbc.home.HomePageViewModel;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.login.LoginViewModel;

/**
 * 自动处理首次进入的一系列工作
 * 1.登陆
 * 2.加载首页内容
 * 3.读取多个帖子内容
 * <p>
 * 内容读取逻辑
 * 1.读取2个帖子，凑够5张图片，添加到界面
 * 2.每次读取到还剩3张图片时，加载下一批图片.
 * 3.如果加载下一批图片时，发现已经没有下一个帖子缓存，则加载下一帖子的缓存
 *
 * <p>
 * 功能
 * 1.每个步骤有回调
 */
public class AutoLoadPresenter implements LifecycleObserver {

    public interface IProgressListener {
        void onProgress(int current, int total, boolean succeed, String msg);
    }

    private static final int TOTOL_STEP = 3;

    /**
     * 开始
     */
    public void start(@NonNull IProgressListener listener) {
        autoLogin(listener);
    }

    /**
     * 自动登陆
     */
    private void autoLogin(final IProgressListener listener) {
        new LoginViewModel().startLogin("hktor9876", "Hkbc9876.", new IResultListener<String>() {
            @Override
            public void onSucceed(String s) {
                listener.onProgress(1, TOTOL_STEP, true, "登陆成功");
                onLoginSucceed(listener);
            }

            @Override
            public void onError(@Nullable Throwable e, String msg) {
                listener.onProgress(1, TOTOL_STEP, false, "登陆失败");
            }
        });
    }

    private void onLoginSucceed(final IProgressListener listener) {
        new HomePageViewModel().initData(new IResultListener<HomePage>() {
            @Override
            public void onSucceed(HomePage homePage) {
                listener.onProgress(2, TOTOL_STEP, false, "加载列表成功");
            }

            @Override
            public void onError(@Nullable Throwable e, String msg) {
                listener.onProgress(2, TOTOL_STEP, false, "加载列表失败");
            }
        });
    }
}
