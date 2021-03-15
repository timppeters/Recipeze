package com.group2.recipeze;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.group2.recipeze.data.model.Recipe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ArrayList<Recipe> recipeList;
    List<String> mItemList;

    public RecyclerViewClickListener listener;

    public RecyclerViewAdapter(List<String> itemList, RecyclerViewClickListener listener) {
        mItemList = itemList;
        this.listener = listener;
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


    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTxt;
        TextView descriptionTxt;
        TextView likesTxt;
        TextView comments;
        TextView timeTxt;
        RatingBar rating;
        ImageView image;
        TextView tag1;
        TextView tag2;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //tvItem = itemView.findViewById(R.id.recipe_title3);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
            titleTxt = itemView.findViewById(R.id.recipe_title3);
            descriptionTxt = itemView.findViewById(R.id.description3);
            likesTxt = itemView.findViewById(R.id.likes3);
            comments = itemView.findViewById(R.id.comments3);
            timeTxt = itemView.findViewById(R.id.time3);
            rating = itemView.findViewById(R.id.ratingBar3);
            image = itemView.findViewById(R.id.recipeIamge);
            tag1 = itemView.findViewById(R.id.tag4);
            tag2 = itemView.findViewById(R.id.tag5);
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
        position = 0;
        viewHolder.titleTxt.setText(recipeList.get(position).getTitle());
        viewHolder.descriptionTxt.setText(recipeList.get(position).getDescription());
        viewHolder.likesTxt.setText(String.valueOf(recipeList.get(position).getLikes()));
        viewHolder.comments.setText(String.valueOf(position)); //comments not added yet, using position instead
        Integer time = recipeList.get(position).getPrepTime() + recipeList.get(position).getCookTime();
        viewHolder.timeTxt.setText(time.toString());
        viewHolder.rating.setRating(recipeList.get(position).getRating());
        viewHolder.image.setImageBitmap(recipeList.get(position).getImagesAsBitmaps().get(0));

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

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

}
