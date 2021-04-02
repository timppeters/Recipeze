package com.group2.recipeze.ui.forum;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import java.util.ArrayList;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ViewHolder>{

    private ArrayList<ForumPost> forumPosts;
    private Fragment thisFragment;
    private Integer selectedPostPosition;
    private ForumRepository forumRepository;
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
        private boolean showDelete = false;
        private int position;
        private ForumPostAdapter parent;
        private ImageButton menu;

        public ViewHolder(@NonNull View view, ForumPostAdapter parent) {
            super(view);
            this.parent = parent;
            username = view.findViewById(R.id.username);
            title = view.findViewById(R.id.post_title);
            body = view.findViewById(R.id.body);

            TextView invisibleLayer = itemView.findViewById(R.id.invisibleLayer);

            invisibleLayer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectedPostPosition = position;
                    int postId = forumPosts.get(selectedPostPosition).getPostId();

                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", postId);
                    NavHostFragment.findNavController(thisFragment).navigate(R.id.action_foodForumFragment_to_forumpost, bundle);
                }
            });
            menu = view.findViewById(R.id.menu);


            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.threedotmenu_actions, popup.getMenu());
                    if (!showDelete) {
                        popup.getMenu().removeItem(R.id.menu_delete);
                    }
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_delete:
                                    parent.removeItem(position);
                                    return true;
                                case R.id.menu_report:
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });
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

        public void setShowDelete(boolean showDelete) {
            this.showDelete = showDelete;
        }

        public void setPosition(int position) {
            this.position = position;
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
        forumRepository = ForumRepository.getInstance();
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ForumPostAdapter.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (viewHolder != null && this.forumPosts.get(position) != null) {
            viewHolder.setPosition(position);
            viewHolder.getUsername().setText(this.forumPosts.get(position).getAuthor());
            viewHolder.getTitle().setText(this.forumPosts.get(position).getTitle());
            viewHolder.getBody().setText(this.forumPosts.get(position).getBody());
        }
        LoginRepository loginRepository = LoginRepository.getInstance();
        if (loginRepository.getUser().getUsername().equals(this.forumPosts.get(position).getAuthor())) {
            viewHolder.setShowDelete(true);
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
    public void removeItem(int position) {
        forumRepository.deletePost(forumPosts.get(position).getPostId());
        forumPosts.remove(position);
        notifyDataSetChanged();
    }

}