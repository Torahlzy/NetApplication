package com.torahli.myapplication.framwork.retrofit;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.torahli.myapplication.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyHttpClient {
    /**
     * 同一进程共用同一个 CookieJar（内存缓存 + SharedPreferences 持久化）：
     * 页面请求（Retrofit）与图片请求（Glide）必须共享同一份 cookie，
     * 才能像网页一样：先访问页面拿到 Discuz 的 cookie，随后页面里的 &lt;img&gt;
     * 请求带上这些 cookie。
     */
    private static volatile ClearableCookieJar sCookieJar;

    private static ClearableCookieJar getCookieJar(Context context) {
        if (sCookieJar == null) {
            synchronized (MyHttpClient.class) {
                if (sCookieJar == null) {
                    sCookieJar = new PersistentCookieJar(new SetCookieCache(),
                            new SharedPrefsCookiePersistor(context.getApplicationContext()));
                }
            }
        }
        return sCookieJar;
    }

    /**
     * 获得带cookie持久化的httpclient（页面/接口请求用）
     *
     * @param context
     * @return
     */
    public static OkHttpClient getCookieSupportClint(Context context) {
        //https://github.com/franmontiel/PersistentCookieJar
        ClearableCookieJar cookieJar = getCookieJar(context);
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

    /**
     * 获得 Glide 加载图片用的 httpclient。
     * <p>
     * 图片请求必须模拟网页里 &lt;img&gt; 的真实请求，否则会被站点防盗链/风控拒绝
     * （现象：列表页图片全部加载失败，页面 HTML 却能正常打开）：
     * 1. 与页面请求共享同一 CookieJar，带上论坛 cookie；
     * 2. 使用移动端浏览器 UA / 图片 Accept / 同源 Referer 等请求头。
     * <p>
     * 参考：浏览器打开 http://site/forum-xxx.html 时，页面内 &lt;img src="data/attachment/..."&gt;
     * 会携带 UA、站点 Cookie 以及 Referer: http://site/forum-xxx.html 去请求图片；
     * 缺少 Referer 或 UA 不像浏览器时，站点会返回 404/403 防盗链页面导致加载失败。
     */
    public static OkHttpClient getGlideUsedClient(Context context) {
        // 图片请求并发控制：该站对大量并发图片请求不友好（曾出现一次发起 20+ 请求全部卡住）。
        // 限制“同时最多 3 个请求在途”，OkHttp 会排队等待——某个请求成功或失败后，
        // 自动从队列里调度下一个，避免一次性打太多。
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(3);
        dispatcher.setMaxRequestsPerHost(3);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                //该站图片响应本身偏慢（单张约 1~3s，移动网络可能更久），
                //超时放宽到与页面请求相当，避免慢请求被默认 10s 读超时掐断
                .writeTimeout((30 * 1000), TimeUnit.MILLISECONDS)
                .readTimeout((30 * 1000), TimeUnit.MILLISECONDS)
                .connectTimeout((15 * 1000), TimeUnit.MILLISECONDS)
                .addInterceptor(new BrowserImageInterceptor())
                .cookieJar(getCookieJar(context));
        if (BuildConfig.DEBUG) {
            // 调试：在 Stetho 里可以直接看到图片请求的完整请求/响应头
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder.build();
    }

    /**
     * 图片请求拦截器：给 Glide 的图片请求补上浏览器 <img> 的请求头。
     */
    private static class BrowserImageInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl url = original.url();
            // Referer 与图片同源（如 http://hkcdn4.space/），模拟“从本站页面加载本站图片”
            String referer = url.scheme() + "://" + url.host() + "/";
            Request request = original.newBuilder()
                    .header("User-Agent", MobileWebKitInterceptor.USER_AGENT)
                    .header("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8")
                    .header("Referer", referer)
                    .header("Connection", "keep-alive")
                    .build();
            return chain.proceed(request);
        }
    }
}
