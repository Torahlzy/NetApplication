package com.torahli.myapplication.framwork.retrofit;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.torahli.myapplication.BuildConfig;

import java.util.concurrent.TimeUnit;

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
            builder.addNetworkInterceptor(new StethoInterceptor());//todo 查看效果
        }
        return builder.build();
    }

    public static OkHttpClient getGlideUsedClient(Context context) {
        return new OkHttpClient.Builder().build();
    }
}
