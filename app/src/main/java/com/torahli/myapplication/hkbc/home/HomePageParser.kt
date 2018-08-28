package com.torahli.myapplication.hkbc.home

import com.torahli.myapplication.framwork.Tlog
import com.torahli.myapplication.hkbc.bean.Topic
import com.torahli.myapplication.hkbc.home.bean.Banners
import com.torahli.myapplication.hkbc.home.bean.HomePage
import com.torahli.myapplication.hkbc.userinfo.UserInfoManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import org.jsoup.nodes.Document

object HomePageParser {

    /**
     * 解析主页
     */
    fun parseTopics(doc: Document): HomePage {
        //banner
        val lis = doc.select("div#framet010zR_left ul > li:has(a)")
        val topicList = lis.map {
            val topic = Topic(it.select("span").text(),
                    it.select("img").attr("src"),
                    it.select("a").attr("href"))
            topic
        }
        val homePage = HomePage().setBanners(Banners(topicList))
        //列表
        val topicsDiv = doc.select("div#portal_block_820:has(a) a")
        val topics = topicsDiv.map {
            Topic(it.text(), null, it.attr("href"))
        }
        homePage.setTopics(topics)
        parseLoginState(doc)
        return homePage
    }

    /**
     * 解析登陆信息
     */
    fun parseLoginState(doc: Document) {
        //登陆信息
        val loginCell = doc.select("div#hd > div > div.hdc.cl")
        //表明需要登陆
        val needLogin = loginCell.select("div.fastlg.cl")
        //已经登录成功
        val myCell = loginCell.select("strong.vwmy > a")
        val avater = loginCell.select("div.avt.y > a > img")
        val myDetail = loginCell.select("div#um > p")
        var jifenText: String = ""
        var grade: String = ""
        if (myDetail.size > 1) {
            val detailsContent = myDetail[1].select("a")
            if (detailsContent.size > 1) {
                jifenText = detailsContent[0].text()
                grade = detailsContent[1].text()
            }
        }

        Flowable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    when {
                        myCell.isNotEmpty() -> {
                            UserInfoManager.getInstance()
                                    .setUserinfo(myCell.text(), avater.attr("src"))
                                    .setUserDetail(jifenText, grade)
                                    .notifyUserInfoChanged()
                        }
                        needLogin.isNotEmpty() -> UserInfoManager.getInstance()
                                .setUserinfo(null, null)
                                .notifyUserInfoChanged()
                        else -> Tlog.w("torahlog", "$loginCell")
                    }
                })
    }

}