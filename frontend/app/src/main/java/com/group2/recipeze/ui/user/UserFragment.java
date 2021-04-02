package com.group2.recipeze.ui.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.UserRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.profile.ProfileFragment;
import com.group2.recipeze.ui.profile.ProfileViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFragment extends Fragment {

    private UserViewModel mViewModel;

    public static UserFragment newInstance() {
        return new UserFragment();
    }


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
        UserFragment thisFragment = this;
        Button followBut = root.findViewById(R.id.editFoodPreferences);
        followBut.setText("Follow");






        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request
        recipeRepository.getRecipesForProfile(loginRepository.getUser().getUsername(), 0, recipes);
        }
    }