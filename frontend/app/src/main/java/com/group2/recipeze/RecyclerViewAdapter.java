package com.group2.recipeze;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.io.InputStream;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ArrayList<Recipe> recipeList;
    public Fragment thisFragment;
    private Integer selectedRecipePosition;


    public RecyclerViewAdapter(ArrayList<Recipe> itemList) {
        recipeList = itemList;
    }

    public void setThisFragment(Fragment fragment) {
        thisFragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return recipeList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleTxt;
        TextView descriptionTxt;
        TextView likesTxt;
        TextView comments;
        TextView timeTxt;
        RatingBar rating;
        ImageView image;
        TextView tag1;
        TextView tag2;
        Integer position;
        TextView invisibleLayer;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.recipe_title3);
            descriptionTxt = itemView.findViewById(R.id.description3);
            likesTxt = itemView.findViewById(R.id.likes3);
            comments = itemView.findViewById(R.id.comments3);
            timeTxt = itemView.findViewById(R.id.time3);
            rating = itemView.findViewById(R.id.ratingBar3);
            image = itemView.findViewById(R.id.recipeIamge);
            tag1 = itemView.findViewById(R.id.tag4);
            tag2 = itemView.findViewById(R.id.tag5);
            invisibleLayer = itemView.findViewById(R.id.invisLayer);

            invisibleLayer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("CLICK-RECIPE", "CLICKED");
                    descriptionTxt.setText(descriptionTxt.getText() + " SELECTED! ");
                    selectedRecipePosition = position;

                    int recipeId = recipeList.get(selectedRecipePosition).getRecipeId();

                    Bundle bundle = new Bundle();
                    bundle.putInt("recipeId", recipeId);
                    NavHostFragment.findNavController(thisFragment).navigate(R.id.action_navigation_feed_to_recipeFragment, bundle);
                }
            });
        }

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        //populate card with values
        //String item = mItemList.get(position);
        //viewHolder.tvItem.setText(item);
        //position = 0;
        String title = recipeList.get(position).getTitle();
        int limit = 27;
        if (title.length() > limit) {
            title = title.substring(0, limit) + "...";
        }
        viewHolder.position = position;
        viewHolder.titleTxt.setText(title);
        viewHolder.descriptionTxt.setText(recipeList.get(position).getDescription());
        viewHolder.likesTxt.setText(String.valueOf(recipeList.get(position).getLikes()));
        viewHolder.comments.setText(String.valueOf(position)); //comments not added yet, using position instead
        Integer time = recipeList.get(position).getPrepTime() + recipeList.get(position).getCookTime();
        viewHolder.timeTxt.setText(time.toString());
        viewHolder.rating.setRating(recipeList.get(position).getRating());
        viewHolder.image.setImageBitmap(recipeList.get(position).getImagesAsBitmaps().get(1));
        //new DownloadImageTask((ImageView) viewHolder.image).execute(recipeList.get(position).getImages().get(1));
        //viewHolder.image.setImageBitmap(recipeList.get(position).getImagesAsBitmaps().get(0));

        ArrayList<TextView> tagObjects = new ArrayList<>();
        tagObjects.add(viewHolder.tag1);
        tagObjects.add(viewHolder.tag2);
        ArrayList<String> tags = recipeList.get(position).getTags();
        int numberOfTags = Math.min(tags.size(), tagObjects.size());
        for(int x = 0; x < numberOfTags; x++) {
            tagObjects.get(x).setText(tags.get(x));
            tagObjects.get(x).setVisibility(View.VISIBLE);
        }

    }

    public Recipe getSelected() {
        if (selectedRecipePosition != null) {
            return recipeList.get(selectedRecipePosition);
        }
        return null;
    }

}

