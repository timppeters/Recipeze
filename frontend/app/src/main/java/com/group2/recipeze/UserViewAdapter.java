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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.group2.recipeze.data.model.User;
import com.group2.recipeze.ui.*;

import java.io.InputStream;
import java.util.ArrayList;

public class UserViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ArrayList<User> userList;
    public Fragment thisFragment;
    private Integer selectedUserPosition;


    public UserViewAdapter(ArrayList<User> itemList) {
        userList = itemList;
    }

    public void setThisFragment(Fragment fragment) {
        thisFragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
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
        return userList == null ? 0 : userList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return userList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        TextView bioText;
        Button followBut;
        TextView invisibleLayer;
        Integer position;
        ImageView profilePic;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.user_username);
            bioText = itemView.findViewById(R.id.user_bio);
            followBut = itemView.findViewById(R.id.follow_button);
            invisibleLayer = itemView.findViewById(R.id.invisLayer);
            profilePic = itemView.findViewById(R.id.profile_image);
            

            invisibleLayer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("CLICK-User", "CLICKED");
                    bioText.setText(bioText.getText() + " SELECTED! ");
                    selectedUserPosition = position;

                    String usernameID = userList.get(selectedUserPosition).getUsername();

                    Bundle bundle = new Bundle();
                    bundle.putString("usernameID", usernameID);
                    NavHostFragment.findNavController(thisFragment).navigate(R.id.action_searchFragment_to_navigation_profile, bundle);

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
        String title = userList.get(position).getUsername();
        int limit = 27;
        if (title.length() > limit) {
            title = title.substring(0, limit) + "...";
        }
        viewHolder.position = position;
        viewHolder.usernameText.setText(title);
        viewHolder.bioText.setText(userList.get(position).getBio());
        //new DownloadImageTask((ImageView) viewHolder.image).execute(userList.get(position).getImages().get(1));
        //viewHolder.image.setImageBitmap(userList.get(position).getImagesAsBitmaps().get(0));
        
    }

    public User getSelected() {
        if (selectedUserPosition != null) {
            return userList.get(selectedUserPosition);
        }
        return null;
    }

}

