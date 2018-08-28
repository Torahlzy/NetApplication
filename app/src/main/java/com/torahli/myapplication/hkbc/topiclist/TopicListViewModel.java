package com.torahli.myapplication.hkbc.topiclist;

import android.arch.lifecycle.MutableLiveData;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;
import com.torahli.myapplication.hkbc.net.HKBCProtocol;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class TopicListViewModel extends BaseViewModel {
    @Nonnull
    MutableLiveData<TopicList> liveData;

    public MutableLiveData<TopicList> getTopicListLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        return liveData;
    }

    public void initData(String url){
        HKBCProtocol.getTopicList(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, TopicList>() {
                    @Override
                    public TopicList apply(String s) throws Exception {
                        return TopicListParser.parser(s);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<TopicList>() {

                    @Override
                    public void onNext(TopicList topicContent) {
                        liveData.setValue(topicContent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Tlog.printException("torahlog", e);
                        TopicList value = new TopicList();
                        liveData.setValue(value.setError(NetErrorType.NetError, "报错" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
