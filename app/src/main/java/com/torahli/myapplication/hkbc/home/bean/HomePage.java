package com.torahli.myapplication.hkbc.home.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.torahli.myapplication.framwork.bean.BaseLiveData;
import com.torahli.myapplication.framwork.bean.NetErrorType;
import com.torahli.myapplication.hkbc.databean.Topic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class HomePage extends BaseLiveData {
    @Nonnull
    private List<Topic> topicList = new ArrayList<>();

    private List<MultiItemEntity> customList = new ArrayList<>();

    private Banners banners1;

    public HomePage setError(int error, String msg) {
        setErrorAndMsg(error, msg);
        this.topicList.clear();
        return this;
    }

    public HomePage setBanners(Banners banners) {
        this.banners1 = banners;
        return this;
    }

    public List<MultiItemEntity> getAllData() {
        if (isError()) {
            return null;
        }
        List<MultiItemEntity> entityList = new ArrayList<>();
        if (banners1 != null) {
            entityList.add(banners1);
        }
        entityList.addAll(topicList);
        entityList.addAll(customList);
        return entityList;
    }

    public HomePage setTopics(List<Topic> topics) {
        if (topics == null || topics.isEmpty()) {
            setErrorAndMsg(NetErrorType.NoDataError, "设置的数据为空");
            this.topicList.clear();
        } else {
            setNoError();
            this.topicList.clear();
            this.topicList.addAll(topics);
        }
        return this;
    }

    public HomePage addCustomEntity(MultiItemEntity entity) {
        customList.add(entity);
        return this;
    }

    public List<Topic> getTopics() {
        return topicList;
    }

    @Override
    public String toString() {
        return "HomePage{" +
                "topicList=" + topicList.size() +
                ", banners1=" + banners1 +
                ", error=" + error +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
