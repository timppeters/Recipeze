package com.group2.recipeze.ui.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;

import java.util.HashMap;

public class RecipeStepsListAdapter extends RecyclerView.Adapter<RecipeStepsListAdapter.ViewHolder> {

    private HashMap<Integer, String> steps;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView step;

        public ViewHolder(View view) {
            super(view);
            step = view.findViewById(R.id.step);
        }

        public TextView getInstruction() {return this.step;}

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param steps String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public RecipeStepsListAdapter(HashMap<Integer, String> steps) {
        this.steps = steps;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeStepsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_step_recipe, viewGroup, false);


        return new RecipeStepsListAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecipeStepsListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getInstruction().setText(String.valueOf(position+1) + ". " + this.steps.get(position+1));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return steps.size();
    }


}