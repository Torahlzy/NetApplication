package com.torahli.myapplication.framwork.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 模拟手机端
 */
class MobileWebKitInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; SKR-A0 Build/G66X1808091CN00MPX) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.146 Mobile Safari/537.36 XiaoMi/MiuiBrowser/9.4.12")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept-Encoding", "deflate")
                .addHeader("Accept-Language", "zh-CN,en-US;q=0.8")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .build();
        return chain.proceed(request);
    }
}
