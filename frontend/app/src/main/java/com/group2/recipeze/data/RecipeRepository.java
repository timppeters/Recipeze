package com.group2.recipeze.data;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.services.RecipeService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository extends Repository {
    private static volatile RecipeRepository instance;
    private RecipeService service;

    private RecipeRepository() {
        super();
        service = retrofit.create(RecipeService.class);
    }

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    /**
     * Get recipes for the feed based on who the user follows
     * @param maxTime Maximum time the recipe should take to prepare+cook
     * @param ingredients List of ingredients in lower case that should be in the recipe
     * @param maxNumberOfIngredients Max number of ingredients
     * @param tags List of tags that the recipe should have
     * @param sortBy likes or newest
     */
    public void getRecipesForFeedByUsers(int maxTime, ArrayList<String> ingredients, int maxNumberOfIngredients, ArrayList<String> tags, String sortBy, int skip, MutableLiveData<ArrayList<Recipe>> result) {
        JSONObject filters = new JSONObject();
        try {
            filters.put("maxTime", maxTime);
            filters.put("ingredients", new JSONArray(ingredients).toString());
            filters.put("maxNumberOfIngredients", maxNumberOfIngredients);
            filters.put("tags", new JSONArray(tags).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<JsonElement> recipes = service.getRecipes("feedUser", this.username, "", filters, sortBy, skip);
        recipes.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonArray recipesJsonArray = response.body().getAsJsonObject().get("recipes").getAsJsonArray();
                    ArrayList recipesArrayList = new ArrayList<Recipe>();
                    for (Iterator<JsonElement> recipeIterator = recipesJsonArray.iterator(); recipeIterator.hasNext();) {
                        Recipe recipe = gson.fromJson(recipeIterator.next(), Recipe.class); // NEED CUSTOM METHOD FOR DESERIALISATION
                        recipesArrayList.add(recipe);
                    }
                    result.postValue(recipesArrayList);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });


    }

}
