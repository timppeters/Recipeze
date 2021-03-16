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
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.util.ArrayList;

/**
 * RecipeBookFragment.
 */
public class RecipeBookFragment extends Fragment {

    RecyclerView recipeBookRecyclerView;
    //RecyclerViewAdapter.RecyclerViewClickListener listener;
    endlessScroll endlessScrollManager;

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
//        recipeBookViewModel = new ViewModelProvider(this).get(RecipeBookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipebook, container, false);
//
//        recipeBookRecyclerView = root.findViewById(R.id.recipes);
//        setOnClickListener(this);
//        recipeBookRecyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<Recipe>()));
//        recipeBookRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        endlessScrollManager = new endlessScroll(recipeBookRecyclerView);
//        endlessScrollManager.populateData(new ArrayList<>());
//        endlessScrollManager.initAdapter();
//        endlessScrollManager.initScrollListener();

        return root;
    }

//    private void setOnClickListener(Fragment here) {
//        listener = new RecyclerViewAdapter.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View v, int position) {
//                NavHostFragment.findNavController(here).navigate(R.id.action_navigation_recipeBook_to_recipe);
//            }
//        };
//    }

}