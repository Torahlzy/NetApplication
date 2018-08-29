package com.torahli.myapplication.app.update;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.torahli.myapplication.app.update.bean.UpdateInfo;

public class UpdateInfoParser {

    public static UpdateInfo parser(String str){
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Gson gson = new Gson();
        UpdateInfo updateInfo = gson.fromJson(str, UpdateInfo.class);
        return updateInfo;
    }
}
