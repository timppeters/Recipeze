package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TagService {

    @GET("tag")
    Call<JsonElement> getTags(@Query("type") String type,
                              @Header("x-access-token") String token);
}
