package com.torahli.myapplication.framwork.retrofit;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.torahli.myapplication.BuildConfig;

import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class MyHttpClient {
    /**
     * 获得带cookie持久化的httpclient
     *
     * @param context
     * @return
     */
    public static OkHttpClient getCookieSupportClint(Context context) {
        //https://github.com/franmontiel/PersistentCookieJar
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        Interceptor interceptor = new MobileWebKitInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .writeTimeout((30 * 1000), TimeUnit.MILLISECONDS)
                .readTimeout((20 * 1000), TimeUnit.MILLISECONDS)
                .connectTimeout((15 * 1000), TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .cookieJar(cookieJar);
        if (BuildConfig.DEBUG) {
//            builder.addNetworkInterceptor(new StethoInterceptor());//todo 查看效果
//            .addInterceptor(LoggerInterceptor("OK_HTTP"))
        }
        // https://github.com/JessYanCoding/ProgressManager/blob/master/README-zh.md 进度监听功能
        // 构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置
        OkHttpClient client = ProgressManager.getInstance().with(builder).build();
        return client;
    }

    public static OkHttpClient getGlideUsedClient(Context context) {
        return ProgressManager.getInstance().with(new OkHttpClient.Builder())
                .build();
    }
}
