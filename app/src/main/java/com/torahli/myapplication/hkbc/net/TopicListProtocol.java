package com.torahli.myapplication.hkbc.net;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TopicListProtocol {
    @GET("{url}")
    Flowable<String> getTopicList(@Path(value = "url", encoded = true) String url);
    //@Path("url") String url
}