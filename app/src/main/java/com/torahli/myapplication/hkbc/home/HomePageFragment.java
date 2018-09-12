package com.torahli.myapplication.hkbc.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.torahli.myapplication.R;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

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
        new MaterialDialog.Builder(getActivity())
                .title("设置域名")
                .content("初始使用必须设置域名，若不知道域名，去获得app的地方找")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("exp:http://www.baidu.com/", HKBCProtocolUtil.BASEURL, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Editable text = dialog.getInputEditText().getText();
                        String url = formatText(text);
                        if (!TextUtils.isEmpty(url)) {
                            HKBCProtocolUtil.BASEURL = url;
                            homePageViewModel.initData();
                        } else {
                            showToast("网址填写错误");
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    initData();
                                }
                            }, 1000);
                        }
                    }
                }).show();

    }

    private String formatText(Editable text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        String s = String.valueOf(text).toLowerCase();
        if (s.startsWith("http")) {
            if (s.endsWith("/")) {
                return s;
            } else {
                return s + "/";
            }
        }
        return "";
    }
}
