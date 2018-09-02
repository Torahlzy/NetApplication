package com.torahli.myapplication.hkbc.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.home.bean.HomePage;

import javax.annotation.Nonnull;

public class HomePageFragment extends BaseFragment {
    @Nonnull
    protected RequestManager fragmentGlide;
    private HomePageViewModel homePageViewModel;
    private RecyclerView homeList;
    private HomeAdapter homeAdapter;
    private EasyRefreshLayout refreshLayout;

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

    private void initView(final View view) {
        homeList = (RecyclerView) view.findViewById(R.id.hk_home_recyclerview);
        homeAdapter = new HomeAdapter(fragmentGlide, this);
        getLifecycle().addObserver(homeAdapter);
        homeList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        homeList.setAdapter(homeAdapter);
        //下拉刷新
        refreshLayout = (EasyRefreshLayout) view.findViewById(R.id.hk_home_easyrefresh);
        refreshLayout.setLoadMoreModel(LoadModel.NONE);
        refreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                homePageViewModel.initData();
            }
        });

        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel.class);
        homePageViewModel.getHomePageData().observe(this, new android.arch.lifecycle.Observer<HomePage>() {
            @Override
            public void onChanged(@Nullable HomePage homePage) {
                if (Tlog.isShowLogCat()) {
                    Tlog.d(TAG, "首页更新 --- homePage:" + homePage);
                }
                refreshLayout.refreshComplete();
                if (homePage == null || homePage.isError()) {
                    String errorMsg = homePage == null ? "无数据" : homePage.getErrorMsg();
                    Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG).show();
                } else {
                    homeAdapter.setNewData(homePage.getAllData());
                    homeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        homePageViewModel.initData();
    }
}
