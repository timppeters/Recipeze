package com.group2.recipeze.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.endlessScroll;

import java.util.ArrayList;

/**
 * FeedFragment.
 */
public class FeedFragment extends Fragment {
    RecyclerView feedRecyclerView;
    endlessScroll endlessScrollManager;

    private FeedViewModel feedViewModel;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feedViewModel =
                new ViewModelProvider(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        feedRecyclerView = root.findViewById(R.id.feedRecipes);
        feedRecyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<>()));
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        endlessScrollManager = new endlessScroll(feedRecyclerView);
        endlessScrollManager.populateData();
        endlessScrollManager.initAdapter();
        endlessScrollManager.initScrollListener();



        return root;
    }
}