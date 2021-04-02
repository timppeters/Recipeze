package com.group2.recipeze.ui.search;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.SearchRepository;
import com.group2.recipeze.data.UserRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.data.model.User;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.explore.ExploreFragment;
import com.group2.recipeze.ui.feed.FeedViewModel;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;
/**
 * SearchFragment.
 */
public class SearchFragment extends Fragment {
    private RecyclerView searchRecipeRecyclerView;
    private RecyclerView searchProfileRecyclerView;
    private endlessScroll endlessScrollManager;
    //second endless scroll for the profiles
    private endlessScroll endlessScrollProfilesManager;
    private SearchRepository searchRepo;
    private static ArrayList<Recipe> recipes = new ArrayList<>();
    private static ArrayList<User> profiles = new ArrayList<>();
    private static ArrayList<Tag> searchedTags = new ArrayList<>();

    private static int maxTime = 100;
    private static int ingNum = 100;

    private static SearchViewModel searchViewModel;

    private Button recipesBtn;
    private Button profilesBtn;
    private Drawable selectedTab;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.search_fragment, container, false);
        SearchFragment thisFragment = this;

        searchProfileRecyclerView = root.findViewById(R.id.searchProfilesScroll);
        searchProfileRecyclerView.setVisibility(View.INVISIBLE);

        searchRecipeRecyclerView = root.findViewById(R.id.searchRecipesScroll);
        endlessScrollManager = new endlessScroll(searchRecipeRecyclerView, "tags");
        endlessScrollManager.populateData(recipes);
        endlessScrollManager.initAdapter(thisFragment);
        endlessScrollManager.initScrollListener();

        //second endless scroll for the profiles
        endlessScrollProfilesManager = new endlessScroll(searchProfileRecyclerView, "tags");
        endlessScrollProfilesManager.populateDataProfile(profiles);
        endlessScrollProfilesManager.initProfileAdapter(thisFragment);
        endlessScrollProfilesManager.initScrollListener();

        recipesBtn = root.findViewById(R.id.recipesTab);
        profilesBtn = root.findViewById(R.id.profilesTab);
        selectedTab = ContextCompat.getDrawable(getContext(), R.drawable.tab_background);

        recipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                recipesBtn.setBackground(selectedTab);
                profilesBtn.setBackgroundColor(Color.TRANSPARENT);
                // Display recipes
                searchRecipeRecyclerView.setVisibility(View.VISIBLE);
                searchProfileRecyclerView.setVisibility(View.INVISIBLE);
            }
        });

        profilesBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               profilesBtn.setBackground(selectedTab);
                                               recipesBtn.setBackgroundColor(Color.TRANSPARENT);
                                               // Display profiles
                                               searchRecipeRecyclerView.setVisibility(View.INVISIBLE);
                                               searchProfileRecyclerView.setVisibility(View.VISIBLE);
                                           }
                                       });

//            public void onChanged(ArrayList<Recipe> recipes) {
//                // Populate endlessScroll with recipes
//                searchRecyclerView = root.findViewById(R.id.searchRecipesScroll);
//                endlessScrollManager = new endlessScroll(searchRecyclerView, "tags");
//                endlessScrollManager.populateData(recipes);
//                endlessScrollManager.initAdapter(thisFragment);
//                endlessScrollManager.initScrollListener();
//
//            }
//        });

        Button forButton = root.findViewById(R.id.forumButton);
        Fragment here = this;

        forButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(here).navigate(R.id.action_searchFragment_to_foodForumFragment);
            }
        });

        Button filButton = root.findViewById(R.id.filterButton);

        filButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(here).navigate(R.id.action_searchFragment_to_filterFragment);
            }
        });

        SearchView search = root.findViewById(R.id.searchSearchPage);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRepo = SearchRepository.getInstance();
                MutableLiveData<Map<String, ArrayList>> results = new MutableLiveData<>();
                searchRepo.search(query, results);

                results.observe(getViewLifecycleOwner(), new Observer<Map<String, ArrayList>>() {
                    @Override
                    public void onChanged(Map<String, ArrayList> stringArrayListMap) {
                        recipes = stringArrayListMap.get("recipes");
                        profiles = stringArrayListMap.get("users");

                        endlessScrollManager.populateData(recipes);

                        // Add profiles recycler view
                        endlessScrollProfilesManager.populateDataProfile(profiles);
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return root;
    }

    public static ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public static void setRecipes(ArrayList<Recipe> searchedRecipes2) {
        SearchFragment.recipes = searchedRecipes2;
    }

    public static void setProfiles(ArrayList<User> searchedProfiles2) {
        SearchFragment.profiles = searchedProfiles2;
    }

    public static void setTags(ArrayList<Tag> searchedTags2) {
        SearchFragment.searchedTags = searchedTags2;
    }

}