package com.torahli.myapplication.app.net;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface DownloadProtocol {
    @Streaming
    @GET("{url}")
    Flowable<ResponseBody> startDownload(@Path(value = "url", encoded = true) String url);
}
