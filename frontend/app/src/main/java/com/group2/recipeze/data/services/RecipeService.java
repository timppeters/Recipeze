package com.group2.recipeze.data.services;


import com.google.gson.JsonElement;

import org.json.JSONObject;
import org.json.JSONStringer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("recipes")
    Call<JsonElement> getRecipes(@Query("for") String for_, @Query("username") String username, @Query("tagName") String tagName, @Query("filters") JSONObject filters, @Query("sortBy") String sortBy, @Query("skip") int skip);
}
