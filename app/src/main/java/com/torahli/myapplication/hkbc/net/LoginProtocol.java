package com.torahli.myapplication.hkbc.net;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginProtocol {
    @FormUrlEncoded
    @POST("/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1")
    Flowable<String> login( @FieldMap Map<String, String> map);
}