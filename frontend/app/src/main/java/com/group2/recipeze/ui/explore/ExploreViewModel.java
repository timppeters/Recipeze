package com.group2.recipeze.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ExploreViewModel.
 */
public class ExploreViewModel extends ViewModel {

    private MutableLiveData<String> meText;

    public ExploreViewModel() {
        meText = new MutableLiveData<>();
        meText.setValue("This is explore fragment");
    }

    public LiveData<String> getText() {
        return meText;
    }
}