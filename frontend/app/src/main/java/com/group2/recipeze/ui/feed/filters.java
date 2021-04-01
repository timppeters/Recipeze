package com.group2.recipeze.ui.feed;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.group2.recipeze.R;
import com.group2.recipeze.data.TagRepository;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class filters extends DialogFragment {

    public interface hasFilters{
        public void updateFilters(int maxTime, ArrayList<String> ingredients, int maxIngredients, ArrayList<String> tags);
    }

    private MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>();
    private TagRepository tagRepository;
    private AlertDialog filterDialog;
    //private FeedFragment feedFragment;
    private hasFilters feedFragment;

    public filters(hasFilters feedFragment){
        this.feedFragment = feedFragment;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_filter_feed, null);

        builder.setView(dialogView);
        filterDialog = builder.create();


        RecyclerView ingredientsList = dialogView.findViewById(R.id.filterIngredientsList);
        Chip addIngredientBtn = dialogView.findViewById(R.id.addIngredient2);
        ChipGroup tagsGroup = dialogView.findViewById(R.id.filtertagsGroup);
        Button filterBtn = dialogView.findViewById(R.id.FiltersChosenBut);
        com.google.android.material.slider.Slider maxTime = dialogView.findViewById(R.id.TimeSeek);
        com.google.android.material.slider.Slider maxIngredients = dialogView.findViewById(R.id.NumOfIndSeek);

        ingredientsList.addItemDecoration(new DividerItemDecoration(dialogView.getContext(), DividerItemDecoration.VERTICAL));
        filterIngredientsListAdapter ingredientsAdapter = new filterIngredientsListAdapter(new ArrayList<String>(), new ArrayList<String>());
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

                feedFragment.updateFilters(time,  ingredientsAdapter.getIngredients(), ing, tagNames);
                filterDialog.dismiss();
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
                    tagsGroup.addView(tagChip);
                }

            }
        });

        tagRepository = TagRepository.getInstance();
        tagRepository.getAllTags(tags);
        // Create the AlertDialog object and return it
        return filterDialog;
    }
}
