package com.group2.recipeze.ui.recipe;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.group2.recipeze.R;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Recipe Fragment
 */
public class RecipeFragment extends Fragment {

    private RecipeViewModel recipeViewModel;
    private boolean scrollable = false;
    private int originalTopMargin;
    private MutableLiveData<Recipe> recipe = new MutableLiveData<Recipe>();
    private RecipeRepository recipeRepository;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        recipeViewModel =
                new ViewModelProvider(this).get(RecipeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipe, container, false);


        // Get recipeId from whoever called fragment
        Integer recipeId = getArguments().getInt("recipeId");
        recipeRepository = RecipeRepository.getInstance();

        recipe.observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                showRecipe(recipe, root);
            }
        });

        // get recipe from db
        recipeRepository.getRecipe(recipeId, recipe);

        LinearLayout contentParent = root.findViewById(R.id.contentParent);
        NestedScrollView content = root.findViewById(R.id.content);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) contentParent.getLayoutParams();
        originalTopMargin = layoutParams.topMargin;

        float dip = 60f; // size of topbar
        Resources r = getResources();
        float marginMin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        content.setOnTouchListener(new View.OnTouchListener() {

            float dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) contentParent.getLayoutParams();
                int marginDelta = layoutParams.topMargin;
                if (marginDelta > marginMin && marginDelta <= originalTopMargin || (marginDelta == marginMin && content.getScrollY() == 0)) { // block scrolling
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            dY = contentParent.getY() - event.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            int newMargin = (int) (event.getRawY() + dY);
                            if (newMargin >= marginMin && newMargin <= originalTopMargin) {
                                layoutParams.topMargin = newMargin;
                                contentParent.setLayoutParams(layoutParams);
                                //content.setScrollY(0);
                            }

                            break;
                        default:
                            return true;
                    }
                }
                return false; // allow scrolling
            }
        });


        return root;

    }

    // show recipe in UI
    public void showRecipe(Recipe recipe, View root) {
        final TextView prepTimeValue = root.findViewById(R.id.prepTimeValue);
        final TextView cookTimeValue = root.findViewById(R.id.cookTimeValue);
        final LinearLayout tagsLayout = root.findViewById(R.id.tagsLayout);
        final TextView recipeName = root.findViewById(R.id.recipeName);
        final TextView recipeDescription = root.findViewById(R.id.recipeDescription);
        final RecyclerView ingredientsList = root.findViewById(R.id.ingredientsList);
        final RecyclerView stepsList = root.findViewById(R.id.stepList);
        final ImageButton likeButton = root.findViewById(R.id.like);
        final ImageButton backButton = root.findViewById(R.id.back);

        prepTimeValue.setText(String.valueOf(recipe.getPrepTime()) + " mins");
        cookTimeValue.setText(String.valueOf(recipe.getCookTime()) + " mins");
        recipeName.setText(recipe.getTitle());
        recipeDescription.setText(recipe.getDescription());
        if (recipe.getTags() != null) {
            for (String tagName : recipe.getTags()) {
                TextView tagView = (TextView) getLayoutInflater().inflate(R.layout.item_tag, null);
                tagView.setText(tagName);
                tagsLayout.addView(tagView);
            }
        }

        RecipeIngredientsListAdapter ingredientsAdapter = new RecipeIngredientsListAdapter(recipe.getIngredients(), recipe.getIngredientsAmounts());
        ingredientsList.setAdapter(ingredientsAdapter);
        ingredientsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecipeStepsListAdapter stepsAdapter = new RecipeStepsListAdapter(recipe.getInstructions());
        stepsList.setAdapter(stepsAdapter);
        stepsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (recipe.getLiked()) {
            likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_filled,null));
            likeButton.setTag("liked");
        }

        ImageSlider imageSlider = root.findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://live.staticflickr.com/7006/6621416427_8504865e6a_z.jpg",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/662/618/496/natur-2560x1600-sceneries-wallpaper-preview.jpg",ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If button in non-liked state
                if (((String)likeButton.getTag()).equals("unliked")) {
                    recipeRepository.likeRecipe(recipe.getRecipeId());
                    likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_filled,null));
                    likeButton.setTag("liked");
                } else {
                    recipeRepository.unlikeRecipe(recipe.getRecipeId());
                    likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_,null));
                    likeButton.setTag("unliked");
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello");
                FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {

                }
            }
        });


    }
}