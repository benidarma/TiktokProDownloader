package com.ameldev.tiktokprodownloader.api;

import com.ameldev.tiktokprodownloader.models.Tiktok;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIServices {

    @FormUrlEncoded
    @POST
    Observable<Tiktok> callTiktokApi(@Url String Url, @Field("url") String url);
}
