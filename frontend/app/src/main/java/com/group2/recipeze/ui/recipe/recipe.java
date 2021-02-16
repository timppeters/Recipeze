package com.group2.recipeze.ui.recipe;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.group2.recipeze.R;
import com.group2.recipeze.ui.recipeBook.RecipeBookViewModel;

/**
 * Recipe Fragment
 */
public class recipe extends Fragment {

    private RecipeViewModel recipeViewModel;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        recipeViewModel =
                new ViewModelProvider(this).get(RecipeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipebook, container, false);

        final TextView prepTimeValue = root.findViewById(R.id.prepTimeValue);
        final TextView cookTimeValue = root.findViewById(R.id.cookTimeValue);

        recipeViewModel.getPrepTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {  prepTimeValue.setText(s); }
        });

        recipeViewModel.getCookTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {  cookTimeValue.setText(s); }
        });

        return root;

    }
}