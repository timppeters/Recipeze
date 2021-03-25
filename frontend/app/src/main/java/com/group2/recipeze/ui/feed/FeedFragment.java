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

import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.util.ArrayList;

/**
 * FeedFragment.
 */
public class FeedFragment extends Fragment{
    RecyclerView feedRecyclerView;
    RecyclerViewAdapter feedRecyclerViewAdapter;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    Button tagsBtn;
    Button usersBtn;
    Button filtersBtn;
    Drawable selectedTab;

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

        recipeRepository = RecipeRepository.getInstance();

        feedRecyclerView = root.findViewById(R.id.recipes);
        endlessScrollManager = new endlessScroll(feedRecyclerView);
        endlessScrollManager.initAdapter(thisFragment);
        endlessScrollManager.initScrollListener();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                endlessScrollManager.populateData(recipes);
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

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request
        ArrayList<String> ingredients = new ArrayList<String>();
        ArrayList<String> tags = new ArrayList<String>();
        ingredients.add("chicken");
        recipeRepository.getRecipesForFeedByUsers(1000, ingredients, 1000, tags, "likes", 0, recipes);
    }

    public void updateFilters(int maxTime, ArrayList<String> ingredients, int maxIngredients, ArrayList<String> tags){
        Log.d("awd", String.valueOf(maxTime) + ingredients.toString() + String.valueOf(maxIngredients) + tags.toString());
        ingredients.add("butter");
        recipeRepository.getRecipesForFeedByUsers(30, ingredients, 1000, tags, "likes", 0, recipes);
    }
}