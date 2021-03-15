package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SearchService {

    @GET("search")
    Call<JsonElement> search(@Field("query") String query,
                             @Header("x-access-token") String token);
}
