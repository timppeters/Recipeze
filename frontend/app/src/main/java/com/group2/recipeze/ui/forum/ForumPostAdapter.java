package com.group2.recipeze.ui.forum;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.recipeze.R;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import java.util.ArrayList;

public class ForumPostAdapter  extends RecyclerView.Adapter<ForumPostAdapter.ViewHolder>{

    private ArrayList<ForumPost> forumPosts;
    private Fragment thisFragment;
    private Integer selectedPostPosition;
    public void setThisFragment(Fragment fragment) {
        thisFragment = fragment;
    }
    // Use this when editing forum post. Don't need to implement just yet
    public static class FoodForumDialog extends AlertDialog {
        AlertDialog dialogBuilder;
        public FoodForumDialog(View view, IngredientsListAdapter ingredientsListAdapter) {
            super(view.getContext());
            dialogBuilder = new AlertDialog.Builder(view.getContext()).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ingredient, null);
            EditText username = dialogView.findViewById(R.id.username);
            EditText title = dialogView.findViewById(R.id.post_title);
            EditText body = dialogView.findViewById(R.id.body);
            ImageView comment = dialogView.findViewById(R.id.comment);
            EditText commentText = dialogView.findViewById(R.id.post_title);

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
        private final TextView title;
        private final TextView body;
        private int position;
        private ForumPostAdapter parent;

        public ViewHolder(@NonNull View view, ForumPostAdapter parent) {
            super(view);
            this.parent = parent;
            username = view.findViewById(R.id.username);
            title = view.findViewById(R.id.post_title);
            body = view.findViewById(R.id.body);
        }

        public TextView getUsername() {
            return this.username;
        }

        public TextView getTitle() {
            return this.title;
        }

        public TextView getBody() {
            return this.body;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void ItemView(@NonNull View itemView) {

            Button button = itemView.findViewById(R.id.invisibleLayer);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectedPostPosition = position;
                    int postId = forumPosts.get(selectedPostPosition).getPostId();

                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", postId);
                    FragmentManager fragmentManager = thisFragment.getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.addToBackStack("post " + String.valueOf(postId));
                    transaction.add(R.id.nav_host_fragment, FoodForumFragment.class, bundle);
                    transaction.commit();
                }
            });
        }
    }




    public ForumPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_foodforum, viewGroup, false);

        return new ForumPostAdapter.ViewHolder(view, this);
    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param forumPosts String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ForumPostAdapter(ArrayList<ForumPost> forumPosts) {
        this.forumPosts = forumPosts;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ForumPostAdapter.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (viewHolder != null && this.forumPosts.get(position) != null) {
            viewHolder.setPosition(position);
            viewHolder.getUsername().setText(this.forumPosts.get(position).getUsername());
            viewHolder.getTitle().setText(this.forumPosts.get(position).getTitle());
            viewHolder.getBody().setText(this.forumPosts.get(position).getBody());
        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return forumPosts.size();
    }

    public ArrayList<ForumPost> getforumPosts() {
        return forumPosts;
    }

}