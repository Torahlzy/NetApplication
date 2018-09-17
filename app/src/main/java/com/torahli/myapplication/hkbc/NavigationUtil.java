package com.torahli.myapplication.hkbc;

import com.torahli.myapplication.framwork.fragment.BaseFragment;
import com.torahli.myapplication.hkbc.topiclist.TopicListFragment;

public class NavigationUtil {
    /**
     * 启动帖子列表页
     */
    public static void startTopicList(BaseFragment fragment, String link, String title) {
        fragment.extraTransaction()
                .start(TopicListFragment.newInstance(fragment.getNoneNullActivity(), link, title));
    }
}
