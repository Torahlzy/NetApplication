package com.torahli.myapplication.hkbc.topiccontent;

import android.arch.lifecycle.MutableLiveData;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;
import com.torahli.myapplication.hkbc.net.HKBCProtocol;
import com.torahli.myapplication.hkbc.topiccontent.bean.TopicContent;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class TopicContentViewModel extends BaseViewModel {
    @Nonnull
    MutableLiveData<TopicContent> contentLiveData;

    public MutableLiveData<TopicContent> getContentLiveData() {
        if (contentLiveData == null) {
            contentLiveData = new MutableLiveData<>();
        }
        return contentLiveData;
    }


    public void initData(String link) {
        HKBCProtocol.getTopicContent(link)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, TopicContent>() {
                    @Override
                    public TopicContent apply(String s) throws Exception {
                        return TopicContentParser.parser(s);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<TopicContent>() {

                    @Override
                    public void onNext(TopicContent topicContent) {
                        contentLiveData.setValue(topicContent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Tlog.printException("torahlog", e);
                        TopicContent value = new TopicContent();
                        contentLiveData.setValue(value.setError(NetErrorType.NetError, "报错" + e.getMessage()));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
