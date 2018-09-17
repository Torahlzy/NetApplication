package com.torahli.myapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.facebook.stetho.Stetho;
import com.torahli.myapplication.framwork.umeng.UmengApp;

import org.jetbrains.annotations.NotNull;

import me.yokeyword.fragmentation.Fragmentation;

public class MainApplication extends Application {
    @NotNull
    public static MainApplication application;
    private RxSharedPreferences rxPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //https://www.jianshu.com/p/6bc14895af96
        Stetho.initializeWithDefaults(this);
        //友盟
        UmengApp.init(this);
        fragmentationInit();
        initField();
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

    private void initField() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getApplication());
        rxPreferences = RxSharedPreferences.create(preferences);
    }

    public RxSharedPreferences getRxPreferences() {
        return rxPreferences;
    }

    @NotNull
    public static MainApplication getApplication() {
        return application;
    }
}
