package com.torahli.myapplication.hkbc.net;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface HomePageProtocol {
    @GET("forum.php")
    Flowable<String> getHomePage();
}