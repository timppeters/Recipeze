package com.group2.recipeze.ui.forum;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.model.Comment;
import com.group2.recipeze.data.model.ForumPost;


import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<Comment> comments;
    private Fragment thisFragment;
    private Integer selectedCommentPosition;
    private ForumRepository forumRepository;

    public void setThisFragment(Fragment fragment) {
        thisFragment = fragment;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final TextView body;
        private Integer commentId = 0;
        private int position;
        private CommentAdapter parent;
        private ImageButton menu;
        private boolean showDelete = false;

        public ViewHolder(@NonNull View view, CommentAdapter parent) {
            super(view);
            this.parent = parent;
            username = view.findViewById(R.id.username_comment);
            body = view.findViewById(R.id.body_comment);
            menu = view.findViewById(R.id.menu);


            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.comment_actions, popup.getMenu());
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

        public TextView getBody() {
            return this.body;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setShowDelete(boolean showDelete) {
            this.showDelete = showDelete;
        }

        public void setCommentId(Integer commentId) { this.commentId = commentId;}
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
     * @param comments String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
        forumRepository = ForumRepository.getInstance();
    }



    // Replace the contents of a view (invoked by the layout manager)

    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (viewHolder != null && this.comments.get(position) != null) {
            viewHolder.setPosition(position);
            viewHolder.getUsername().setText(this.comments.get(position).getAuthor());
            viewHolder.getBody().setText(this.comments.get(position).getBody());
            viewHolder.setCommentId(this.comments.get(position).getCommentId());

            LoginRepository loginRepository = LoginRepository.getInstance();
            if (loginRepository.getUser().getUsername().equals(this.comments.get(position).getAuthor())) {
                viewHolder.setShowDelete(true);
            }
        }

    }


    // Return the size of your dataset (invoked by the layout manager)

    public int getItemCount() {
        return comments.size();
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void removeItem(int position) {
        forumRepository.deleteCommentFromPost(comments.get(position).getCommentId());
        comments.remove(position);
        notifyDataSetChanged();
    }
}
