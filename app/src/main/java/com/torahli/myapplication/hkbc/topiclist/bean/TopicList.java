package com.torahli.myapplication.hkbc.topiclist.bean;

import com.torahli.myapplication.framwork.bean.BaseLiveData;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.hkbc.databean.Topic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class TopicList extends BaseLiveData {
    @Nonnull
    private final List<Topic> topicList = new ArrayList<>();

    private String pageName;

    private LinkedHashMap<String, String> otherPages;
    /**
     * true:初始化，false:加载更多
     */
    private boolean isInit = true;

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public LinkedHashMap<String, String> getOtherPages() {
        return otherPages;
    }

    public void setOtherPages(@Nonnull LinkedHashMap<String, String> otherPages) {
        this.otherPages = otherPages;
    }

    public TopicList setError(int netError, String msg) {
        setErrorAndMsg(netError, msg);
        topicList.clear();
        return this;
    }

    /**
     * 加载第一页数据时使用
     *
     * @param topics
     * @return
     */
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

    @Override
    public String toString() {
        return "TopicList{" +
                "topicList=" + topicList +
                ", pageName='" + pageName + '\'' +
                ", otherPages=" + otherPages +
                ", isInit=" + isInit +
                ", error=" + error +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
