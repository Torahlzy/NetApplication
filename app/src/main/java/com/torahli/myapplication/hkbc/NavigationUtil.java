package com.torahli.myapplication.hkbc;

import android.content.Context;
import android.content.Intent;

import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.databean.ILink;
import com.torahli.myapplication.hkbc.topiccontent.picdetails.PicDetailActivity;
import com.torahli.myapplication.hkbc.topiccontent.portrait.TopicContentPortraitActivity;
import com.torahli.myapplication.hkbc.topiclist.TopicListFragment;
import com.torahli.myapplication.hkbc.topiclist.texttitle.TextTitleListFragment;

public class NavigationUtil {
    public static final String INTENT_LINK = "INTENT_LINK";


    /**
     * 打开"带预览图样式列表"页
     */
    public static void startPicTopicList(BaseFragment fragment, String link, String title) {
        fragment.extraTransaction()
                .start(TopicListFragment.newInstance(fragment.getNoneNullActivity(), link, title));
    }

    /**
     * 打开"文字样式列表"页
     */
    public static void startTextTopicList(BaseFragment fragment, String link, String title) {
        fragment.extraTransaction()
                .start(TextTitleListFragment.newInstance(fragment.getNoneNullActivity(), link, title));
    }

    /**
     * 打开内容为图片的网址
     */
    public static void startPicContent(Context context, ILink link) {
        Intent intent = new Intent(context, TopicContentPortraitActivity.class);
        intent.putExtra(INTENT_LINK, link.getLink());
        context.startActivity(intent);
    }

    /**
     * 打开单张图片查看
     */
    public static void startPicDetialsPage(Context context, String picUrl) {
        Intent intent = new Intent(context, PicDetailActivity.class);
        intent.putExtra(INTENT_LINK, picUrl);
        context.startActivity(intent);
    }

}
