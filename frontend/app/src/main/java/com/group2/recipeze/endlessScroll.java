package com.group2.recipeze;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.RecipeRepository;

import com.group2.recipeze.data.UserRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.User;


import com.group2.recipeze.data.model.Comment;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.data.model.LoggedInUser;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.ui.forum.ForumPostAdapter;

import java.util.ArrayList;
import java.util.HashMap;
public class endlessScroll {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    UserViewAdapter UserViewAdapter;
    RecipeRepository recipeRepository;
    UserRepository UserRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    public MutableLiveData<ArrayList<User>> users = new MutableLiveData<>();
    ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    ArrayList<User> userList = new ArrayList<>();
    public MutableLiveData<ArrayList<Comment>> comment = new MutableLiveData<>();
    String feedType = new String();
    LoginRepository loginRepository;


    // Filters
    int maxTime = 1000;
    ArrayList<String> ingredients = new ArrayList<String>();
    int maxIngredients = 1000;
    ArrayList<String> tags = new ArrayList<String>();

    // For profile
    String profileUsername = "";

    // For tag
    String tagName = "";


    boolean isLoading = false;
    /*
    @param feedType Can be "users", "tags", "recipeBook"
     */
    public endlessScroll(RecyclerView recycler, String feedType, String username, String tagName) {
        this.feedType = feedType;
        this.profileUsername = username;
        this.tagName = tagName;
        recyclerView = recycler;

        if (feedType == "users") {
            // Set default filters from user's foodPreferences
            loginRepository = LoginRepository.getInstance();
            LoggedInUser loggedInUser = loginRepository.getUser();
            if (loggedInUser.getSettings().containsKey("foodPreferences")) {
                this.maxTime = ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("maxTime").getAsInt();
                this.ingredients = new Gson().fromJson( ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("ingredients").getAsJsonArray(), ArrayList.class);
                this.maxIngredients = ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("maxIngredients").getAsInt();
                this.tags = new Gson().fromJson( ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("tags").getAsJsonArray(), ArrayList.class);
            }
        }
    }

    public void populateData(ArrayList<Recipe> initialRecipes) {
        recipes.setValue(recipeList);
        recipes.postValue(initialRecipes);
        for(int x = 0; x < recipeList.size(); x++){
            Log.d("recipe1", recipeList.get(x).getTitle());
        }
    }

    public void populateDataProfile(ArrayList<User> initialUsers){
        users.setValue(userList);
        users.postValue(initialUsers);
        for(int x = 0; x < userList.size(); x++){
            Log.d("users", userList.get(x).getUsername());
        }
    }


    public endlessScroll(RecyclerView recycler, String feedType){
        this(recycler, feedType, "", "");
    }


    public endlessScroll(RecyclerView recycler, String feedType, String username){
        this(recycler, feedType, username, "");
    }

    public void initAdapter(Fragment fragmment) {
        recyclerViewAdapter = new RecyclerViewAdapter(recipeList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmment.getContext()));
        recyclerViewAdapter.setThisFragment(fragmment);
        recipeRepository = RecipeRepository.getInstance();
        recipes.observeForever(new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                recipeList.addAll(recipes);
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
        /**forumRepository = ForumRepository.getInstance();
         resultingPosts.observeForever(new Observer<ArrayList<ForumPost>>() {
        @Override
        public void onChanged(ArrayList<ForumPost> resultingPosts) {
        // Populate endlessScroll with forums
        forumPostsList.addAll(resultingPosts);
        recyclerViewAdapter.notifyDataSetChanged();
        isLoading = false;
        }
        });
         */
    }


    public void initProfileAdapter(Fragment fragmment) {

        UserViewAdapter = new UserViewAdapter(userList);
        recyclerView.setAdapter(UserViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmment.getContext()));
        UserViewAdapter.setThisFragment(fragmment);

        UserRepository = UserRepository.getInstance();
        users.observeForever(new Observer<ArrayList<User>>() {

            @Override
            public void onChanged(ArrayList<User> users) {
                userList.addAll(users);
                UserViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
    }


    public void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == recipeList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
    public void loadMore() {
        recipeList.add(null);
        recyclerViewAdapter.notifyItemInserted(recipeList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeList.remove(recipeList.size() - 1);
                int scrollPosition = recipeList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                if(feedType.equals("users")) {
                    recipeRepository.getRecipesForFeedByUsers(maxTime, ingredients, maxIngredients, tags, "likes", recipeList.size(), recipes);
                }else if(feedType.equals("recipeBook")){
                    recipeRepository.getRecipesForRecipeBook(maxTime, ingredients, maxIngredients, tags, "likes", recipeList.size(), recipes);
                }else if(feedType.equals("explore")){
                    recipeRepository.getRecipesForExplore(recipeList.size(), recipes);
                } else if(feedType.equals("tags")){
                    recipeRepository.getRecipesForFeedByTags(maxTime, ingredients, maxIngredients, tags, "likes", recipeList.size(), recipes);
                }
                else if(feedType.equals("profile")){
                    recipeRepository.getRecipesForProfile(profileUsername, recipeList.size(), recipes);
                } else if (feedType.equals("tag")) {
                    recipeRepository.getRecipesForTag(tagName, recipeList.size(), recipes);
                }
            }
        }, 10000);
    }

    public void updateFilters(int maxTime, ArrayList<String> ingredients, int maxIngredients, ArrayList<String> tags) {
        this.maxTime = maxTime;
        this.ingredients = ingredients;
        this.maxIngredients = maxIngredients;
        this.tags = tags;
    }
}