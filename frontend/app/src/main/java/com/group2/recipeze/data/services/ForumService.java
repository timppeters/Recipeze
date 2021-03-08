package com.group2.recipeze.data.services;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ForumService {

    @POST("forum")
    @FormUrlEncoded
    Call<JsonElement> createForumPost(@Field("username") String username,
                                      @Field("title") String title,
                                      @Field("body") String body,
                                      @Field("tag") String tagName);

    @GET("forum")
    Call<JsonElement> readForumPost(@Query("postId") int postId);

    @GET("posts")
    Call<JsonElement> getForumPosts(@Query("tag") String tagName);

    @PUT("user")
    @FormUrlEncoded
    Call<JsonElement> updateForumPost(@Field("postId") int postId,
                                    @Field("updates") JSONObject updates);

    @DELETE("user")
    @FormUrlEncoded
    Call<JsonElement> deleteForumPost(@Field("postId") int postId);

    @POST("like")
    @FormUrlEncoded
    Call<JsonElement> likePost(@Field("username") String username,
                                 @Field("postId") int postId);

    @DELETE("like")
    @FormUrlEncoded
    Call<JsonElement> unlikePost(@Field("username") String username,
                                   @Field("postId") int postId);

    @POST("comment")
    @FormUrlEncoded
    Call<JsonElement> addCommentToPost(@Field("username") String username,
                                       @Field("postId") int postId,
                                       @Field("body") String body);

    @DELETE("comment")
    @FormUrlEncoded
    Call<JsonElement> deleteCommentFromPost(@Field("commentId") int commentId);
}
