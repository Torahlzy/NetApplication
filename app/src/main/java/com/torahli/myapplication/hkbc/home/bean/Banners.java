package com.torahli.myapplication.hkbc.home.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.torahli.myapplication.hkbc.bean.Topic;
import com.torahli.myapplication.hkbc.home.ItemType;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class Banners implements MultiItemEntity {
    @Nonnull
    private List<Topic> topicList = new ArrayList<>();

    public Banners(@Nonnull List<Topic> topicList) {
        this.topicList.addAll(topicList);
    }

    @Nonnull
    public List<Topic> getTopicList() {
        return topicList;
    }

    public List<String> getTopicListTitle() {
        List<String> titles = new ArrayList<>();
        if (topicList.size() > 0) {
            for (Topic topic : topicList) {
                titles.add(topic.getTitle());
            }
        }
        return titles;
    }

    @Override
    public int getItemType() {
        return ItemType.Banners;
    }

    @Override
    public String toString() {
        return "Banners{" +
                "topicList=" + topicList.size() +
                '}';
    }

}
