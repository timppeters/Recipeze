package com.group2.recipeze.ui.recipe;

import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.JsonObject;
import com.group2.recipeze.R;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;

/**
 * Recipe Fragment
 */
public class RecipeFragment extends Fragment {

    private RecipeViewModel recipeViewModel;
    private boolean scrollable = false;
    private int originalTopMargin;
    private MutableLiveData<Recipe> recipe = new MutableLiveData<Recipe>();
    private RecipeRepository recipeRepository;
    private RecipeFragment thisFragment = this;
    private boolean showDelete = false;

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
        recipeRepository = RecipeRepository.getInstance();

        Recipe recipe = getArguments().getParcelable("recipe");
        Integer recipeId = getArguments().getInt("recipeId");
        if (recipe != null) {
            showRecipe(recipe, root);
        } else {
            MutableLiveData<Recipe> r = new MutableLiveData<>();
            r.observe(getViewLifecycleOwner(), new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    showRecipe(recipe, root);
                }
            });
            recipeRepository.getRecipe(recipeId, r);
        }


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
        final ImageButton menu = root.findViewById(R.id.menu);

        prepTimeValue.setText(String.valueOf(recipe.getPrepTime()) + " mins");
        cookTimeValue.setText(String.valueOf(recipe.getCookTime()) + " mins");
        recipeName.setText(recipe.getTitle());
        recipeDescription.setText(recipe.getDescription());
        if (recipe.getTags() != null) {
            for (String tagName : recipe.getTags()) {
                TextView tagView = (TextView) getLayoutInflater().inflate(R.layout.item_tag, null);
                tagView.setText(tagName);
                tagsLayout.addView(tagView);
                tagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("tagName", tagName);
                        NavHostFragment.findNavController(thisFragment).navigate(R.id.action_recipeFragment_to_fragment_tag, bundle);
                    }
                });
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

        ViewPager imageSlider = root.findViewById(R.id.slider);
        SliderAdapter sliderAdapter = new SliderAdapter(this.getContext(), recipe.getImagesAsBitmaps());
        imageSlider.setAdapter(sliderAdapter);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If button in non-liked state
                if (((String)likeButton.getTag()).equals("unliked")) {
                    recipeRepository.likeRecipe(recipe.getRecipeId());
                    likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_filled,null));
                    recipe.setLiked(true);
                    likeButton.setTag("liked");
                } else {
                    recipeRepository.unlikeRecipe(recipe.getRecipeId());
                    likeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_,null));
                    recipe.setLiked(false);
                    likeButton.setTag("unliked");
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }*/
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });

        LoginRepository loginRepository = LoginRepository.getInstance();
        if (recipe.getAuthor().equals(loginRepository.getUser().getUsername())) {
            showDelete = true;
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.threedotmenu_actions, popup.getMenu());
                if (!showDelete) {
                    popup.getMenu().removeItem(R.id.menu_delete);
                }
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                recipeRepository.deleteRecipe(recipe.getRecipeId());
                                NavHostFragment.findNavController(thisFragment).popBackStack();
                                return true;
                            case R.id.menu_report:
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });


        // Nutrition
        final TextView servings = root.findViewById(R.id.servingValue);
        final TextView calories = root.findViewById(R.id.caloriesValue);
        final TextView totalFat = root.findViewById(R.id.totalFatValue);
        final TextView totalFatDaily = root.findViewById(R.id.totalFatDailyPercentage);
        final TextView satFat = root.findViewById(R.id.satFatValue);
        final TextView satFatDaily = root.findViewById(R.id.satFatDailyPercentage);
        final TextView transFat = root.findViewById(R.id.transFatValue);
        //final TextView transFatDaily = root.findViewById(R.id.transFatDailyPercentage);
        final TextView totalCarbs = root.findViewById(R.id.totalCarbsValue);
        final TextView totalCarbsDaily = root.findViewById(R.id.totalCarbsDailyPercentage);
        final TextView fiber = root.findViewById(R.id.fiberValue);
        final TextView fiberDaily = root.findViewById(R.id.fiberDailyPercentage);
        final TextView sugar = root.findViewById(R.id.sugarValue);
        //final TextView sugarDaily = root.findViewById(R.id.sugarDailyPercentage);
        final TextView protein = root.findViewById(R.id.proteinValue);
        final TextView proteinDaily = root.findViewById(R.id.proteinDailyPercentage);
        final TextView cholesterol = root.findViewById(R.id.cholesterolValue);
        final TextView cholesterolDaily = root.findViewById(R.id.cholesterolDailyPercentage);
        final TextView sodium = root.findViewById(R.id.sodiumValue);
        final TextView sodiumDaily = root.findViewById(R.id.sodiumDailyPercentage);

        JsonObject nutrition = recipe.getNutrition();
        if (nutrition != null) {


            servings.setText(root.getResources().getString(R.string.servings, (int) Float.parseFloat(String.valueOf(nutrition.get("yield")))));
            calories.setText(String.valueOf(nutrition.get("calories")));
            totalFat.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            totalFatDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            satFat.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("FASAT").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            satFatDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("FASAT").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            transFat.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("FATRN").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            /*transFatDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("FATRN").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit"));*/
            totalCarbs.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("CHOCDF").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            totalCarbsDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("CHOCDF").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            fiber.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("FIBTG").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            fiberDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("FIBTG").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            sugar.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("SUGAR").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            /*sugarDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("SUGAR").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit"));*/
            protein.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("PROCNT").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            proteinDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("PROCNT").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            cholesterol.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("CHOLE").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            cholesterolDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("CHOLE").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            sodium.setText(String.valueOf(nutrition.get("totalNutrients").getAsJsonObject().get("NA").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalNutrients").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
            sodiumDaily.setText(String.valueOf(nutrition.get("totalDaily").getAsJsonObject().get("NA").getAsJsonObject().get("quantity"))
                    + nutrition.get("totalDaily").getAsJsonObject().get("FAT").getAsJsonObject().get("unit").getAsString().replace("\"", ""));
        }
    }
}
