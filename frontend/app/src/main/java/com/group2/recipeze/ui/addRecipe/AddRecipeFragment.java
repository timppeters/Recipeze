package com.group2.recipeze.ui.addRecipe;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.group2.recipeze.R;
import com.group2.recipeze.data.TagRepository;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.ui.addRecipe.AddRecipeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddRecipeFragment extends BottomSheetDialogFragment {

    BottomSheetBehavior bottomSheetBehavior;
    private AddRecipeViewModel addRecipeViewModel;
    private TagRepository tagRepository;
    private MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>();

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

        tagRepository = TagRepository.getInstance();
        ChipGroup tagsGroup = view.findViewById(R.id.tagsGroup);

        tags.observe(this, new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                for (Tag tag : tags) {
                    System.out.println(tag.getName());
                    Chip tagChip = (Chip) getLayoutInflater().inflate(R.layout.layout_chip_filter, tagsGroup, false);
                    tagChip.setText(tag.getName());
                    tagsGroup.addView(tagChip);
                }

            }
        });

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