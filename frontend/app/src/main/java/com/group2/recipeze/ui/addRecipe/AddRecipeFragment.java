package com.group2.recipeze.ui.addRecipe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.group2.recipeze.R;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.TagRepository;
import com.group2.recipeze.data.model.Tag;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeFragment extends BottomSheetDialogFragment {

    BottomSheetBehavior bottomSheetBehavior;
    private AddRecipeViewModel addRecipeViewModel;
    private TagRepository tagRepository;
    private MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>();
    private Chip addImageBtn;
    private ImagesListAdapter imagesAdapter;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.fragment_addrecipe, null);

        //setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));


        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);


        //setting max height of bottom sheet
        //view.findViewById(R.id.extraSpace).setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));

        AddRecipeFragment thisFragment = this;
        EditText title = view.findViewById(R.id.recipeTitle);
        EditText description = view.findViewById(R.id.description);
        Chip addIngredientBtn = view.findViewById(R.id.addIngredient);
        Chip addStepBtn = view.findViewById(R.id.addStep);
        RecyclerView stepsList = view.findViewById(R.id.stepsList);
        RecyclerView ingredientsList = view.findViewById(R.id.ingredientsList);
        ChipGroup tagsGroup = view.findViewById(R.id.tagsGroup);
        EditText prepTime = view.findViewById(R.id.prepTimeValue);
        EditText cookTime = view.findViewById(R.id.cookTimeValue);
        addImageBtn = view.findViewById(R.id.addImage);
        RecyclerView imagesList = view.findViewById(R.id.imagesList);
        Button saveRecipeBtn = view.findViewById(R.id.saveRecipeButton);


        ingredientsList.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        IngredientsListAdapter ingredientsAdapter = new IngredientsListAdapter(new ArrayList<String>(), new ArrayList<String>());
        ingredientsList.setAdapter(ingredientsAdapter);
        ingredientsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        stepsList.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        StepsListAdapter stepsAdapter = new StepsListAdapter(new ArrayList<String>());
        stepsList.setAdapter(stepsAdapter);
        stepsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        imagesList.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        imagesAdapter = new ImagesListAdapter(new ArrayList<Bitmap>());
        imagesList.setAdapter(imagesAdapter);
        imagesList.setLayoutManager(new LinearLayoutManager(getActivity()));


        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsAdapter.newIngredient(view);
            }
        });

        addStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsAdapter.newStep(view);
            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(thisFragment)
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                        .includeVideo(false) // Show video on image picker
                        .multi() // multi mode (default mode)
                        .limit(5) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .enableLog(false) // disabling log
                        .start(); // start image picker activity with request code
            }
        });

        saveRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeRepository recipeRepository = RecipeRepository.getInstance();
                MutableLiveData<Integer> resultingRecipeId = new MutableLiveData<>();
                ArrayList<String> tagNames = new ArrayList<>();
                List<Integer> ids = tagsGroup.getCheckedChipIds();
                for (Integer id:ids){
                    Chip chip = tagsGroup.findViewById(id);
                    tagNames.add(chip.getText().toString());
                }

                resultingRecipeId.observe(thisFragment, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        System.out.println("NEW RECIPE: " + integer);
                    }
                });

                recipeRepository.createRecipe(
                        title.getText().toString(),
                        description.getText().toString(),
                        ingredientsAdapter.getIngredients(),
                        ingredientsAdapter.getIngredientsAmounts(),
                        stepsAdapter.getStepsForRecipeSave(),
                        imagesAdapter.getImagesForRecipeSave(),
                        tagNames,
                        Integer.parseInt(prepTime.getText().toString()),
                        Integer.parseInt(cookTime.getText().toString()),
                        resultingRecipeId);
            }
        });

        tags.observe(this, new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                for (Tag tag : tags) {
                    Chip tagChip = (Chip) getLayoutInflater().inflate(R.layout.layout_chip_filter, tagsGroup, false);
                    tagChip.setText(tag.getName());
                    tagsGroup.addView(tagChip);
                }

            }
        });

        tagRepository = TagRepository.getInstance();
        tagRepository.getAllTags(tags);



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_EXPANDED == i) {
                    //

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == i) {
                    //
                }

                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        return bottomSheet;
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            ArrayList<Image> images = new ArrayList<>(ImagePicker.getImages(data));
            if (images.size() == 5) {
                addImageBtn.setVisibility(View.INVISIBLE);
            } else {
                addImageBtn.setVisibility(View.VISIBLE);
            }
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            for (Image image: images) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image.getUri());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmaps.add(bitmap);
            }
            imagesAdapter.populate(bitmaps);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

}