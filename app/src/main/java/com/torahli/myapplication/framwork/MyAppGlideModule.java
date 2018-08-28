package com.torahli.myapplication.framwork;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.torahli.myapplication.framwork.retrofit.MyHttpClient;

import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    private OkHttpClient glideUsedClient;

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        if (glideUsedClient == null) {
            glideUsedClient = MyHttpClient.getGlideUsedClient(context);
        }
        //Glide 底层默认使用 HttpConnection ，这里替换为自定义 Okhttp 后才能使用 Glide 的加载进度监听
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(glideUsedClient));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
