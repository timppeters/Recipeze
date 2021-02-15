package com.group2.recipeze.ui.recipeBook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * RecipeBookViewModel.
 */
public class RecipeBookViewModel extends ViewModel {

    private MutableLiveData<String> meText;

    public RecipeBookViewModel() {
        meText = new MutableLiveData<>();
        meText.setValue("This is recipeBook fragment");
    }

    public LiveData<String> getText() {
        return meText;
    }
}
