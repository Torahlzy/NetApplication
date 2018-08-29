package com.torahli.myapplication.hkbc.topiclist.bean;

import com.torahli.myapplication.framwork.bean.BaseLiveData;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.hkbc.bean.Topic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class TopicList extends BaseLiveData {
    @Nonnull
    private final List<Topic> topicList = new ArrayList<>();

    public TopicList setError(int netError, String msg) {
        setErrorAndMsg(netError, msg);
        topicList.clear();
        return this;
    }

    public TopicList setTopicList(List<Topic> topics) {
        if (topics == null || topics.size() == 0) {
            setErrorAndMsg(NetErrorType.NoDataError, "解析详情出错");
        } else {
            super.setNoError();
            topicList.clear();
            topicList.addAll(topics);
        }
        return this;
    }

    @Nonnull
    public List<Topic> getTopicList() {
        return topicList;
    }
}
