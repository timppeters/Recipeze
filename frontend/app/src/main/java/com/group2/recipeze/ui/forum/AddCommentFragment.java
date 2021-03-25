package com.group2.recipeze.ui.forum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.ui.forum.ViewPostFragment;
import com.group2.recipeze.ui.forum.ViewPostView;

public class AddCommentFragment extends Fragment {
    ForumRepository forumRepository;
    AddCommentView addCommentView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AddCommentFragment thisFragment = this;
      addCommentView =
                new ViewModelProvider(this).get(AddCommentView.class);
        forumRepository = ForumRepository.getInstance();
        android.view.View root = inflater.inflate(R.layout.add_comment, container, false);
        EditText body = root.findViewById(R.id.body_comment);
        Button post = root.findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumRepository.addCommentToPost(1,body.getText().toString());
            }
        });
                return root;
    }
}
