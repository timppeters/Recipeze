package com.group2.recipeze.ui.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;

import java.util.ArrayList;

public class RecipeIngredientsListAdapter extends RecyclerView.Adapter<RecipeIngredientsListAdapter.ViewHolder> {

    private ArrayList<String> ingredients;
    private ArrayList<String> ingredientsAmounts;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredient;

        public ViewHolder(View view) {
            super(view);
            ingredient = view.findViewById(R.id.ingredient);
        }

        public TextView getIngredient() {return this.ingredient;}

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param ingredients String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public RecipeIngredientsListAdapter(ArrayList<String> ingredients, ArrayList<String> ingredientsAmounts) {
        this.ingredients = ingredients;
        this.ingredientsAmounts = ingredientsAmounts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeIngredientsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_ingredient_recipe, viewGroup, false);


        return new RecipeIngredientsListAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecipeIngredientsListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getIngredient().setText(this.ingredientsAmounts.get(position) + " " + this.ingredients.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ingredients.size();
    }


}