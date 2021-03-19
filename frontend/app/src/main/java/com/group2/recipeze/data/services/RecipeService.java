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
import retrofit2.http.HTTP;
import retrofit2.http.Header;
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
                                 @Query("skip") int skip,
                                 @Header("x-access-token") String token);

    @POST("recipe")
    @FormUrlEncoded
    Call<JsonElement> createRecipe(@Field("title") String title,
                                   @Field("description") String description,
                                   @Field("ingredients") JSONArray ingredients,
                                   @Field("ingredientsAmounts") JSONArray ingredientsAmounts,
                                   @Field("instructions") JSONObject instructions,
                                   @Field("images") JSONObject images,
                                   @Field("tags") JSONArray tags,
                                   @Field("prepTime") int prepTime,
                                   @Field("cookTime") int cookTime,
                                   @Header("x-access-token") String token);


    @GET("recipe")
    Call<JsonElement> getRecipe(@Query("recipeId") int recipeId,
                                @Header("x-access-token") String token);

    @PUT("recipe")
    @FormUrlEncoded
    Call<JsonElement> updateRecipe(@Field("recipeId") int recipeId,
                                   @Field("updates") JSONObject updates,
                                   @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "recipe", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> deleteRecipe(@Field("recipeId") int recipeId,
                                   @Header("x-access-token") String token);

    @POST("like")
    @FormUrlEncoded
    Call<JsonElement> likeRecipe(@Field("recipeId") int recipeId,
                                 @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "like", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> unlikeRecipe(@Field("recipeId") int recipeId,
                                   @Header("x-access-token") String token);

    @POST("rate")
    @FormUrlEncoded
    Call<JsonElement> rateRecipe(@Field("recipeId") int recipeId,
                                 @Field("rating") int rating,
                                 @Field("review") String review,
                                 @Header("x-access-token") String token);

    @GET("rate")
    Call<JsonElement> getRatingsOfRecipe(@Field("recipeId") int recipeId,
                                         @Header("x-access-token") String token);

    @HTTP(method = "DELETE", path = "rate", hasBody = true)
    @FormUrlEncoded
    Call<JsonElement> deleteRatingOfRecipe(@Field("recipeId") int recipeId,
                                           @Header("x-access-token") String token);
}
