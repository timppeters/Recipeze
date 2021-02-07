package com.group2.recipeze.ui.addRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.group2.recipeze.R;

/**
 *  AddRecipeFragment.
 */
public class AddRecipeFragment extends Fragment {

    private AddRecipeViewModel addRecipeViewModel;

    /**
     * Called when view is created.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addRecipeViewModel =
                new ViewModelProvider(this).get(AddRecipeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addrecipe, container, false);
        final TextView textView = root.findViewById(R.id.text_addRecipe);
        addRecipeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}