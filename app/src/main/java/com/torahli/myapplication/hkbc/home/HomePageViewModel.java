package com.torahli.myapplication.hkbc.home;

import android.arch.lifecycle.MutableLiveData;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;
import com.torahli.myapplication.hkbc.databean.TextTopic;
import com.torahli.myapplication.hkbc.home.bean.HomePage;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class HomePageViewModel extends BaseViewModel {
    MutableLiveData<HomePage> homePageLiveData;

    public MutableLiveData<HomePage> getHomePageData() {
        if (homePageLiveData == null) {
            homePageLiveData = new MutableLiveData<>();
        }
        return homePageLiveData;
    }

    public void initData() {
        HKBCProtocolUtil.getHomePage()
                .map(new Function<String, HomePage>() {
                    @Override
                    public HomePage apply(String s) throws Exception {
                        Document doc = Jsoup.parse(s);
                        if (Tlog.isShowLogCat()) {
                            Tlog.d(TAG, "apply --- 首页数据长度:" + s.length());
                        }
                        HomePage homePage = HomePageParser.INSTANCE.parseTopics(doc);
                        //添加一个自定义的连接
                        homePage.addCustomEntity(TextTopic.newInnerComicGuideLink());
                        return homePage;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<HomePage>() {
                    @Override
                    public void onNext(HomePage homePage) {
                        homePageLiveData.setValue(homePage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Tlog.printException("torahlog", e);
                        HomePage value = new HomePage();
                        homePageLiveData.setValue(value.setError(NetErrorType.NetError, "报错" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
