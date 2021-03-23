package com.group2.recipeze.ui.forum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodForumViewModel extends ViewModel {
    // TODO: Implement the
    private MutableLiveData<String> meText;

    public FoodForumViewModel() {
        meText = new MutableLiveData<>();
        meText.setValue("This is food forum fragment");
    }

    public LiveData<String> getText() {
        return meText;
    }
}