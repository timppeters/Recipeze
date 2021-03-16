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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import androidx.recyclerview.widget.RecyclerView;


import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.UserRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ProfileFragment.
 */
public class ProfileFragment extends Fragment {
    public HashMap<String, ?> settings = new HashMap<>();
    private ProfileViewModel profileViewModel;
    RecyclerView recyclerView;
    endlessScroll endlessScrollManager;
    UserRepository userRepository;
    RecipeRepository recipeRepository;
    LoginRepository loginRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    private MutableLiveData<Boolean> profile_updated = new MutableLiveData<>();

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        recipeRepository = RecipeRepository.getInstance();
        loginRepository = LoginRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
            }
        });
        TextView username = root.findViewById(R.id.username);
        TextView bio = root.findViewById(R.id.bio);
        username.setText(loginRepository.getUser().getUsername());
        bio.setText(loginRepository.getUser().getBio());
        profile_updated.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                username.setText(loginRepository.getUser().getUsername());
                bio.setText(loginRepository.getUser().getBio());
            }
        });

        final TextView textView = root.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        ImageButton settingBut = root.findViewById(R.id.SetBut);
        Fragment here = this;
        userRepository = UserRepository.getInstance();
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

        recipeRepository.getRecipesForProfile("user", 0, recipes);
        recyclerView = root.findViewById(R.id.profileRecipes);
        recyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        endlessScrollManager = new endlessScroll(recyclerView);
        endlessScrollManager.populateData(new ArrayList<>());
        endlessScrollManager.initAdapter();
        endlessScrollManager.initScrollListener();
        userRepository.updateProfile(loginRepository.getUser().getUsername(), loginRepository.getUser().getEmail(), "I am vegan", settings);
        userRepository.getPrivateProfile(profile_updated);
        //userRepository.followUser("leokou");
        //userRepository.unfollowUser("leokou");
        return root;
    }


}