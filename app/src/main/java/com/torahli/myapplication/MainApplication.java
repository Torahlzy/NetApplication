package com.torahli.myapplication;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;

import me.yokeyword.fragmentation.Fragmentation;

public class MainApplication extends Application {
    @NotNull
    public static MainApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //https://www.jianshu.com/p/6bc14895af96
        Stetho.initializeWithDefaults(this);
        fragmentationInit();
    }

    /**
     * fragment导航
     */
    private void fragmentationInit() {
        Fragmentation.builder()
                .stackViewMode(AppConfig.debug ? Fragmentation.BUBBLE : Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();
    }

    @NotNull
    public static MainApplication getApplication() {
        return application;
    }
}
