package com.group2.recipeze.ui.forum;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.model.Comment;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.recipe.RecipeFragment;

import java.util.ArrayList;

public class ViewPostFragment extends Fragment {
    ViewPostView viewPostView;
    ForumRepository forumRepository;
    RecyclerView postRecyclerView;
    endlessScroll endlessScrollManager;
    public MutableLiveData<ArrayList<Comment>>commentPosts = new MutableLiveData<>();
    private ViewPostFragment thisFragment = this;
    private MutableLiveData<ForumPost> forumPost = new MutableLiveData<ForumPost>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewPostFragment thisFragment = this;
        viewPostView =
                new ViewModelProvider(this).get(ViewPostView.class);
        forumRepository = ForumRepository.getInstance();
        android.view.View root = inflater.inflate(R.layout.fragment_post, container, false);
        Integer postId = getArguments().getInt("postId");
        Integer commentId = getArguments().getInt("commentId");
        EditText body = root.findViewById(R.id.postBody);
        Button add_comment = root.findViewById(R.id.button_add_comment);
        Button delete_comment = root.findViewById(R.id.button_delete_comment);

        forumPost.observe(getViewLifecycleOwner(), new Observer<ForumPost>() {
            @Override
            public void onChanged(ForumPost forumPost) {
                showForumPost(forumPost, root);
            }
        });

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(thisFragment).navigate(R.id.postFragment_to_add_comment);
                //forumRepository.addCommentToPost(1,body.getText().toString());
            }

        });
        delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumRepository.deleteCommentFromPost(1);
            }
        });

        commentPosts.observe(getViewLifecycleOwner(), new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                // Populate endlessScroll with forums
                postRecyclerView = root.findViewById(R.id.commentList);
                endlessScrollManager = new endlessScroll(postRecyclerView);
                endlessScrollManager.populateData2(comments);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();
            }
        });

        return root;
    }

    public void showForumPost(ForumPost forumPost, View root) {

        final ImageButton like = root.findViewById(R.id.like);
        final ImageButton backButton = root.findViewById(R.id.back);

        if (forumPost.getLiked()) {
            like.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_filled,null));
            like.setTag("liked");
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If button in non-liked state
                if (((String)like.getTag()).equals("unliked")) {
                    forumRepository.likePost(forumPost.getPostId());
                    like.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_filled,null));
                    like.setTag("liked");
                } else {
                    forumRepository.unlikePost(forumPost.getPostId());
                    like.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like__1_,null));
                    like.setTag("unliked");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }*/
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });
    }
}

