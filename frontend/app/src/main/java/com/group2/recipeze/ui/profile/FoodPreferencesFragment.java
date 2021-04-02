package com.group2.recipeze.ui.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.group2.recipeze.R;
import com.group2.recipeze.data.TagRepository;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.ui.feed.filterIngredientsListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodPreferencesFragment extends DialogFragment {
    private MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>();
    private TagRepository tagRepository;
    private AlertDialog foodPreferencesDialog;
    //private FeedFragment feedFragment;
    private ProfileFragment parent;
    private JsonObject currentPreferences;

    public FoodPreferencesFragment(ProfileFragment parent, JsonObject currentPreferences){
        this.parent = parent;
        this.currentPreferences = currentPreferences;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_filter_profile, null);
        builder.setView(dialogView);
        foodPreferencesDialog = builder.create();


        TextView ingredientsLabel = dialogView.findViewById(R.id.ingredientsFilterTxt);
        TextView dialogTitle = dialogView.findViewById(R.id.textView4);
        Button saveBtn = dialogView.findViewById(R.id.FiltersChosenBut);
        Button tagApplication = dialogView.findViewById(R.id.requestTagBtn);

        RecyclerView ingredientsList = dialogView.findViewById(R.id.filterIngredientsList);
        Chip addIngredientBtn = dialogView.findViewById(R.id.addIngredient2);
        ChipGroup tagsGroup = dialogView.findViewById(R.id.filtertagsGroup);
        Button filterBtn = dialogView.findViewById(R.id.FiltersChosenBut);
        com.google.android.material.slider.Slider maxTime = dialogView.findViewById(R.id.TimeSeek);
        com.google.android.material.slider.Slider maxIngredients = dialogView.findViewById(R.id.NumOfIndSeek);

        if (currentPreferences != null) {
            if (currentPreferences.get("maxTime").getAsFloat() == 1000.0) {
                maxTime.setValue(0);
            } else {
                maxTime.setValue(currentPreferences.get("maxTime").getAsFloat());
            }

            if (currentPreferences.get("maxIngredients").getAsFloat() == 1000.0) {
                maxIngredients.setValue(0);
            } else {
                maxIngredients.setValue(currentPreferences.get("maxIngredients").getAsFloat());
            }
        }

        ingredientsList.addItemDecoration(new DividerItemDecoration(dialogView.getContext(), DividerItemDecoration.VERTICAL));
        filterIngredientsListAdapter ingredientsAdapter;
        if (currentPreferences != null) {
            ingredientsAdapter = new filterIngredientsListAdapter(new Gson().fromJson(currentPreferences.get("ingredients").getAsJsonArray(), ArrayList.class));
        } else {
            ingredientsAdapter = new filterIngredientsListAdapter(new ArrayList<String>());
        }

        ingredientsList.setAdapter(ingredientsAdapter);
        ingredientsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> tagNames = new ArrayList<>();
                List<Integer> ids = tagsGroup.getCheckedChipIds();
                for (Integer id:ids){
                    Chip chip = tagsGroup.findViewById(id);
                    tagNames.add(chip.getText().toString());
                }
                //if user has put 0 as filter, set it to 1000 (otherwise you get no results)
                int time = (int) maxTime.getValue();
                if (time == 0){
                    time = 1000;
                }
                int ing = (int) maxIngredients.getValue();
                if(ing == 0){
                    ing = 1000;
                }

                parent.updatePreferences(time,  ingredientsAdapter.getIngredients(), ing, tagNames);
                foodPreferencesDialog.dismiss();
            }
        });

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsAdapter.newIngredient(dialogView);
            }
        });

        tags.observe(this, new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                for (Tag tag : tags) {
                    Chip tagChip = (Chip) getLayoutInflater().inflate(R.layout.layout_chip_filter, tagsGroup, false);
                    tagChip.setText(tag.getName());
                    if (currentPreferences != null && currentPreferences.get("tags").getAsJsonArray().contains(new JsonPrimitive(tag.getName()))) {
                        tagChip.setChecked(true);
                    }
                    tagsGroup.addView(tagChip);
                }

            }
        });

        tagApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tagRepository = TagRepository.getInstance();
        tagRepository.getAllTags(tags);
        // Create the AlertDialog object and return it
        return foodPreferencesDialog;
    }
}

