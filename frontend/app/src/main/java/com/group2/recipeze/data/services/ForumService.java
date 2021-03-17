package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ForumService {

    @POST("forum")
    @FormUrlEncoded
    Call<JsonElement> createForumPost(@Field("title") String title,
                                      @Field("body") String body,
                                      @Field("tag") String tagName,
                                      @Header("x-access-token") String token);

    @GET("forum")
    Call<JsonElement> readForumPost(@Query("postId") int postId,
                                    @Header("x-access-token") String token);

    @GET("posts")
    Call<JsonElement> getForumPosts(@Query("tag") String tagName,
                                    @Header("x-access-token") String token);

    @PUT("user")
    @FormUrlEncoded
    Call<JsonElement> updateForumPost(@Field("postId") int postId,
                                      @Field("updates") JSONObject updates,
                                      @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "user", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> deleteForumPost(@Field("postId") int postId,
                                      @Header("x-access-token") String token);

    @POST("like")
    @FormUrlEncoded
    Call<JsonElement> likePost(@Field("postId") int postId,
                               @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "like", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> unlikePost(@Field("postId") int postId,
                                 @Header("x-access-token") String token);

    @POST("comment")
    @FormUrlEncoded
    Call<JsonElement> addCommentToPost(@Field("postId") int postId,
                                       @Field("body") String body,
                                       @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "comment", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> deleteCommentFromPost(@Field("commentId") int commentId,
                                            @Header("x-access-token") String token);
}
