package com.group2.recipeze.data;


import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.data.services.TagService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagRepository extends Repository {
    private static volatile TagRepository instance;
    private TagService service;

    private TagRepository() {
        super();
        service = retrofit.create(TagService.class);
    }

    public static TagRepository getInstance() {
        if (instance == null) {
            instance = new TagRepository();
        }
        return instance;
    }


    /**
     * Get all tags as Tag objects.
     *
     * @param resultingTags The tags will be stored here.
     */
    public void getAllTags(MutableLiveData<ArrayList<Tag>> resultingTags) {
        Call<JsonElement> response = service.getTags("all", loggedInUser.getToken());
        response.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonObject tagsJson = response.body().getAsJsonObject();
                    ArrayList tagsArrayList = new ArrayList<Tag>();
                    for (Iterator<Map.Entry<String, JsonElement>> tagsIterator = tagsJson.entrySet().iterator(); tagsIterator.hasNext();) {
                        Map.Entry<String, JsonElement> entry = tagsIterator.next();
                        Tag tag = new Tag(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
                        tagsArrayList.add(tag);
                    }
                    resultingTags.postValue(tagsArrayList);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }


    /**
     * Get the top 5 tags. (For explore page)
     *
     * @param resultingTags The tags will be stored here.
     */
    public void getTop5Tags(MutableLiveData<ArrayList<Tag>> resultingTags) {
        Call<JsonElement> response = service.getTags("popular5", loggedInUser.getToken());
        response.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonObject tagsJson = response.body().getAsJsonObject();
                    ArrayList tagsArrayList = new ArrayList<Tag>();
                    for (Iterator<Map.Entry<String, JsonElement>> tagsIterator = tagsJson.entrySet().iterator(); tagsIterator.hasNext();) {
                        Map.Entry<String, JsonElement> entry = tagsIterator.next();
                        Tag tag = new Tag(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
                        tagsArrayList.add(tag);
                    }
                    resultingTags.postValue(tagsArrayList);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }


}
