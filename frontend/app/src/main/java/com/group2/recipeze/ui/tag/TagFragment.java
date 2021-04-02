package com.group2.recipeze.ui.tag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;

import java.util.ArrayList;

public class TagFragment extends Fragment {

    private String tagName;
    private RecyclerView tagRecyclerView;
    private RecyclerViewAdapter tagRecyclerViewAdapter;
    private endlessScroll endlessScrollManager;
    private RecipeRepository recipeRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tag, container, false);
        TagFragment thisFragment = this;

        recipeRepository = RecipeRepository.getInstance();

        recipes.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                // Populate endlessScroll with recipes
                tagRecyclerView = root.findViewById(R.id.recipes);
                endlessScrollManager = new endlessScroll(tagRecyclerView);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
                endlessScrollManager.populateData(recipes);
            }
        });

        // Get tagName from whoever called fragment
        tagName = getArguments().getString("tagName");

        TextView tagTitle = root.findViewById(R.id.tagTitle);
        tagTitle.setText(tagName + " Tag");

        Button goToForum = root.findViewById(R.id.goToForum);
        ImageButton backButton = root.findViewById(R.id.back_forum2);

        goToForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tagName", tagName);
                NavHostFragment.findNavController(thisFragment).navigate(R.id.action_fragment_tag_to_foodForumFragment, bundle);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        recipeRepository.getRecipesForTag(tagName, 0, recipes);
    }

}