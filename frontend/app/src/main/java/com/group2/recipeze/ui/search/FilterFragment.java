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

import java.util.ArrayList;

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

        CheckBox glutenCheck = (CheckBox) root.findViewById(R.id.GlutenFree);
        CheckBox NoFish = (CheckBox) root.findViewById(R.id.NoFish);
        CheckBox NutCheck = (CheckBox) root.findViewById(R.id.NutCheck);
        CheckBox DairyFree = (CheckBox) root.findViewById(R.id.DairyFree);
        CheckBox PotatoCheck = (CheckBox) root.findViewById(R.id.PotatoCheck);
        CheckBox pastaCheck = (CheckBox) root.findViewById(R.id.pastaCheck);
        CheckBox riceCheck = (CheckBox) root.findViewById(R.id.riceCheck);
        CheckBox carrotCheck = (CheckBox) root.findViewById(R.id.carrotCheck);
        CheckBox PepperCheck = (CheckBox) root.findViewById(R.id.PepperCheck);
        CheckBox OnionCheck = (CheckBox) root.findViewById(R.id.OnionCheck);
        CheckBox TomatoCheck = (CheckBox) root.findViewById(R.id.TomatoCheck);
        CheckBox EggCheck = (CheckBox) root.findViewById(R.id.EggCheck);
        CheckBox tagArr[] = {
                glutenCheck,
                NutCheck,
                DairyFree,
                NoFish
        };

        CheckBox specificIng[] = {
                PotatoCheck,
                pastaCheck,
                riceCheck,
                carrotCheck,
                PepperCheck,
                OnionCheck,
                TomatoCheck,
                EggCheck
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
                ArrayList<String> tags = new ArrayList<>();
                for(CheckBox each: tagArr){
                    if(each.isChecked()){
                        tags.add(String.valueOf(each.getText()));
                        System.out.println(each.getText());
                    }
                }
                ArrayList<String> ingList = new ArrayList<>();
                for(CheckBox each: specificIng){
                    if(each.isChecked()){
                        ingList.add(String.valueOf(each.getText()));
                    }
                }
                tags.add(String.valueOf(diet.getSelectedItem()));
                SearchFragment.setIngNum(ingNum.getProgress());
                SearchFragment.setMaxTime(maxTime.getProgress());
                SearchFragment.setTags(tags);
                SearchFragment.setIngredientList(ingList);
                System.out.println("MIN TIME: " + minTimeText.getText());
                System.out.println("MAX TIME: " + maxTimeText.getText());
                System.out.println("Difficulty: " + difficultyText.getText());
                System.out.println("Ingerdient number : " + ingNumText.getText());
                System.out.println("DIET CHOICE: " + diet.getSelectedItem());
                Toast.makeText(getActivity(), "CLICKED", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(here).navigate(R.id.action_filterFragment_to_searchFragment);
            }
        });

        return root;
    }

}