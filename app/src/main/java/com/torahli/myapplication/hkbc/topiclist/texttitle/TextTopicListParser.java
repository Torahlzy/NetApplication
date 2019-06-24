package com.torahli.myapplication.hkbc.topiclist.texttitle;

import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.hkbc.databean.Topic;
import com.torahli.myapplication.hkbc.topiclist.bean.TopicList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TextTopicListParser {
    public static TopicList parser(String s) {
        Document doc = Jsoup.parse(s);
        Elements tbodys = doc.select("div.bm_c table > tbody:has(td.icn)");
        TopicList topicList = new TopicList();
        List<Topic> list = new ArrayList<>();
        if (tbodys != null && tbodys.size() > 0) {
            for (int i = 0; i < tbodys.size(); i++) {
                Element cell = tbodys.get(i);
                Elements titleEs = cell.select("th.common,th.new > a");
                if (titleEs != null && titleEs.size() > 0) {
                    Element titleE = (Element) titleEs.get(0);
                    String title = titleE.text();
                    String link = titleE.attr("href");
                    String timeStr = cell.select("td.by > em > span").get(0).text();
                    String author = cell.select("td.by > cite > a").get(0).text();

                    Topic topic = new Topic(title, null, link);
                    topic.setTimeStr(timeStr);
                    topic.setAuthor(author);
                    list.add(topic);
                } else {
                    Tlog.d("torahlog", "--跳过条目\n:" + cell);
                }
            }
        }
        topicList.setTopicList(list);
        //解析有几页数据
        Elements pages = doc.select("span#fd_page_top > div.pg");
        String currentPage = pages.select("> strong").text();
        Elements pagesLink = pages.select("> a");
        LinkedHashMap<String, String> pageLinks = new LinkedHashMap<>();
        for (int i = 0; i < pagesLink.size(); i++) {
            Element page = pagesLink.get(i);
            String pageClass = page.attr("class");
            if ("prev".equals(pageClass) || "nxt".equals(pageClass)) {
                continue;
            }
            String name = page.text();
            pageLinks.put(name, page.attr("href"));
        }
        topicList.setPageName(currentPage);
        topicList.setOtherPages(pageLinks);

        return topicList;
    }
}
