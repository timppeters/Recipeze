package com.group2.recipeze.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.TagRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import java.util.ArrayList;

/**
 * ExploreFragment.
 */
public class ExploreFragment extends Fragment {
    RecyclerView recipesRecyclerView;
    RecyclerView tagsRecyclerView;
    endlessScroll endlessScrollManager;
    RecipeRepository recipeRepository;
    TagRepository tagRepository;
    com.cooltechworks.views.shimmer.ShimmerRecyclerView shimmerRecyclerView;
    private MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>();

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                recipesRecyclerView = root.findViewById(R.id.exploreRecipes);


                endlessScrollManager = new endlessScroll(recipesRecyclerView, "explore");
                endlessScrollManager.populateData(recipes);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
            }
        });

        tagRepository = TagRepository.getInstance();
        tags.observe(getViewLifecycleOwner(), new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                tagsRecyclerView = root.findViewById(R.id.trendingTagsList);
                TrendingTagsListAdapter tagsListAdapter = new TrendingTagsListAdapter(tags, thisFragment);
                tagsRecyclerView.setAdapter(tagsListAdapter);
                tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        tagRepository.getTop5Tags(tags);
    }
}