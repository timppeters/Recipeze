package com.group2.recipeze.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.group2.recipeze.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * ExploreFragment.
 */
public class ExploreFragment extends Fragment {
    RecyclerView recyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    SearchRepository searchRepo;
    
    
    private ExploreViewModel exploreViewModel;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        ExploreFragment thisFragment = this;

    
        recipeRepository = RecipeRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                recyclerView = root.findViewById(R.id.exploreRecipes);

                endlessScrollManager = new endlessScroll(recyclerView);
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
            }
        });

        SearchView search = root.findViewById(R.id.searchExplorePage);
        Fragment thisFrag = this;

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRepo = SearchRepository.getInstance();
                MutableLiveData<Map<String, ArrayList<?>>> searchRecipes = new MutableLiveData<>();
                MutableLiveData< ArrayList<Recipe>> searchedRecipes2 = new MutableLiveData<>();
                searchRepo.search(query, searchRecipes, searchedRecipes2);
                SearchFragment.setSearchedRecipes2(searchedRecipes2);
                NavHostFragment.findNavController(thisFrag).navigate(R.id.action_navigation_explore_to_searchFragment);
                Toast.makeText(getActivity(), "search completed", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Just an example request
        recipeRepository.getRecipesForExplore(0, recipes);
    }
}