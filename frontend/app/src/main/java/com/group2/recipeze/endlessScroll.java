package com.group2.recipeze;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.RecipeRepository;
import com.group2.recipeze.data.model.Comment;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.ui.forum.ForumPostAdapter;
import java.util.ArrayList;
import java.util.HashMap;
public class endlessScroll {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    RecipeRepository recipeRepository;
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Comment>> comment = new MutableLiveData<>();
    ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    ArrayList<Comment> commentlist = new ArrayList<>();
    ForumRepository forumRepository;
    public MutableLiveData<ArrayList<ForumPost>> resultingPosts = new MutableLiveData<>();
    ArrayList<ForumPost> forumPostsList = new ArrayList<ForumPost>();

    boolean isLoading = false;
    public endlessScroll(RecyclerView recycler) {
        recyclerView = recycler;
    }
    public void populateData(ArrayList<Recipe> initialRecipes) {
        recipeList = initialRecipes;
        recipes.setValue(recipeList);
        //recipes.postValue(recipeList);
        for(int x = 0; x < recipeList.size(); x++){
            Log.d("recipe1", recipeList.get(x).getTitle());
        }
    }
    public void populateData2(ArrayList<Comment> commentList) {
        commentlist = commentList;
        comment.setValue(commentlist);
        //recipes.postValue(recipeList);
        for(int x = 0; x < commentlist.size(); x++){
            Log.d("recipe1", commentlist.get(x).getBody());
        }
    }
    public void addFakeRecipe(String title, int amount) {
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("Tomatoes");
        ArrayList<String> ingredientsAmounts = new ArrayList<String>();
        ingredientsAmounts.add("200g");
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        Recipe exampleRecipe = new Recipe(1,
                4.5f,
                123,
                "Alf",
                title,
                "Recipe Description",
                ingredients,
                ingredientsAmounts,
                new HashMap<Integer, String>(),
                new HashMap<Integer, String>(),
                tags,
                15,
                90,
                Boolean.FALSE
        );
        for(int i = 0; i < amount; i++) {
            recipeList.add(exampleRecipe);
        }
        recipes.setValue(recipeList);
    }
    public void initAdapter(Fragment fragmment) {
        recyclerViewAdapter = new RecyclerViewAdapter(recipeList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmment.getContext()));
        recyclerViewAdapter.setThisFragment(fragmment);
        recipeRepository = RecipeRepository.getInstance();
        recipes.observeForever(new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                recipeList.addAll(recipes);
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
        /**forumRepository = ForumRepository.getInstance();
         resultingPosts.observeForever(new Observer<ArrayList<ForumPost>>() {
        @Override
        public void onChanged(ArrayList<ForumPost> resultingPosts) {
        // Populate endlessScroll with forums
        forumPostsList.addAll(resultingPosts);
        recyclerViewAdapter.notifyDataSetChanged();
        isLoading = false;
        }
        });
         */
    }
    public void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == recipeList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
    public void loadMore() {
        recipeList.add(null);
        recyclerViewAdapter.notifyItemInserted(recipeList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeList.remove(recipeList.size() - 1);
                int scrollPosition = recipeList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                ArrayList<String> ingredients = new ArrayList<String>();
                ArrayList<String> tags = new ArrayList<String>();
                recipeRepository.getRecipesForFeedByUsers(1000, ingredients, 1000, tags, "likes", recipeList.size(), recipes);
            }
        }, 10000);

    }
}