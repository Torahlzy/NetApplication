package com.torahli.myapplication.framwork.retrofit;

import com.torahli.myapplication.MainApplication;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 入门教程https://www.jianshu.com/p/308f3c54abdd
 */
public class MyRetrofit {

    private static final HashMap<String, WeakReference<Retrofit>> retrofitMap = new HashMap<>();

    /**
     * 封装一下retrofit。
     * cookie持久化
     * rxjava支持
     *
     * @param baseUrl
     */
    public static Retrofit create(String baseUrl) {
        WeakReference<Retrofit> wrRetrofit = retrofitMap.get(baseUrl);
        Retrofit retrofit = null;
        if (wrRetrofit != null) {
            retrofit = wrRetrofit.get();
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(MyHttpClient.getCookieSupportClint(MainApplication.getApplication()))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // 针对rxjava2.x
                    .build();
            retrofitMap.put(baseUrl, new WeakReference<Retrofit>(retrofit));
        }

        return retrofit;
    }
}
