package com.torahli.myapplication;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.torahli.myapplication.framwork.umeng.UmengApp;

import org.jetbrains.annotations.NotNull;

public class MainApplication extends Application {
    @NotNull
    public static MainApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //https://www.jianshu.com/p/6bc14895af96
        Stetho.initializeWithDefaults(this);
        //友盟
        UmengApp.init(this);
    }

    @NotNull
    public static MainApplication getApplication() {
        return application;
    }
}
