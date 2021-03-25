package com.group2.recipeze.ui.forum;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;


import java.util.ArrayList;

public class CommentAdapter {
    private ArrayList<ForumPost> commentPosts;
    private Fragment thisFragment;
    private Integer selectedCommentPosition;
    public void setThisFragment(Fragment fragment) {
        thisFragment = fragment;
    }
    // Use this when editing forum post. Don't need to implement just yet
    public static class CommentDialog extends AlertDialog {
        AlertDialog dialogBuilder;
        public CommentDialog(View view, IngredientsListAdapter ingredientsListAdapter) {
            super(view.getContext());
            dialogBuilder = new AlertDialog.Builder(view.getContext()).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ingredient, null);
            EditText username = dialogView.findViewById(R.id.username_comment);
            EditText body = dialogView.findViewById(R.id.body_comment);


            dialogBuilder.setView(dialogView);
        }
        public void show() {
            dialogBuilder.show();
        }
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;

        private final TextView body;
        private int position;
        private CommentAdapter parent;

        public ViewHolder(@NonNull View view, CommentAdapter parent) {
            super(view);
            this.parent = parent;
            username = view.findViewById(R.id.username_comment);

            body = view.findViewById(R.id.body_comment);


        }

        public TextView getUsername() {
            return this.username;
        }


        public TextView getBody() {
            return this.body;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }




    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment, viewGroup, false);

        return new CommentAdapter.ViewHolder(view, this);
    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param commentPosts String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CommentAdapter(ArrayList<ForumPost> commentPosts) {
        this.commentPosts = commentPosts;
    }



    // Replace the contents of a view (invoked by the layout manager)

    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (viewHolder != null && this.commentPosts.get(position) != null) {
            viewHolder.setPosition(position);
            viewHolder.getUsername().setText(this.commentPosts.get(position).getUsername());

            viewHolder.getBody().setText(this.commentPosts.get(position).getBody());
        }

    }


    // Return the size of your dataset (invoked by the layout manager)

    public int getItemCount() {
        return commentPosts.size();
    }

    public ArrayList<ForumPost> getCommentPosts() {
        return commentPosts;
    }
}
