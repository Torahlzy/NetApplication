package com.torahli.myapplication.hkbc.databean;

import com.torahli.myapplication.hkbc.home.ItemType;
import com.torahli.myapplication.hkbc.net.HKBCProtocolUtil;

public class TextTopic extends Topic {

    public TextTopic(String title, String link) {
        super(title, null, link);
    }

    @Override
    public int getItemType() {
        //表示该对象跳转进去，是text列表样式
        return ItemType.TextTopic;
    }


    /**
     * 自定义的一个地址
     *
     * @return
     */
    public static TextTopic newInnerComicGuideLink() {
        return new TextTopic("漫画主题", HKBCProtocolUtil.getWholeUrl("forum-79-1.html"));
    }
}
