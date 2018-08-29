package com.torahli.myapplication.framwork.util;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;

public class ProtocolUtil {
    /**
     * 获得一个网址的域名
     *
     * @param url
     * @return
     */
    @Nonnull
    public static String getHost(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        URL url1 = null;
        try {
            url1 = new URL(url);
            return url1.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param url
     * @return like "https://www.github.com/"
     */
    public static String getHostWithProtocol(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        URL url1 = null;
        try {
            url1 = new URL(url);

            String host = url1.getHost();
            String protocol = url1.getProtocol();
            return protocol + "://" + host + "/";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
