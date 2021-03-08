package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface TagService {

    @GET("tag")
    Call<JsonElement> getTags(@Field("type") String type);
}
