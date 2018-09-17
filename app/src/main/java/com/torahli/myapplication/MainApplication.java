package com.torahli.myapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.facebook.stetho.Stetho;
import com.torahli.myapplication.framwork.Tlog;
import com.torahli.myapplication.framwork.umeng.UmengApp;

import org.jetbrains.annotations.NotNull;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

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
        FragmentationInit();
        initField();
    }

    /**
     * fragment导航
     */
    private void FragmentationInit() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                // 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 建议在该回调处上传至我们的Crash监测服务器
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                        Tlog.printException("torahlog", e);
                    }
                })
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
