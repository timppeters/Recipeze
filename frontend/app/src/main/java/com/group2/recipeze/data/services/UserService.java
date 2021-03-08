package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {

    @GET("user")
    Call<JsonElement> getPrivateProfile(@Query("username") String username);

    @GET("publicUser")
    Call<JsonElement> getPublicProfile(@Query("username") String username);

    @PUT("user")
    @FormUrlEncoded
    Call<JsonElement> updateProfile(@Field("username") String username,
                                    @Field("updates") JSONObject updates);

    @DELETE("user")
    @FormUrlEncoded
    Call<JsonElement> deleteAccount(@Field("username") String username);

    @POST("follow")
    @FormUrlEncoded
    Call<JsonElement> followUser(@Field("username") String username, @Field("followUser") String followUser);

    @DELETE("follow")
    @FormUrlEncoded
    Call<JsonElement> unfollowUser(@Field("username") String username, @Field("unfollowUser") String unfollowUser);


}
