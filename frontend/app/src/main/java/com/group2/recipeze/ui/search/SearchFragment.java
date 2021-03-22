package com.group2.recipeze.ui.search;

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
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.feed.FeedViewModel;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;
/**
 * SearchFragment.
 */
public class SearchFragment extends Fragment {
    RecyclerView searchRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository repo;
    SearchRepository searchRepo;
    private MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    private static MutableLiveData<Map<String, ArrayList<?>>> searchedRecipes = new MutableLiveData<>();
    private static MutableLiveData< ArrayList<Recipe>> searchedRecipes2 = new MutableLiveData<>();


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
        Fragment thisFrag = this;

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRepo = SearchRepository.getInstance();
                MutableLiveData<Map<String, ArrayList<?>>> searchRecipes = new MutableLiveData<>();
                searchRepo.search(query, searchRecipes, searchedRecipes2);
                Toast.makeText(getActivity(), "search completed", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

      //  repo.getRecipesForFeedByUsers(maxTime, ingredientList, ingNum, tags, "likes", 0, searchedRecipes2);

        searchedRecipes2.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                searchRecyclerView = root.findViewById(R.id.searchRecipesScroll);
                endlessScrollManager = new endlessScroll(searchRecyclerView);
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
            }
        });
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request

        /* TODO I wasn't sure what was meant to be here so just did this for now
           It doesn't seem to search by tag or filter so just left like this
         */
        repo.getRecipesForFeedByUsers(maxTime, ingredientList, ingNum, tags, "likes", 0, recipes);
    }

    public static void setSearchedRecipes2(MutableLiveData<ArrayList<Recipe>> searchedRecipes2) {
        SearchFragment.searchedRecipes2 = searchedRecipes2;
    }

    public static void setSearchedRecipes(MutableLiveData<Map<String, ArrayList<?>>> searchedRecipes) {
        SearchFragment.searchedRecipes = searchedRecipes;
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