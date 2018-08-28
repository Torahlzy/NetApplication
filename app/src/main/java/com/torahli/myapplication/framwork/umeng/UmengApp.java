package com.torahli.myapplication.framwork.umeng;

import android.app.Application;

import com.torahli.myapplication.AppConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class UmengApp {
    public static void init(Application mainApplication) {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(mainApplication,
                AppConfig.UMENG_APP_KEY, AppConfig.UMENG_CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE, AppConfig.UMENG_PUSH_SECRET);
        //关闭activity的自动统计
        MobclickAgent.openActivityDurationTrack(false);
        if (AppConfig.debug) {
            UMConfigure.setLogEnabled(true);
        }
    }
}
