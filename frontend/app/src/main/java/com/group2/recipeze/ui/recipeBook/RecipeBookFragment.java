package com.group2.recipeze.ui.recipeBook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.$Gson$Preconditions;
import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.feed.FeedFragment;
import com.group2.recipeze.ui.feed.filters;
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.util.ArrayList;

/**
 * RecipeBookFragment.
 */
public class RecipeBookFragment extends Fragment implements filters.hasFilters{
    RecyclerView recipeBookRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    Button filtersBtn;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    com.cooltechworks.views.shimmer.ShimmerRecyclerView shimmerRecyclerView;

    int maxTime = 1000;
    ArrayList<String> ingredients = new ArrayList<String>();
    int maxIngredients = 1000;
    ArrayList<String> tags = new ArrayList<String>();

    private RecipeBookViewModel recipeBookViewModel;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recipeBookViewModel = new ViewModelProvider(this).get(RecipeBookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipebook, container, false);
        RecipeBookFragment thisFragment = this;

        recipeBookRecyclerView = root.findViewById(R.id.recipes);
        shimmerRecyclerView = root.findViewById(R.id.shimmer_recycler_view);
        shimmerRecyclerView.showShimmerAdapter();

        recipeRepository = RecipeRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                shimmerRecyclerView.hideShimmerAdapter();
                recipeBookRecyclerView.setVisibility(View.VISIBLE);
                endlessScrollManager = new endlessScroll(recipeBookRecyclerView, "recipeBook");
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
            }
        });

        filtersBtn = root.findViewById(R.id.filter2);

        filtersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment filterDialog = new filters(RecipeBookFragment.this);
                filterDialog.show(getParentFragmentManager(), "FilterDialog");
            }
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        recipeRepository.getRecipesForRecipeBook(maxTime, ingredients, maxIngredients, tags, "likes", 0, recipes);
    }

    @Override
    public void updateFilters(int maxTime, ArrayList<String> ingredients, int maxIngredients, ArrayList<String> tags){
        shimmerRecyclerView.showShimmerAdapter();
        recipeBookRecyclerView.setVisibility(View.INVISIBLE);
        this.maxTime = maxTime;
        this.ingredients = ingredients;
        this.maxIngredients = maxIngredients;
        this.tags = tags;
        recipeRepository.getRecipesForRecipeBook(maxTime, ingredients, maxIngredients, tags, "likes", 0, recipes);
    }
}