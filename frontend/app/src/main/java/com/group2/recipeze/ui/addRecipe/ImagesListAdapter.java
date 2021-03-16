package com.group2.recipeze.ui.addRecipe;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

public class ImagesListAdapter extends RecyclerView.Adapter<ImagesListAdapter.ViewHolder> {

    private ArrayList<Bitmap> images;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView cross;
        private int position;
        private ImagesListAdapter parent;

        public ViewHolder(View view, ImagesListAdapter parent) {
            super(view);
            this.parent = parent;
            image = view.findViewById(R.id.image);
            cross = view.findViewById(R.id.cross);

            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.removeItem(position);
                }
            });
        }

        public ImageView getImage() {
            return this.image;
        }

        public void setPosition(int position) {
            this.position = position;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param images String[] containing the data to populate views to be used
     *                    by RecyclerView.
     */
    public ImagesListAdapter(ArrayList<Bitmap> images) {
        this.images = images;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImagesListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_picked_image, viewGroup, false);


        return new ImagesListAdapter.ViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ImagesListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setIsRecyclable(false);
        viewHolder.setPosition(position);
        viewHolder.getImage().setImageBitmap(images.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return images.size();
    }

    public void populate(ArrayList<Bitmap> bitmaps) {
        images.clear();
        images.addAll(bitmaps);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        images.remove(position);
        notifyDataSetChanged();
    }

    public HashMap<String, String> getImagesForRecipeSave() {
        HashMap<String, Bitmap> result = new HashMap<>();
        for (int i = 0; i < images.size(); i++) {
            result.put(String.valueOf(i+1), images.get(i));
        }
        HashMap<String, String> b64 = Recipe.convertBitmapsToBase64(result);
        return b64;
    }
}