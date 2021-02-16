package com.group2.recipeze.ui.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Recipe view model
 */
public class RecipeViewModel extends ViewModel {
    private MutableLiveData<String> prepTime;
    private MutableLiveData<String> cookTime;

    public RecipeViewModel() {
        prepTime = new MutableLiveData<>();
        prepTime.setValue("5 mins");

        cookTime = new MutableLiveData<>();
        cookTime.setValue("20 mins");
    }

    /*
    I figure at some point we will have to make this get the stuff from
    the database.
     */

    public LiveData<String> getPrepTime() {
        return prepTime;
    }

    public LiveData<String> getCookTime() {
        return cookTime;
    }
}