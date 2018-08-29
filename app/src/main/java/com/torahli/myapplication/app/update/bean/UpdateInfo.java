package com.torahli.myapplication.app.update.bean;

import com.torahli.myapplication.framwork.bean.BaseLiveData;
import com.torahli.myapplication.framwork.bean.DeProguard;

public class UpdateInfo extends BaseLiveData implements DeProguard {
    public UpdateInfo setError(int netError, String msg) {
        setErrorAndMsg(netError, msg);
        return this;
    }

    private Update update;

    public boolean isAvailable() {
        return update != null && update.versionCode > 0;
    }

    public static class Update implements DeProguard {
        int versionCode;
        String versionName;
        String url;
        String desc;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "update=" + update +
                ", error=" + error +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
