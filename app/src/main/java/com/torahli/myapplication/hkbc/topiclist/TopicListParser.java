package com.torahli.myapplication.hkbc.topiclist;

import com.torahli.myapplication.hkbc.bean.Topic;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TopicListParser {
    public static TopicList parser(String s) {
        Document doc = Jsoup.parse(s);
        Elements board = doc.select("div.boardnav div#threadlist");
        Elements cells = board.select("div.bm_c ul.ml > li");
        TopicList topicList = new TopicList();
        List<Topic> list = new ArrayList<>();
        if (cells != null && cells.size() > 0) {
            for (int i = 0; i < cells.size(); i++) {
                Element cell = cells.get(i);
                String picUrl = cell.select("img").get(0).attr("src");
                Elements topicLink = cell.select("h3 a");
                String title = topicLink.text();
                String link = topicLink.attr("href");
                String timeStr = cell.select("> div > em").get(1).text();

                Topic topic = new Topic(title, picUrl, link);
                topic.setTimeStr(timeStr);

                list.add(topic);
            }
        }
        topicList.setTopicList(list);
        return topicList;
    }
}
