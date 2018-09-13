package com.torahli.myapplication.hkbc.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.R;
import com.torahli.myapplication.app.sharedpreferences.SharedPrefsKey;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import javax.annotation.Nonnull;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HomePageFragment extends BaseFragment {
    @Nonnull
    protected RequestManager fragmentGlide;
    private HomePageViewModel homePageViewModel;
    private RecyclerView homeList;
    private HomeAdapter homeAdapter;
    private EasyRefreshLayout refreshLayout;
    private RxSharedPreferences rxPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hk_homepage_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentGlide = Glide.with(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getApplication());
        rxPreferences = RxSharedPreferences.create(preferences);
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
        //判断是否有域名
        rxPreferences.getString(SharedPrefsKey.hostUrl).asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        String url = formatText(s);
                        if (!TextUtils.isEmpty(url)) {
                            HKBCProtocolUtil.BASEURL = url;
                            homePageViewModel.initData();
                        } else {
                            showSetUrlDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showSetUrlDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("设置域名")
                .content("初始使用必须设置域名，若不知道域名，去获得app的地方找")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("exp:http://www.baidu.com/", HKBCProtocolUtil.BASEURL, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Editable text = dialog.getInputEditText().getText();
                        String url = formatText(String.valueOf(text));
                        if (!TextUtils.isEmpty(url)) {
                            Flowable.just(url)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(rxPreferences.getString(SharedPrefsKey.hostUrl).asConsumer());
                            HKBCProtocolUtil.BASEURL = url;
                            homePageViewModel.initData();
                        } else {
                            showToast("网址填写错误");
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showSetUrlDialog();
                                }
                            }, 1000);
                        }
                    }
                }).show();
    }

    private String formatText(String text) {
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
