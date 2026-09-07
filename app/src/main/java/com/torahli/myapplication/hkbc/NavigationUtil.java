package com.torahli.myapplication.hkbc;

import android.content.Context;
import android.content.Intent;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.databean.ILink;
import com.torahli.myapplication.hkbc.topiccontent.portrait.TopicContentPortraitActivity;
import com.torahli.myapplication.hkbc.topiclist.TopicListFragment;
import com.torahli.myapplication.hkbc.topiclist.texttitle.TextTitleListFragment;

public class NavigationUtil {
    public static final String INTENT_LINK = "INTENT_LINK";
    /**
     * 页面跳转日志统一 tag（logcat 里用 adb logcat | grep "页面跳转" 过滤即可）
     */
    private static final String PAGE_JUMP_TAG = "页面跳转";


    /**
     * 打开"带预览图样式列表"页
     */
    public static void startPicTopicList(BaseFragment fragment, String link, String title) {
        Tlog.i(PAGE_JUMP_TAG, "【跳转】打开\"带预览图样式列表\"页 TopicListFragment\nlink=" + link + "\ntitle=" + title);
        fragment.extraTransaction()
                .start(TopicListFragment.newInstance(fragment.getNoneNullActivity(), link, title));
    }

    /**
     * 打开"文字样式列表"页
     */
    public static void startTextTopicList(BaseFragment fragment, String link, String title) {
        Tlog.i(PAGE_JUMP_TAG, "【跳转】打开\"文字样式列表\"页 TextTitleListFragment\nlink=" + link + "\ntitle=" + title);
        fragment.extraTransaction()
                .start(TextTitleListFragment.newInstance(fragment.getNoneNullActivity(), link, title));
    }

    /**
     * 打开内容为图片的网址
     */
    public static void startPicContent(Context context, ILink link) {
        Tlog.i(PAGE_JUMP_TAG, "【跳转】打开\"图片内容(纵向看图)\"页 TopicContentPortraitActivity\nlink=" + link.getLink());
        Intent intent = new Intent(context, TopicContentPortraitActivity.class);
        intent.putExtra(INTENT_LINK, link.getLink());
        context.startActivity(intent);
    }

}
