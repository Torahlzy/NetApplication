package com.torahli.myapplication.hkbc.home.auto;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.IResultListener;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.datamanager.PicDataManager;
import com.torahli.myapplication.hkbc.home.HomePageViewModel;
import com.torahli.myapplication.hkbc.home.ItemType;
import com.torahli.myapplication.hkbc.home.bean.Banners;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.login.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void release() {
        Tlog.d("torahlog", "AutoLoadPresenter.release(..)--1:" + 1);
    }

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
                resolveHomePage(homePage, listener);
            }

            @Override
            public void onError(@Nullable Throwable e, String msg) {
                listener.onProgress(2, TOTOL_STEP, false, "加载列表失败");
            }
        });
    }

    private void resolveHomePage(HomePage homePage, final IProgressListener listener) {
        List<MultiItemEntity> allData = homePage.getAllData();

        List<Topic> topics = new ArrayList<>();
        if (allData.size() > 0) {
            for (MultiItemEntity datum : allData) {
                if (datum.getItemType() == ItemType.Banners) {
                    //banner里面的话题加入集合
                    Banners banner = (Banners) datum;
                    List<Topic> topicList = banner.getTopicList();
                    if (topicList.size() > 0) {
                        topics.addAll(topicList);
                    }
                } else {
                    //todo
                }
            }
            PicDataManager.getInstance().init(topics);
            listener.onProgress(3, TOTOL_STEP, true, "列表初始化完成");
        } else {
            listener.onProgress(3, TOTOL_STEP, false, "无数据");
        }
    }
}
