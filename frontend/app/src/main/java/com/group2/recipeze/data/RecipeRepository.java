package com.group2.recipeze.data;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.Rating;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.data.services.RecipeService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
     *
     * @param maxTime Maximum time the recipe should take to prepare+cook
     * @param ingredients List of ingredients in lower case that should be in the recipe
     * @param maxNumberOfIngredients Max number of ingredients
     * @param tags List of tags that the recipe should have
     * @param sortBy likes or newest
     * @param skip Number of items to skip (for use with infiniteScroll,
     *             just pass current length of array)
     * @param result MutableLiveData object where results will be placed
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

        System.out.println("Filters " + filters.toString());

        getRecipes("feedUser", loggedInUser.getUsername(), "", filters, sortBy, skip, result);
    }

    /**
     * Get recipes for the feed based on what tags the user follows
     *
     * @param maxTime Maximum time the recipe should take to prepare+cook
     * @param ingredients List of ingredients in lower case that should be in the recipe
     * @param maxNumberOfIngredients Max number of ingredients
     * @param tags List of tags that the recipe should have
     * @param sortBy likes or newest
     * @param skip Number of items to skip (for use with infiniteScroll,
     *            just pass current length of array)
     * @param result MutableLiveData object where results will be placed
     */
    public void getRecipesForFeedByTags(int maxTime, ArrayList<String> ingredients, int maxNumberOfIngredients, ArrayList<String> tags, String sortBy, int skip, MutableLiveData<ArrayList<Recipe>> result) {
        JSONObject filters = new JSONObject();
        try {
            filters.put("maxTime", maxTime);
            filters.put("ingredients", new JSONArray(ingredients).toString());
            filters.put("maxNumberOfIngredients", maxNumberOfIngredients);
            filters.put("tags", new JSONArray(tags).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getRecipes("feedTag", loggedInUser.getUsername(), "", filters, sortBy, skip, result);
    }

    /**
     * Get recipes for the explore page (most popular recipes)
     *
     * @param skip Number of items to skip (for use with infiniteScroll,
     *             just pass current length of array)
     * @param result MutableLiveData object where results will be placed
     */
    public void getRecipesForExplore(int skip, MutableLiveData<ArrayList<Recipe>> result) {
        getRecipes("explore", "", "", new JSONObject(), "", skip, result);
    }

    /**
     * Get recipes for the user's recipe book
     *
     * @param maxTime Maximum time the recipe should take to prepare+cook
     * @param ingredients List of ingredients in lower case that should be in the recipe
     * @param maxNumberOfIngredients Max number of ingredients
     * @param tags List of tags that the recipe should have
     * @param sortBy likes or newest
     * @param skip Number of items to skip (for use with infiniteScroll,
     *            just pass current length of array)
     * @param result MutableLiveData object where results will be placed
     */
    public void getRecipesForRecipeBook(int maxTime, ArrayList<String> ingredients, int maxNumberOfIngredients, ArrayList<String> tags, String sortBy, int skip, MutableLiveData<ArrayList<Recipe>> result) {
        JSONObject filters = new JSONObject();
        try {
            filters.put("maxTime", maxTime);
            filters.put("ingredients", new JSONArray(ingredients).toString());
            filters.put("maxNumberOfIngredients", maxNumberOfIngredients);
            filters.put("tags", new JSONArray(tags).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getRecipes("recipe book", loggedInUser.getUsername(), "", filters, sortBy, skip, result);
    }

    /**
     * Get recipes for a user's profile
     *
     * @param username The user's username
     * @param skip Number of items to skip (for use with infiniteScroll,
     *             just pass current length of array)
     * @param result MutableLiveData object where results will be placed
     */
    public void getRecipesForProfile(String username, int skip, MutableLiveData<ArrayList<Recipe>> result) {
        getRecipes("profile", username, "", new JSONObject(), "", skip, result);
    }

    /**
     * Get recipes for a tag
     *
     * @param tagName The tag
     * @param skip Number of items to skip (for use with infiniteScroll,
     *             just pass current length of array)
     * @param result MutableLiveData object where results will be placed
     */
    public void getRecipesForTag(String tagName, int skip, MutableLiveData<ArrayList<Recipe>> result) {
        getRecipes("tag", "", tagName, new JSONObject(), "", skip, result);
    }

    /**
     * Helper function. Gets recipes asynchronously for /api/recipes
     */
    private void getRecipes(String for_, String username, String tagName, JSONObject filters, String sortBy, int skip, MutableLiveData<ArrayList<Recipe>> result) {
        Call<JsonElement> recipes = service.getRecipes(for_, username, tagName, filters, sortBy, skip, loggedInUser.getToken());
        recipes.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonArray recipesJsonArray = response.body().getAsJsonObject().get("recipes").getAsJsonArray();
                    ArrayList recipesArrayList = new ArrayList<Recipe>();
                    for (Iterator<JsonElement> recipeIterator = recipesJsonArray.iterator(); recipeIterator.hasNext();) {
                        JsonElement next = recipeIterator.next();
                        Recipe recipe = gson.fromJson(next, Recipe.class);
                        recipe.parseNutrients();
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


    /**
     * Create a recipe and get the resulting recipeId to call getRecipe() with
     *
     * @param title Recipe title
     * @param description Recipe description
     * @param ingredients_ List of ingredients e.g. ["tomatoes"]
     * @param ingredientsAmounts_ List of ingredients amounts (corresponding to the index of ingredients) e.g. ["500g"]
     * @param instructions_ Instructions
     * @param images_ Images for instructions (corresponding to the keys)
     * @param tags_ List of tags the recipe should have
     * @param prepTime How long it takes to prepare the recipe
     * @param cookTime How long it takes to cook the recipe
     * @param resultingRecipeId The recipeId of the created recipe will be stored here
     */
    public void createRecipe(String title, String description, ArrayList<String> ingredients_, ArrayList<String> ingredientsAmounts_, HashMap<String, String> instructions_, HashMap<String, String> images_, ArrayList<String> tags_, int prepTime, int cookTime , MutableLiveData<Integer> resultingRecipeId, ArrayList<String> incorrectTags) {
        JSONArray ingredients = new JSONArray(ingredients_);
        JSONArray ingredientsAmounts = new JSONArray(ingredientsAmounts_);
        JSONObject instructions = new JSONObject(instructions_);
        JSONObject images = new JSONObject(images_);
        JSONArray tags = new JSONArray(tags_);


        Call<JsonElement> recipeId = service.createRecipe(title, description, ingredients, ingredientsAmounts, instructions, images, tags, prepTime, cookTime, loggedInUser.getToken());
        recipeId.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonObject jsonResponse = response.body().getAsJsonObject();
                    if (jsonResponse.has("tagError")) {
                        ArrayList<String> tags = gson.fromJson(jsonResponse.get("tagError").getAsJsonArray(), ArrayList.class);
                        incorrectTags.addAll(tags);
                        resultingRecipeId.postValue(-1);
                    } else {
                        Integer recipeId = Integer.parseInt(jsonResponse.get("recipeId").toString());
                        resultingRecipeId.postValue(recipeId);
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }


    /**
     * Get a recipe
     *
     * @param recipeId ID of the recipe
     * @param resultingRecipe The resulting recipe will be stored here
     */
    public void getRecipe(Integer recipeId, MutableLiveData<Recipe> resultingRecipe) {
        Call<JsonElement> recipeJSON = service.getRecipe(recipeId, loggedInUser.getToken());
        recipeJSON.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    Recipe recipe = gson.fromJson(response.body().getAsJsonObject(), Recipe.class);
                    resultingRecipe.postValue(recipe);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Update a recipe. Use the same method as creating a recipe to fill out the parameters. Do not just put null.
     *
     * @param recipeId ID of the recipe you want to update
     * @param title
     * @param description
     * @param ingredients_
     * @param ingredientsAmounts_
     * @param instructions_
     * @param images_
     * @param tags_
     * @param prepTime
     * @param cookTime
     */
    public void updateRecipe(Integer recipeId, String title, String description, ArrayList<String> ingredients_, ArrayList<String> ingredientsAmounts_, HashMap<Integer, String> instructions_, HashMap<Integer, String> images_, ArrayList<Tag> tags_, int prepTime, int cookTime) {
        JSONArray ingredients = new JSONArray(ingredients_);
        JSONArray ingredientsAmounts = new JSONArray(ingredientsAmounts_);
        JSONObject instructions = new JSONObject(instructions_);
        JSONObject images = new JSONObject(images_);
        JSONArray tags = new JSONArray(tags_);

        JSONObject updates = new JSONObject();
        try {
            updates.put("title", title)
                    .put("description", description)
                    .put("ingredients", ingredients)
                    .put("ingredientsAmounts", ingredientsAmounts)
                    .put("instructions", instructions)
                    .put("images", images)
                    .put("tags", tags)
                    .put("prepTime", prepTime)
                    .put("cookTime", cookTime);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Call<JsonElement> recipeJSON = service.updateRecipe(recipeId, updates, loggedInUser.getToken());
        recipeJSON.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Updated Recipe Successfully");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Delete a recipe
     *
     * @param recipeId ID of the recipe
     */
    public void deleteRecipe(Integer recipeId) {
        Call<JsonElement> result = service.deleteRecipe(recipeId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Recipe delete successfully");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Like a recipe
     *
     * @param recipeId ID of the recipe
     */
    public void likeRecipe(Integer recipeId) {
        Call<JsonElement> result = service.likeRecipe(recipeId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Recipe liked successfully");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Unlike a recipe
     *
     * @param recipeId ID of the recipe
     */
    public void unlikeRecipe(Integer recipeId) {
        Call<JsonElement> result = service.unlikeRecipe(recipeId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Recipe unliked successfully");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Rate a recipe
     *
     * @param recipeId ID of the recipe
     */
    public void rateRecipe(Integer recipeId, int rating, String review) {
        Call<JsonElement> result = service.rateRecipe(recipeId, rating, review, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Recipe rated successfully");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Delete rating of a recipe
     *
     * @param recipeId ID of the recipe that the rating is on (only 1 rating per recipe per user)
     */
    public void deleteRatingOfRecipe(Integer recipeId) {
        Call<JsonElement> result = service.deleteRatingOfRecipe(recipeId, loggedInUser.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Recipe rating deleted successfully");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Get ratings of a recipe (text reviews)
     *
     * @param recipeId ID of the recipe
     * @param ratings The reviews will be stored here
     */
    public void getRatingsOfRecipe(Integer recipeId, MutableLiveData<ArrayList<Rating>> ratings) {
        Call<JsonElement> ratingsJSON = service.getRatingsOfRecipe(recipeId, loggedInUser.getToken());
        ratingsJSON.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    JsonArray ratingsJsonArray = response.body().getAsJsonObject().get("ratings").getAsJsonArray();
                    ArrayList ratingsArrayList = new ArrayList<Rating>();
                    for (Iterator<JsonElement> ratingIterator = ratingsJsonArray.iterator(); ratingIterator.hasNext();) {
                        Rating rating = gson.fromJson(ratingIterator.next(), Rating.class);
                        ratingsArrayList.add(rating);
                    }
                    ratings.postValue(ratingsArrayList);
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

}
