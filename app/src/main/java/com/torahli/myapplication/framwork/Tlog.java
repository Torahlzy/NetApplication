package com.torahli.myapplication.framwork;

import android.util.Log;

import com.torahli.myapplication.AppConfig;

public class Tlog {
    public static boolean showLogCat = AppConfig.showLogcat;

    public static boolean isShowLogCat() {
        return showLogCat;
    }

    public static void d(String tag, String msg) {
        if (showLogCat) {
            Log.d(tag + AppConfig.TAG_APPEND, msg + "" + getCurLineForJump(4));
        }
    }

    public static void i(String tag, String msg) {
        if (showLogCat) {
            Log.i(tag + AppConfig.TAG_APPEND, msg + "" + getCurLineForJump(4));
        }
    }

    public static void w(String tag, String msg) {
        if (showLogCat) {
            Log.w(tag + AppConfig.TAG_APPEND, msg + "" + getCurLineForJump(4));
        }
    }

    public static void e(String tag, String msg) {
        Log.e(tag + AppConfig.TAG_APPEND, msg + "" + getCurLineForJump(4));
    }

    public static void printException(String tag, Throwable throwable) {
        Log.e(tag + AppConfig.TAG_APPEND, Log.getStackTraceString(throwable));
    }


    public static void printCallStack(String tag, String title) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null && 0 < stackTrace.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("==========").append(title).append("==========");
            for (int i = 0; i < stackTrace.length; i++) {
                if (i >= 3) {
                    sb.append(stackTrace[i]);
                }
            }
            i(tag, sb.toString());
        } else {
            e(tag, title + " getStackTrace fail");
        }

    }


    private static String getCurLineForJump(int precount) {
        if (!showLogCat) {
            return "";
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null && precount >= 0 && precount < stackTrace.length) {
            return "\n ==> at " + stackTrace[precount];
        }
        return "";
    }
}
