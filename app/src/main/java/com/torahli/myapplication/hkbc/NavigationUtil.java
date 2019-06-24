package com.torahli.myapplication.hkbc;

import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.topiclist.TopicListFragment;
import com.torahli.myapplication.hkbc.topiclist.texttitle.TextTitleListFragment;

public class NavigationUtil {
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

}
