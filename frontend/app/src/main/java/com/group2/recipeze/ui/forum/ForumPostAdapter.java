package com.group2.recipeze.ui.forum;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.recipeze.R;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;
import java.util.ArrayList;
public class ForumPostAdapter  extends RecyclerView.Adapter<ForumPostAdapter.ViewHolder>{
    private ArrayList<ForumPost> forumPosts;
    public static class FoodForumDialog extends AlertDialog {
        AlertDialog dialogBuilder;
        public FoodForumDialog(View view, IngredientsListAdapter ingredientsListAdapter) {
            super(view.getContext());
            dialogBuilder = new AlertDialog.Builder(view.getContext()).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ingredient, null);
            EditText username = dialogView.findViewById(R.id.username);
            EditText title = dialogView.findViewById(R.id.forum_title);
            EditText body = dialogView.findViewById(R.id.body);
            ImageView comment = dialogView.findViewById(R.id.comment);
            EditText commentText = dialogView.findViewById(R.id.forum_title);

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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cross;
        private final TextView ingredient;
        private int position;
        private ForumPostAdapter  parent;
        public ViewHolder(View view, ForumPostAdapter parent) {
            super(view);
            this.parent = parent;
            ingredient = view.findViewById(R.id.ingredient);
            cross = view.findViewById(R.id.cross);
            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.removeItem(position);
                }
            });
        }
        public TextView getForumPost() {return this.ingredient;}
        public void setPosition(int position) {
            this.position = position;
        }
    }
    public ForumPostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
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
    public void ForumPostAdapter(ArrayList<ForumPost> forumPosts) {
        this.forumPosts = forumPosts;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ForumPostAdapter.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setIsRecyclable(false);
        viewHolder.setPosition(position);
        viewHolder.getForumPost().setText((CharSequence) this.forumPosts.get(position));
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return forumPosts.size();
    }

    public void addItem(String amount, ForumPost foodforum) {
        forumPosts.add(foodforum);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        forumPosts.remove(position);
        notifyDataSetChanged();
    }
    public ArrayList<ForumPost> getforumPosts() {
        return forumPosts;
    }

}