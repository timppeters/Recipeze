package com.group2.recipeze.ui.forum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewPostView extends ViewModel {
    private MutableLiveData<String> meText;

    public ViewPostView() {
        meText = new MutableLiveData<>();
        meText.setValue("This is food preference fragment");
    }

    public LiveData<String> getText() {
        return meText;
    }
}
