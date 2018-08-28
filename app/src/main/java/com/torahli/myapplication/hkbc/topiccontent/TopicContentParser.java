package com.torahli.myapplication.hkbc.topiccontent;

import com.torahli.myapplication.hkbc.topiccontent.bean.TopicContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TopicContentParser {

    public static TopicContent parser(String str) {
        TopicContent content = new TopicContent();

        List<String> imgList = new ArrayList<>();
        Document doc = Jsoup.parse(str);
        Elements floors = doc.select("div#postlist");
        if (floors.size() > 0) {
            for (int i = 0; i < floors.size(); i++) {
                if (i == 0) {
                    imgList.addAll(parserFloor(floors.get(i)));
                } else {
                    //其他楼层暂时不解析
                    break;
                }
            }
        }

        content.setImgList(imgList);
        return content;
    }

    private static List<String> parserFloor(Element floor) {
        List<String> imgList = new ArrayList<>();
        Elements imgs = floor.select("tbody > tr > td.plc > div.pct img[file]");
        if (imgs.size() > 0) {
            for (Element img : imgs) {
                imgList.add(img.attr("file"));
            }
        }
        return imgList;
    }
}
