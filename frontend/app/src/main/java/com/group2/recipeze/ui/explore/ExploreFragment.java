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
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;

import java.util.ArrayList;

/**
 * ExploreFragment.
 */
public class ExploreFragment extends Fragment {
    RecyclerView recyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    com.cooltechworks.views.shimmer.ShimmerRecyclerView shimmerRecyclerView;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

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
        shimmerRecyclerView = root.findViewById(R.id.shimmer_recycler_view);
        shimmerRecyclerView.showShimmerAdapter();

    
        recipeRepository = RecipeRepository.getInstance();
        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {

            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                shimmerRecyclerView.hideShimmerAdapter();
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