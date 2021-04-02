package com.group2.recipeze.ui.explore;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.model.Tag;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import java.util.ArrayList;

public class TrendingTagsListAdapter extends RecyclerView.Adapter<TrendingTagsListAdapter.ViewHolder> {

    private ArrayList<Tag> tags;
    private ExploreFragment thisFragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tagName;
        private final TextView recipesAmount;
        private TrendingTagsListAdapter parent;

        public ViewHolder(View view, TrendingTagsListAdapter parent) {
            super(view);
            this.parent = parent;
            tagName = view.findViewById(R.id.tag);
            recipesAmount = view.findViewById(R.id.recipesNumber);

            tagName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tagName", tagName.getText().toString());
                    NavHostFragment.findNavController(parent.thisFragment).navigate(R.id.action_exploreFragment_to_foodForumFragment, bundle);
                }
            });
        }

        public TextView getTagName() {return this.tagName;}

        public TextView getRecipesAmount() {return this.recipesAmount;}

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param tags String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public TrendingTagsListAdapter(ArrayList<Tag> tags, ExploreFragment thisFragment) {
        this.tags = tags;
        this.thisFragment = thisFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrendingTagsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tag_explore, viewGroup, false);


        return new TrendingTagsListAdapter.ViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TrendingTagsListAdapter.ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setIsRecyclable(false);
        viewHolder.getTagName().setText(this.tags.get(position).getName());
        viewHolder.getRecipesAmount().setText(String.valueOf(this.tags.get(position).getNumberOfRecipes()) + " recipes");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tags.size();
    }

}