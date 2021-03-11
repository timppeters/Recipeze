package com.group2.recipeze.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.endlessScroll;

import java.util.ArrayList;

/**
 * ProfileFragment.
 */
public class ProfileFragment extends Fragment{

    private ProfileViewModel profileViewModel;
    RecyclerView recyclerView;
    endlessScroll endlessScrollManager;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        ImageButton settingBut = root.findViewById(R.id.SetBut);
        Fragment here = this;

        settingBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(here).navigate(R.id.action_navigation_profile_to_settingsFragment2);
            }
        });

        Button button = root.findViewById(R.id.editFoodPreferences);
        Fragment here2 = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(here2).navigate(R.id.action_navigation_profile_to_foodPreferences2);
            }
        });

        recyclerView = root.findViewById(R.id.profileRecipes);
        recyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        endlessScrollManager = new endlessScroll(recyclerView);
        endlessScrollManager.populateData();
        endlessScrollManager.initAdapter();
        endlessScrollManager.initScrollListener();


        return root;
    }

}