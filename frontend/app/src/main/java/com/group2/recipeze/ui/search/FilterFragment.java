package com.group2.recipeze.ui.search;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.group2.recipeze.R;
import com.group2.recipeze.ui.addRecipe.AddRecipeViewModel;

public class FilterFragment extends Fragment {

    private FilterViewModel mViewModel;

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(FilterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filter, container, false);

        //connecting elements
        SeekBar minTime = (SeekBar) root.findViewById(R.id.TimeMinSeek);
        SeekBar maxTime = (SeekBar) root.findViewById(R.id.TimeMaxSeek);
        SeekBar difficulty = (SeekBar) root.findViewById(R.id.DifficultySeek);
        SeekBar ingNum = (SeekBar) root.findViewById(R.id.NumOfIndSeek);

        TextView minTimeText = (TextView) root.findViewById(R.id.minTimeShow);
        TextView maxTimeText = (TextView) root.findViewById(R.id.maxTimeShow);
        TextView difficultyText = (TextView) root.findViewById(R.id.difficultyShow);
        TextView ingNumText = (TextView) root.findViewById(R.id.ingNumShow);

        Spinner diet = (Spinner) root.findViewById(R.id.DietSpinner);

        CheckBox breakfastCheck = (CheckBox) root.findViewById(R.id.BreakfastCheck);
        CheckBox lunchCheck = (CheckBox) root.findViewById(R.id.LunchCheck);
        CheckBox dinnerCheck = (CheckBox) root.findViewById(R.id.DinnerCheck);
        CheckBox snackCheck = (CheckBox) root.findViewById(R.id.SnackCheck);
        CheckBox britishCheck = (CheckBox) root.findViewById(R.id.BritishCheck);
        CheckBox chineseCheck = (CheckBox) root.findViewById(R.id.ChineseCheck);
        CheckBox japanCheck = (CheckBox) root.findViewById(R.id.JapaneseCheck);
        CheckBox thaiCheck = (CheckBox) root.findViewById(R.id.ThaiCheck);
        CheckBox italianCheck = (CheckBox) root.findViewById(R.id.ItalianCheck);
        CheckBox mexicanCheck = (CheckBox) root.findViewById(R.id.MexicanCheck);
        CheckBox greekCheck = (CheckBox) root.findViewById(R.id.GreekCheck);
        CheckBox frenchCheck = (CheckBox) root.findViewById(R.id.FrenchCheck);
        CheckBox cuisineArray[] = {
                breakfastCheck,
                lunchCheck,
                dinnerCheck,
                snackCheck,
                britishCheck,
                chineseCheck,
                japanCheck,
                thaiCheck,
                italianCheck,
                mexicanCheck,
                greekCheck,
                frenchCheck,
        };

        minTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minTimeText.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxTimeText.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        difficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                difficultyText.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ingNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ingNumText.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        Fragment here = this;
        Button filtersChosenButton = root.findViewById(R.id.FiltersChosenBut);

        filtersChosenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(CheckBox each: cuisineArray){
                    if(each.isChecked()){
                        System.out.println(each.getText());
                    }
                }
                System.out.println("MIN TIME: " + minTimeText.getText());
                System.out.println("MAX TIME: " + maxTimeText.getText());
                System.out.println("Difficulty: " + difficultyText.getText());
                System.out.println("Ingerdient number : " + ingNumText.getText());
                System.out.println("DIET CHOICE: " + diet.getSelectedItem());
                Toast.makeText(getActivity(), "CLICKED", Toast.LENGTH_SHORT).show();
                //NavHostFragment.findNavController(here).navigate(R.id.action_filterFragment_to_searchFragment);
            }
        });

        return root;
    }

}