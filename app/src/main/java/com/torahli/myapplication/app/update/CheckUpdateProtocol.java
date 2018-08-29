package com.torahli.myapplication.app.update;

import com.torahli.myapplication.AppConfig;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface CheckUpdateProtocol {
    @GET(AppConfig.CHECK_PDATE_JSON_URL)
    Flowable<String> checkUpdate();
}
