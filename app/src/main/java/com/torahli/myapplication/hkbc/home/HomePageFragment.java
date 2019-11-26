package com.torahli.myapplication.hkbc.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.ILink;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.datamanager.PicDataManager;
import com.torahli.myapplication.hkbc.home.auto.AutoLoadPresenter;
import com.torahli.myapplication.hkbc.setting.sethost.SetUrlDialogHelper;

import javax.annotation.Nonnull;

/**
 * 这个页面只做loading用
 */
public class HomePageFragment extends BaseFragment implements SetUrlDialogHelper.IView {
    @Nonnull
    protected RequestManager fragmentGlide;
    private NumberProgressBar loadingBar;

    public static HomePageFragment newInstance(BaseActivity activity) {
        HomePageFragment fragment = new HomePageFragment();
        fragment.setNoneNullActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hk_homepage_main2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentGlide = Glide.with(this);
        initView(view);
        initData();
    }

    private void initView(final View view) {
        loadingBar = (NumberProgressBar) view.findViewById(R.id.loadingProgressBar);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        AutoLoadPresenter autoLoadPresenter = new AutoLoadPresenter();
        getLifecycle().addObserver(autoLoadPresenter);
        autoLoadPresenter.start(new AutoLoadPresenter.IProgressListener() {
            @Override
            public void onProgress(int current, int total, boolean succeed, String msg) {
                if (succeed) {
                    loadingBar.setProgress((int) (current * 100f / total));
                    showTips(msg);
                    if (current == total) {
                        final Topic nextTopic = PicDataManager.getInstance().getNextTopic();
                        if (!TextUtils.isEmpty(nextTopic.getLink())) {
                            NavigationUtil.startPicContent(getActivity(), new ILink() {
                                @Override
                                public String getLink() {
                                    return nextTopic.getLink();
                                }
                            });
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        } else {
                            showTips("数据有误");
                        }
                    }
                } else {
                    showErrorView();
                }
            }
        });
    }

    private void showErrorView() {

    }


    /**
     * 设置完域名后回调此方法
     */
    @Override
    @Deprecated
    public void onHostSetted() {
//        homePageViewModel.initData();
    }

    @Override
    public String getTitle() {
        return "首页";
    }
}
