package com.group2.recipeze.ui.forum;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.model.ForumPost;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ForumPostFragment extends Fragment {

    private Integer postId;
    private ForumRepository forumRepository;
    public MutableLiveData<ForumPost> post = new MutableLiveData<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_post, container, false);

        postId = getArguments().getInt("postId");
        forumRepository = ForumRepository.getInstance();

        TextView postTitle = root.findViewById(R.id.postTitle);
        TextView postBody = root.findViewById(R.id.postBody);

        post.observeForever(new Observer<ForumPost>() {
            @Override
            public void onChanged(ForumPost forumPost) {
                postTitle.setText(forumPost.getTitle());
                postBody.setText(forumPost.getBody());
            }
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        forumRepository.readForumPost(postId, post);
    }
}