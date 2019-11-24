package com.torahli.myapplication.hkbc.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.R;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.bean.IResultListener;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.NavigationUtil;
import com.torahli.myapplication.hkbc.databean.ILink;
import com.torahli.myapplication.hkbc.home.auto.AutoLoadPresenter;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.login.LoginViewModel;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.setting.sethost.SetUrlDialogHelper;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;

/**
 * 这个页面只做loading用
 */
public class HomePageFragment extends BaseFragment implements SetUrlDialogHelper.IView {
    @Nonnull
    protected RequestManager fragmentGlide;
    private HomePageViewModel homePageViewModel;
    private RecyclerView homeList;
    private HomeAdapter homeAdapter;
    private EasyRefreshLayout refreshLayout;
    private RxSharedPreferences rxPreferences;
    private ProgressBar loadingBar;

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
        rxPreferences = MainApplication.getApplication().getRxPreferences();
        initView(view);
        initData();
    }

    private void initView(final View view) {
        loadingBar = (ProgressBar) view.findViewById(R.id.loadingProgressBar);
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
                    loadingBar.setMax(total);
                    loadingBar.setProgress(current);
                    showTips(msg);
                    if (current == total) {
                        NavigationUtil.startPicContent(getActivity(), new ILink() {
                            @Override
                            public String getLink() {
                                return "";
                            }
                        });
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
