package com.torahli.myapplication.hkbc.net;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TopicContentProtocol {
    @GET("{url}")
    Flowable<String> getContent(@Path(value = "url", encoded = true) String url);
    //@Path("url") String url
}