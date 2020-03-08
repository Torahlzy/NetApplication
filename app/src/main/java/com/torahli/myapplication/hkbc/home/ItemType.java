package com.torahli.myapplication.hkbc.home;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 首页的条目，跳转后的页面样式。
 * 既：取值不同，跳转的页面不同，处理逻辑也不一样
 * 仅用在首页
 */
@IntDef
@Retention(RetentionPolicy.SOURCE)
public @interface ItemType {

    int Banners = 1;
    int PicTopicList = 2;
    int TextTopic = 3;
}