package com.group2.recipeze.ui.recipeBook;

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
 * RecipeBookFragment.
 */
public class RecipeBookFragment extends Fragment {

    private RecipeBookViewModel recipeBookViewModel;

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
        recipeBookViewModel =
                new ViewModelProvider(this).get(RecipeBookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipebook, container, false);
        final TextView textView = root.findViewById(R.id.text_recipeBook);
        recipeBookViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}