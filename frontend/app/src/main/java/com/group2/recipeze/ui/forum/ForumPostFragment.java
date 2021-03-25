package com.group2.recipeze.ui.forum;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.model.Comment;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.ui.addRecipe.IngredientsListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class ForumPostFragment extends Fragment {

    private Integer postId;
    private ForumRepository forumRepository;
    private Fragment thisFragment;
    private MutableLiveData<ForumPost> post = new MutableLiveData<>();
    private RecyclerView commentsRecyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_post, container, false);
        thisFragment = this;
        postId = getArguments().getInt("postId");
        System.out.println(postId);
        forumRepository = ForumRepository.getInstance();



        post.observeForever(new Observer<ForumPost>() {
            @Override
            public void onChanged(ForumPost forumPost) {
                showForumPost(forumPost, root);
            }
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        forumRepository.readForumPost(postId, post);
    }

    public void showForumPost(ForumPost forumPost, View root) {

        System.out.println(forumPost.getBody());
        final ImageButton like = root.findViewById(R.id.like);
        final ImageButton backButton = root.findViewById(R.id.back);
        TextView postTitle = root.findViewById(R.id.postTitle);
        TextView postBody = root.findViewById(R.id.postBody);
        Button addCommentBtn = root.findViewById(R.id.button_add_comment);
        commentsRecyclerView = root.findViewById(R.id.commentList);

        postTitle.setText(forumPost.getTitle());
        postBody.setText(forumPost.getBody());
        ArrayList<Comment> comments = new ArrayList<>();
        if (forumPost.getComments() != null) {
            comments = new ArrayList<>(Arrays.asList(forumPost.getComments()));
        }
        CommentAdapter commentAdapter = new CommentAdapter(comments);
        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("postId", postId);
                NavHostFragment.findNavController(thisFragment).navigate(R.id.postFragment_to_add_comment, bundle);
            }
        });
    }
}