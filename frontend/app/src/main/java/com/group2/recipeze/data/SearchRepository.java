package com.group2.recipeze.data;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.data.model.User;
import com.group2.recipeze.data.services.SearchService;
import com.group2.recipeze.ui.search.SearchFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository extends Repository {

    private static volatile SearchRepository instance;
    private SearchService service;

    private SearchRepository() {
        super();
        service = retrofit.create(SearchService.class);
    }

    public static SearchRepository getInstance() {
        if (instance == null) {
            instance = new SearchRepository();
        }
        return instance;
    }

    public void search(String query, MutableLiveData<Map<String, ArrayList>> results) {
        Call<JsonElement> response = service.search(query, loggedInUser.getToken());
        response.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonObject searchResults = response.body().getAsJsonObject();

                    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
                    for (Iterator<JsonElement> recipesIterator = searchResults.get("recipes").getAsJsonArray().iterator(); recipesIterator.hasNext();) {
                        Recipe recipe = gson.fromJson(recipesIterator.next(), Recipe.class);
                        recipeArrayList.add(recipe);
                    }

                    ArrayList<User> usersArrayList = new ArrayList<>();
                    for (Iterator<JsonElement> usersIterator = searchResults.get("users").getAsJsonArray().iterator(); usersIterator.hasNext();) {
                        User user = gson.fromJson(usersIterator.next(), User.class);
                        usersArrayList.add(user);
                    }

                    ArrayList<Tag> tagsArrayList = new ArrayList<>();
                    for (Iterator<JsonElement> tagsIterator = searchResults.get("tags").getAsJsonArray().iterator(); tagsIterator.hasNext();) {
                        Tag tag = gson.fromJson(tagsIterator.next(), Tag.class);
                        tagsArrayList.add(tag);
                    }

                    // Map of results
                    Map<String, ArrayList> types = new HashMap<String, ArrayList>();
                    try {
                        types = Map.of(
                                "recipes", recipeArrayList,
                                "users", usersArrayList,
                                "tags", tagsArrayList);
                        System.out.println("TYPES: "+ types);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    results.postValue(types);
                    System.out.println("RESULTS: " +results);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }
}
