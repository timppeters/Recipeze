package com.group2.recipeze.ui.recipeBook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * RecipeBookViewModel.
 */
public class RecipeBookViewModel extends ViewModel {

    private MutableLiveData<String> meText;
    private MutableLiveData<String> buttonText;

    public RecipeBookViewModel() {
        meText = new MutableLiveData<>();
        meText.setValue("This is recipeBook fragment");

        buttonText = new MutableLiveData<>();
        buttonText.setValue("Recipe x");
    }

    public LiveData<String> getMeText() {
        return meText;
    }

    public LiveData<String> getButtonText() { return buttonText; }
}
