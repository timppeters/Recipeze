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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group2.recipeze.R;
import androidx.recyclerview.widget.RecyclerView;


import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.UserRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.User;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.feed.FeedFragment;
import com.group2.recipeze.ui.feed.filters;

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
    com.cooltechworks.views.shimmer.ShimmerRecyclerView shimmerRecyclerView;

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
        ProfileFragment thisFragment = this;
        shimmerRecyclerView = root.findViewById(R.id.shimmer_recycler_view);
        shimmerRecyclerView.showShimmerAdapter();
        recipeRepository = RecipeRepository.getInstance();
        loginRepository = LoginRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                shimmerRecyclerView.hideShimmerAdapter();
                recyclerView = root.findViewById(R.id.profileRecipes);
                endlessScrollManager = new endlessScroll(recyclerView, "profile", loginRepository.getUser().getUsername());
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
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
        userRepository = UserRepository.getInstance();

        settingBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(thisFragment).navigate(R.id.action_navigation_profile_to_settingsFragment2);
            }
        });

        Button foodPreferencesBtn = root.findViewById(R.id.editFoodPreferences);

        foodPreferencesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment preferencesDialog;
                if (loginRepository.getUser().getSettings().containsKey("foodPreferences")) {
                    preferencesDialog = new FoodPreferencesFragment(thisFragment, loginRepository.getUser().getSettings().get("foodPreferences").getAsJsonObject());
                } else {
                    preferencesDialog = new FoodPreferencesFragment(thisFragment, null);
                }
                preferencesDialog.show(getParentFragmentManager(), "PreferencesDialog");
            }
        });

        userRepository.getPrivateProfile(profile_updated);

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request
        recipeRepository.getRecipesForProfile(loginRepository.getUser().getUsername(), 0, recipes);
    }

    public void updatePreferences(int maxTime, ArrayList<String> ingredients, int maxIngredients, ArrayList<String> tags) {
        // save preferences to LoggedInUser & database
        JsonObject foodPreferences = new JsonObject();
        foodPreferences.addProperty("maxTime", maxTime);
        foodPreferences.add("ingredients", new Gson().toJsonTree(ingredients).getAsJsonArray());
        foodPreferences.addProperty("maxIngredients", maxIngredients);
        foodPreferences.add("tags", new Gson().toJsonTree(tags).getAsJsonArray());
        loginRepository.getUser().getSettings().put("foodPreferences", foodPreferences);
        UserRepository userRepository = UserRepository.getInstance();
        userRepository.updateProfile(loginRepository.getUser().getUsername(), loginRepository.getUser().getEmail(), loginRepository.getUser().getBio(), loginRepository.getUser().getSettings());
    }
}