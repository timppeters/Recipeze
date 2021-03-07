package com.group2.recipeze.data.services;


import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("recipes")
    Call<JsonElement> getRecipes(@Query("for") String for_,
                                 @Query("username") String username,
                                 @Query("tagName") String tagName,
                                 @Query("filters") JSONObject filters,
                                 @Query("sortBy") String sortBy,
                                 @Query("skip") int skip);

    @POST("recipe")
    @FormUrlEncoded
    Call<JsonElement> createRecipe(@Field("username") String username,
                                   @Field("title") String title,
                                   @Field("description") String description,
                                   @Field("ingredients") JSONArray ingredients,
                                   @Field("ingredientsAmounts") JSONArray ingredientsAmounts,
                                   @Field("instructions") JSONObject instructions,
                                   @Field("images") JSONObject images,
                                   @Field("tags") JSONArray tags,
                                   @Field("prepTime") int prepTime,
                                   @Field("cookTime") int cookTime);


    @GET("recipe")
    Call<JsonElement> getRecipe(@Query("recipeId") int recipeId);

    @PUT("recipe")
    @FormUrlEncoded
    Call<JsonElement> updateRecipe(@Field("recipeId") int recipeId,
                                   @Field("updates") JSONObject updates);

    @DELETE("recipe")
    @FormUrlEncoded
    Call<JsonElement> deleteRecipe(@Field("recipeId") int recipeId);

    @POST("like")
    @FormUrlEncoded
    Call<JsonElement> likeRecipe(@Field("username") String username,
                                 @Field("recipeId") int recipeId);

    @DELETE("like")
    @FormUrlEncoded
    Call<JsonElement> unlikeRecipe(@Field("username") String username,
                                   @Field("recipeId") int recipeId);

    @POST("rate")
    @FormUrlEncoded
    Call<JsonElement> rateRecipe(@Field("username") String username,
                                 @Field("recipeId") int recipeId,
                                 @Field("rating") int rating,
                                 @Field("review") String review);

    @GET("rate")
    Call<JsonElement> getRatingsOfRecipe(@Field("recipeId") int recipeId);

    @DELETE("rate")
    @FormUrlEncoded
    Call<JsonElement> deleteRatingOfRecipe(@Field("username") String username,
                                           @Field("recipeId") int recipeId);
}
