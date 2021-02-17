package com.group2.recipeze.ui.recipe;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.group2.recipeze.R;
import com.group2.recipeze.ui.recipeBook.RecipeBookViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe Fragment
 */
public class RecipeFragment extends Fragment {

    private RecipeViewModel recipeViewModel;
    private boolean scrollable = false;
    private int originalTopMargin;

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
        View root = inflater.inflate(R.layout.fragment_recipe, container, false);

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

        ImageSlider imageSlider = root.findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://p.ecopetit.cat/wpic/lpic/26-263518_tumblr-photography-wallpaper-rocks-on-earth-background.jpg",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://live.staticflickr.com/7006/6621416427_8504865e6a_z.jpg",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/662/618/496/natur-2560x1600-sceneries-wallpaper-preview.jpg",ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);

        LinearLayout contentParent = root.findViewById(R.id.contentParent);
        ScrollView content = root.findViewById(R.id.content);
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
                                content.setScrollY(0);
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
}