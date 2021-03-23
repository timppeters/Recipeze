package com.group2.recipeze.ui.forum;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.model.ForumPost;

import java.util.ArrayList;

public class endlessScrollForum {
    RecyclerView recyclerView;
    ForumPostAdapter recyclerViewAdapter;
    private String tagName;

    ForumRepository forumRepository;
    public MutableLiveData<ArrayList<ForumPost>> resultingPosts = new MutableLiveData<>();
    ArrayList<ForumPost> forumPostsList = new ArrayList<ForumPost>();


    boolean isLoading = false;

    public endlessScrollForum(RecyclerView recycler, String tagName) {
        recyclerView = recycler;
        tagName = tagName;
    }


    public void populateData(ArrayList<ForumPost> initialForum) {
        forumPostsList = initialForum;
        resultingPosts.setValue(forumPostsList);
        //recipes.postValue(recipeList);
        for(int x = 0; x < forumPostsList.size(); x++){
            Log.d("forums1", forumPostsList.get(x).getTitle());
        }
    }

    public void initAdapter(Fragment fragmment) {
        recyclerViewAdapter = new ForumPostAdapter(forumPostsList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmment.getContext()));
        recyclerViewAdapter.setThisFragment(fragmment);


        forumRepository = ForumRepository.getInstance();
        resultingPosts.observeForever(new Observer<ArrayList<ForumPost>>() {

            @Override
            public void onChanged(ArrayList<ForumPost> resultingPosts) {
                // Populate endlessScroll with forums
                forumPostsList.addAll(resultingPosts);
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == forumPostsList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void loadMore() {
        forumPostsList.add(null);
        recyclerViewAdapter.notifyItemInserted(forumPostsList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                forumPostsList.remove(forumPostsList.size() - 1);
                int scrollPosition = forumPostsList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);

                forumRepository.getForumPostsInTag(tagName, resultingPosts);
            }
        }, 10000);


    }
}