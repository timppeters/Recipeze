package com.group2.recipeze.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


    public class FoodPreferenceViewModel extends ViewModel {

        private MutableLiveData<String> meText;

        public FoodPreferenceViewModel() {
            meText = new MutableLiveData<>();
            meText.setValue("This is food preference fragment");
        }

        public LiveData<String> getText() {
            return meText;
        }
    }
