package com.group2.recipeze.ui.forum;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group2.recipeze.R;
import com.group2.recipeze.ui.search.SearchViewModel;

public class FoodForumFragment extends Fragment {

    private FoodForumViewModel mViewModel;

    public static FoodForumFragment newInstance() {
        return new FoodForumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(FoodForumViewModel.class);
        View root = inflater.inflate(R.layout.food_forum_fragment, container, false);
        return root;
    }



}