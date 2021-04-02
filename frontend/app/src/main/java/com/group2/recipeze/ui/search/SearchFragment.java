package com.group2.recipeze.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.feed.FeedViewModel;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * SearchFragment.
 */
public class SearchFragment extends Fragment {
    RecyclerView searchRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository repo;
    private MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    private static int maxTime = 100;
    private static ArrayList<String> ingredientList;
    private static int ingNum = 100;
    private static ArrayList<String> tags;

    private SearchViewModel searchViewModel;

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
        repo = RecipeRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                searchRecyclerView = root.findViewById(R.id.searchRecipesScroll);
                endlessScrollManager = new endlessScroll(searchRecyclerView, "tags");
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
            }
        });

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

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request

        /* TODO I wasn't sure what was meant to be here so just did this for now
           It doesn't seem to search by tag or filter so just left like this
         */
        ArrayList<String> ingredients = new ArrayList<String>();
        ArrayList<String> tags = new ArrayList<String>();
        repo.getRecipesForFeedByTags(maxTime, ingredients, ingNum, tags, "likes", 0, recipes);
    }


    public static void setIngNum(int ingNum) {
        SearchFragment.ingNum = ingNum;
    }

    public static void setMaxTime(int maxTime) {
        SearchFragment.maxTime = maxTime;
    }

    public static void setTags(ArrayList<String> tags) {
        SearchFragment.tags = tags;
    }

    public static void setIngredientList(ArrayList<String> ingredientList) { SearchFragment.ingredientList = ingredientList; }
}