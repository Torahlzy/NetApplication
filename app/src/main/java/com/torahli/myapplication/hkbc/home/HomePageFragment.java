package com.torahli.myapplication.hkbc.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.snackbar.Snackbar;
import com.torahli.myapplication.AppConfig;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.R;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.framwork.activity.BaseActivity;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.setting.sethost.SetUrlDialogHelper;

import javax.annotation.Nonnull;

import androidx.lifecycle.Observer;

public class HomePageFragment extends BaseFragment implements SetUrlDialogHelper.IView {
    @Nonnull
    protected RequestManager fragmentGlide;
    private HomePageViewModel homePageViewModel;
    private RecyclerView homeList;
    private HomeAdapter homeAdapter;
    private SwipeRefreshLayout refreshLayout;

    public static HomePageFragment newInstance(BaseActivity activity) {
        HomePageFragment fragment = new HomePageFragment();
        fragment.setNoneNullActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hk_homepage_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentGlide = Glide.with(this);
        initView(view);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (homeAdapter != null) {
            homeAdapter.onResume();
        }
    }

    @Override
    public void onPause() {
        if (homeAdapter != null) {
            homeAdapter.onPause();
        }
        super.onPause();
    }

    private void initView(final View view) {
        homeList = view.findViewById(R.id.hk_home_recyclerview);
        homeAdapter = new HomeAdapter(fragmentGlide, this);
        homeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeList.setAdapter(homeAdapter);
        //下拉刷新
        refreshLayout = view.findViewById(R.id.hk_home_easyrefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePageViewModel.initData();
            }
        });

        homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        homePageViewModel.getHomePageData().observe(this, new Observer<HomePage>() {
            @Override
            public void onChanged(@Nullable HomePage homePage) {
                refreshLayout.setRefreshing(false);
                if (homePage == null || homePage.isError()) {
                    String errorMsg = homePage == null ? "无数据" : homePage.getErrorMsg();
                    Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG).show();
                } else {
                    homeAdapter.setNewData(homePage.getAllData());
                }
            }
        });
    }

    private void initData() {
        //判断是否有域名缓存，没有则使用默认域名 AppConfig.DEFAULT_HOST_URL
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getApplication());
        String url = SetUrlDialogHelper.checkHost(preferences.getString(SharedPrefsKey.hostUrl, AppConfig.DEFAULT_HOST_URL));
        if (!TextUtils.isEmpty(url)) {
            HKBCProtocolUtil.BASEURL = url;
            onHostSetted();
        } else {
            //默认域名非法时，才需要用户手动设置
            showSetUrlDialog();
        }
    }

    /**
     * 需要用户设置域名
     */
    private void showSetUrlDialog() {
        SetUrlDialogHelper.showSetUrlDialog(this);
    }

    /**
     * 设置完域名后回调此方法
     */
    @Override
    public void onHostSetted() {
        homePageViewModel.initData();
    }

    @Override
    public String getTitle() {
        return "首页";
    }
}
