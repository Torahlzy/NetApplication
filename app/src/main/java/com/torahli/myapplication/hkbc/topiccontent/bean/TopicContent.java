package com.torahli.myapplication.hkbc.topiccontent.bean;

import com.torahli.myapplication.framwork.bean.LiveDataBase;
import com.torahli.myapplication.framwork.bean.NetErrorType;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * 主题详情页的解析结果
 */
public class TopicContent extends LiveDataBase {
    @Nonnull
    List<String> imgList = new ArrayList<>();

    public TopicContent setError(int netError, String msg) {
        setErrorAndMsg(netError, msg);
        imgList.clear();
        return this;
    }

    public TopicContent setImgList(List<String> imgs) {
        if (imgs == null || imgs.size() == 0) {
            setErrorAndMsg(NetErrorType.NoDataError, "解析详情出错");
        } else {
            super.setNoError();
            imgList.clear();
            imgList.addAll(imgs);
        }
        return this;
    }

    @Nonnull
    public List<String> getImgList() {
        return imgList;
    }

    @Override
    public String toString() {
        return "TopicContent{" +
                "imgList=" + imgList.size() +
                ", error=" + error +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
