package com.group2.recipeze.ui.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * FeedViewModel.
 */
public class FeedViewModel extends ViewModel {

    private MutableLiveData<String> meText;

    public FeedViewModel() {
        meText = new MutableLiveData<>();
        meText.setValue("This is feed fragment");
    }

    public LiveData<String> getText() {
        return meText;
    }
}