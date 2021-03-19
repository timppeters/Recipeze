package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {

    @GET("user")
    Call<JsonElement> getPrivateProfile(@Header("x-access-token") String token);

    @GET("publicUser")
    Call<JsonElement> getPublicProfile(@Query("username") String username,
                                       @Header("x-access-token") String token);

    @PUT("user")
    @FormUrlEncoded
    Call<JsonElement> updateProfile(@Field("updates") JSONObject updates,
                                    @Header("x-access-token") String token);

    @DELETE("user")
    @FormUrlEncoded
    Call<JsonElement> deleteAccount(@Header("x-access-token") String token);

    @POST("follow")
    @FormUrlEncoded
    Call<JsonElement> followUser(@Field("followUser") String followUser,
                                 @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "follow", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> unfollowUser(@Field("unfollowUser") String unfollowUser,
                                   @Header("x-access-token") String token);

    @POST("tokensignin")
    @FormUrlEncoded
    Call<JsonElement> tokenSignIn(@Field("token") String token,
                                  @Field("type") String type);

    @POST("setUsername")
    @FormUrlEncoded
    Call<JsonElement> setUsername(@Field("username") String username,
                                  @Header("x-access-token") String token);

    @POST("requesttokenemail")
    @FormUrlEncoded
    Call<JsonElement> requestTokenEmail(@Field("email") String email);

}
