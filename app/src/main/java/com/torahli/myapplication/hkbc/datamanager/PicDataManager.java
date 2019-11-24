package com.torahli.myapplication.hkbc.datamanager;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.hkbc.databean.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有缓存
 */
public class PicDataManager {
    private List<String> picList = new ArrayList<>();

    private List<Topic> topics = new ArrayList<>();
    private int topicIndex = 0;

    private static volatile PicDataManager instance;

    private PicDataManager() {
    }

    public static PicDataManager getInstance() {
        if (instance == null) {
            synchronized (PicDataManager.class) {
                if (instance == null) {
                    instance = new PicDataManager();
                }
            }
        }
        return instance;
    }

    public synchronized static void release() {
        //TODO release instance to avoid memory leak
        /* exit HandlerThread
         * instance = null;
         */
    }

    /**
     * 初始化话题列表
     *
     * @param page
     */
    public void init(List<Topic> page) {
        if (page.size() > 0) {
            topics.clear();
            topics.addAll(page);
            initPickList();
        } else {
            Tlog.w("torahlog", "数据有误");
        }
    }

    /**
     * 根据话题，解析图片列表
     */
    private void initPickList() {
        Topic topic = topics.get(topicIndex);
    }

    /**
     * 检查图片和话题缓存是否够用
     */
    private void checkPageCache() {

    }

    public Topic getNextTopic() {
        if (topics.size() > 0 && topicIndex >= 0 && topicIndex < topics.size()) {
            return topics.get(topicIndex++);
        } else {
            return null;
        }
    }

    public Topic getPreviousTopic() {
        if (topics.size() > 0 && topicIndex >= 0 && topicIndex < topics.size()) {
            return topics.get(topicIndex--);
        } else {
            return null;
        }
    }

}
