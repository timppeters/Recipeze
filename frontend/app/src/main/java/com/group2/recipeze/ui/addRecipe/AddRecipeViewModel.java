package com.group2.recipeze.ui.addRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * AddRecipeViewModel.
 */
public class AddRecipeViewModel extends ViewModel {

    private MutableLiveData<String> meText;

    public AddRecipeViewModel() {
        meText = new MutableLiveData<>();
        meText.setValue("This is addRecipe fragment");
    }

    public LiveData<String> getText() {
        return meText;
    }
}