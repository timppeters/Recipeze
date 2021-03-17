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
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.util.ArrayList;

/**
 * RecipeBookFragment.
 */
public class RecipeBookFragment extends Fragment {
    RecyclerView recipeBookRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

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

        recipeRepository = RecipeRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                recipeBookRecyclerView = root.findViewById(R.id.recipes);
                recipeBookRecyclerView.setAdapter(new RecyclerViewAdapter(recipes));
                recipeBookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                endlessScrollManager = new endlessScroll(recipeBookRecyclerView);
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter();
                endlessScrollManager.initScrollListener();
            }
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request
        ArrayList<String> ingredients = new ArrayList<String>();
        ArrayList<String> tags = new ArrayList<String>();
        recipeRepository.getRecipesForRecipeBook(100, ingredients, 100, tags, "likes" , 0, recipes);
    }
}