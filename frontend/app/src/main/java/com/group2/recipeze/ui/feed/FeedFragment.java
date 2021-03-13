package com.group2.recipeze.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;

import java.util.ArrayList;

/**
 * FeedFragment.
 */
public class FeedFragment extends Fragment {
    RecyclerView feedRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

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

        recipeRepository = RecipeRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
            }
        });
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        feedRecyclerView = view.findViewById(R.id.recipes);
        feedRecyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<>(), null));
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        endlessScrollManager = new endlessScroll(feedRecyclerView);
        endlessScrollManager.populateData();
        endlessScrollManager.initAdapter();
        endlessScrollManager.initScrollListener();


        // Just an example request
        ArrayList<String> ingredients = new ArrayList<String>();
        //ingredients.add("tomatoes");
        ArrayList<String> tags = new ArrayList<String>();
        recipeRepository.getRecipesForFeedByUsers(130, ingredients, 6, tags, "likes", 0, recipes);
    }
}