package com.group2.recipeze.ui.feed;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.LoggedInUser;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.util.ArrayList;

/**
 * FeedFragment.
 */
public class FeedFragment extends Fragment implements filters.hasFilters{
    RecyclerView feedRecyclerView;
    com.cooltechworks.views.shimmer.ShimmerRecyclerView shimmerRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    LoginRepository loginRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    Button tagsBtn;
    Button usersBtn;
    Button filtersBtn;
    Drawable selectedTab;

    int maxTime = 1000;
    ArrayList<String> ingredients = new ArrayList<String>();
    int maxIngredients = 1000;
    ArrayList<String> tags = new ArrayList<String>();

    private FeedViewModel feedViewModel;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        FeedFragment thisFragment = this;

        feedRecyclerView = root.findViewById(R.id.recipes);
        shimmerRecyclerView = root.findViewById(R.id.shimmer_recycler_view);
        shimmerRecyclerView.showShimmerAdapter();

        recipeRepository = RecipeRepository.getInstance();

        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                shimmerRecyclerView.hideShimmerAdapter();
                feedRecyclerView.setVisibility(View.VISIBLE);
                endlessScrollManager = new endlessScroll(feedRecyclerView, "feed");
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.updateFilters(maxTime, ingredients, maxIngredients, tags);
            }
        });

        tagsBtn = root.findViewById(R.id.tagsTab);
        usersBtn = root.findViewById(R.id.usersTab);

        selectedTab = ContextCompat.getDrawable(getContext(), R.drawable.tab_background);

        tagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagsBtn.setBackground(selectedTab);
                usersBtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        usersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersBtn.setBackground(selectedTab);
                tagsBtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        filtersBtn = root.findViewById(R.id.filter);

        filtersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment filterDialog = new filters(FeedFragment.this);
                filterDialog.show(getParentFragmentManager(), "FilterDialog");
            }
        });

        // Set default filters from user's foodPreferences
        loginRepository = LoginRepository.getInstance();
        LoggedInUser loggedInUser = loginRepository.getUser();
        if (loggedInUser.getSettings().containsKey("foodPreferences")) {
            this.maxTime = ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("maxTime").getAsInt();
            this.ingredients = new Gson().fromJson( ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("ingredients").getAsJsonArray(), ArrayList.class);
            this.maxIngredients = ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("maxIngredients").getAsInt();
            this.tags = new Gson().fromJson( ((JsonObject) loggedInUser.getSettings().get("foodPreferences")).get("tags").getAsJsonArray(), ArrayList.class);
        }

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request
        recipeRepository.getRecipesForFeedByUsers(maxTime, ingredients, maxIngredients, tags, "likes", 0, recipes);
    }

    @Override
    public void updateFilters(int maxTime, ArrayList<String> ingredients, int maxIngredients, ArrayList<String> tags){
        shimmerRecyclerView.showShimmerAdapter();
        feedRecyclerView.setVisibility(View.INVISIBLE);
        this.maxTime = maxTime;
        this.ingredients = ingredients;
        this.maxIngredients = maxIngredients;
        this.tags = tags;
        recipeRepository.getRecipesForFeedByUsers(maxTime, ingredients, maxIngredients, tags, "likes", 0, recipes);
    }
}