package com.torahli.myapplication.hkbc.topiclist.texttitle;

import androidx.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.framwork.vm.BaseViewModel;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

/**
 * 文字主题列表
 */
public class TextTopicListViewModel extends BaseViewModel {
    @Nonnull
    MutableLiveData<TopicList> liveData;
    private LinkedHashMap<String, String> otherPages;
    private int nextIndex;//下一个网址在map中的索引。访问网络成功时再加

    public MutableLiveData<TopicList> getTopicListLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        return liveData;
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        if (nextIndex == -1) {
            throw new IllegalStateException("应该调初始化方法");
        }
        if (nextIndex < otherPages.size()) {
            Iterator<String> iterator = otherPages.values().iterator();
            int i = 0;
            String nextUrl = null;
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (i == nextIndex) {
                    nextUrl = next;
                    break;
                }
                i++;
            }
            loadData(nextUrl, false, nextIndex);
        }
    }

    public void initData(String url) {
        loadData(url, true, -1);
    }

    private void loadData(String url, final boolean isInit, final int index) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url为空");
        }
        HKBCProtocolUtil.getTopicList(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, TopicList>() {
                    @Override
                    public TopicList apply(String s) {
                        return TextTopicListParser.parser(s);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<TopicList>() {

                    @Override
                    public void onNext(TopicList topicContent) {
                        topicContent.setInit(isInit);
                        liveData.setValue(topicContent);
                        if (isInit) {
                            otherPages = topicContent.getOtherPages();
                        }
                        nextIndex = index + 1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Tlog.printException("torahlog", e);
                        TopicList value = new TopicList();
                        value.setInit(isInit);
                        liveData.setValue(value.setError(NetErrorType.NetError, "报错" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
