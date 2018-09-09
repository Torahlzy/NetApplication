package com.torahli.myapplication.hkbc.databean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.torahli.myapplication.hkbc.home.ItemType;

/**
 * 主题。
 */
public class Topic implements MultiItemEntity, ILink {
    private String title;
    /**
     * 主题的跳转网址
     */
    private String link;
    /**
     * 配图
     */
    private String picUrl;

    private String timeStr;

    public Topic(String title, String picUrl, String link) {
        this.title = title;
        this.picUrl = picUrl;
        this.link = link;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        return ItemType.Topic;
    }

    @Override
    public String getLink() {
        return link;
    }
}
