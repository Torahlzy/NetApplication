package com.torahli.myapplication.framwork.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.torahli.myapplication.MainApplication;

import javax.annotation.Nonnull;

public class SystemUtil {
    public static int getWrapHeightForMatchWidth(int width, Float ratio) {
        return (int) (width * ratio);
    }

    public static int getScreenWidth(@Nonnull Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        Context context = MainApplication.getApplication();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        Context context = MainApplication.getApplication();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
