package com.torahli.myapplication.framwork.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.torahli.myapplication.AppConfig;
import com.torahli.myapplication.MainApplication;
import com.torahli.myapplication.framwork.Tlog;

import java.io.File;

import javax.annotation.Nonnull;

public class SystemUtil {
    /**
     * 宽度填充时，高度应该多少
     * @param width
     * @param ratio
     * @return
     */
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

    /**
     * 获得sd卡的文件夹。
     *
     * @return 大概这样：/storage/emulated/0/{@link AppConfig.SDCARD_FOLDER}/
     */
    public static String getAPPSDCardPath() {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath() +
                    File.separator + AppConfig.SDCARD_FOLDER + File.separator;
        }
        return path;
    }

    /**
     * 获得当前app版本号
     *
     * @return
     */
    public static int getAppVersionCode() {
        MainApplication context = MainApplication.getApplication();
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Tlog.printException("torahlog", e);
        }
        return versioncode;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        MainApplication context = MainApplication.getApplication();
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Tlog.printException("torahlog", e);
        }
        return versionName;
    }
}
