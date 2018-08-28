package com.torahli.myapplication.hkbc.net;

import com.torahli.myapplication.framwork.retrofit.MyRetrofit;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;

/**
 * 比思网络协议
 */
public class HKBCProtocol {
    public static final String BASEURL = "http://hkbbcc.xyz/";

    /**
     * 主页
     * @return
     */
    public static Flowable<String> getHomePage() {
        HomePageProtocol hkbc = MyRetrofit.create(BASEURL)
                .create(HomePageProtocol.class);
        Flowable<String> homePage = hkbc.getHomePage();
        return homePage;
    }

    /**
     * 登陆
     * @param map
     * @return
     */
     public static Flowable<String> getLogin(@FieldMap Map<String, String> map) {
         LoginProtocol loginProtocol = MyRetrofit.create(BASEURL)
                .create(LoginProtocol.class);
        Flowable<String> login1 = loginProtocol.login(map);
        return login1;
    }
    /**
     * 获得主题详情
     * @param url
     * @return
     */
    public static Flowable<String> getTopicContent(String url) {
        TopicListProtocol topicContentProtocol = MyRetrofit.create(BASEURL)
                .create(TopicListProtocol.class);
        Flowable<String> content = topicContentProtocol.getTopicList(url);
        return content;
    }

    /**
     * 获得图片类的主题详情
     * @param url
     * @return
     */
    public static Flowable<String> getTopicList(String url) {
        TopicContentProtocol topicContentProtocol = MyRetrofit.create(BASEURL)
                .create(TopicContentProtocol.class);
        Flowable<String> content = topicContentProtocol.getContent(url);
        return content;
    }



    public static String getWholeUrl(String url) {
        if (url == null) {
            return null;
        }
        if (url.startsWith("http")) {
            return url;
        } else {
            return BASEURL + (url.startsWith("/") ? url.substring(1) : url);
        }
    }

}
