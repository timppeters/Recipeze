package com.group2.recipeze.data;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.data.services.ForumService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumRepository extends Repository {
    private static volatile ForumRepository instance;
    private ForumService service;

    private ForumRepository() {
        super();
        service = retrofit.create(ForumService.class);
    }

    public static ForumRepository getInstance() {
        if (instance == null) {
            instance = new ForumRepository();
        }
        return instance;
    }


    /**
     * Create a forum post
     * @param title Title
     * @param body body
     * @param tagName What tag the forum is in
     * @param resultingPostId The resulting postId will be stored here
     */
    public void createForumPost(String title, String body, String tagName, MutableLiveData<Integer> resultingPostId) {
        Call<JsonElement> postId = service.createForumPost(title, body, tagName, loggedInUser.getToken());
        postId.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    Integer recipeId = Integer.parseInt(response.body().getAsJsonObject().get("postId").toString());
                    resultingPostId.postValue(recipeId);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Read a single forum post (gets comments as well)
     *
     * @param postId postId of the forum post
     * @param resultingPost THe resulting post will be stored here
     */
    public void readForumPost(int postId, MutableLiveData<ForumPost> resultingPost) {
        Call<JsonElement> postJson = service.readForumPost(postId, loggedInUser.getToken());
        postJson.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    ForumPost post = gson.fromJson(response.body().getAsJsonObject(), ForumPost.class);
                    resultingPost.postValue(post);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }


    /**
     * Get a list of posts in a forum of a specific tag
     *
     * @param tagName Name of the tag
     * @param resultingPosts The resulting posts will be stored here
     */
    public void getForumPostsInTag(String tagName, MutableLiveData<ArrayList<ForumPost>> resultingPosts) {
        Call<JsonElement> postJson = service.getForumPosts(tagName, loggedInUser.getToken());
        postJson.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonArray postsJsonArray = response.body().getAsJsonObject().get("posts").getAsJsonArray();
                    ArrayList postsArrayList = new ArrayList<ForumPost>();
                    for (Iterator<JsonElement> postIterator = postsJsonArray.iterator(); postIterator.hasNext();) {
                        ForumPost post = gson.fromJson(postIterator.next(), ForumPost.class);
                        postsArrayList.add(post);
                    }
                    resultingPosts.postValue(postsArrayList);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Update a forum post. Use the same method as creating a post to fill out the parameters. Do not just put null.
     *
     * @param postId post ID
     * @param title new title
     * @param body new body
     */
    public void updatePost(int postId, String title, String body) {
        JSONObject updates = new JSONObject();
        try {
            updates.put("title", title)
                    .put("body", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<JsonElement> result = service.updateForumPost(postId, updates, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Updated post successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Delete a forum post.
     *
     * @param postId post ID
     */
    public void deletePost(int postId) {
        Call<JsonElement> result = service.deleteForumPost(postId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Deleted post successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Like a forum post.
     *
     * @param postId post ID
     */
    public void likePost(int postId) {
        Call<JsonElement> result = service.likePost(postId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Liked post successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Unlike a forum post.
     *
     * @param postId post ID
     */
    public void unlikePost(int postId) {
        Call<JsonElement> result = service.unlikePost(postId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Unliked post successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Add a comment to a post.
     *
     * @param postId post ID
     * @param body Comment text
     */
    public void addCommentToPost(int postId, String body) {
        Call<JsonElement> result = service.addCommentToPost(postId, body, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Added comment successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Delete a comment from a forum post
     *
     * @param commentId Comment ID
     */
    public void deleteCommentFromPost(int commentId) {
        Call<JsonElement> result = service.deleteCommentFromPost(commentId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Deleted comment successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }


}
