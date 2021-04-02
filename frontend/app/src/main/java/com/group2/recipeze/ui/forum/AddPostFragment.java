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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.group2.recipeze.R;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.ui.profile.ProfileFragment;
import com.group2.recipeze.ui.profile.ProfileViewModel;

import java.util.List;

public class AddPostFragment extends Fragment {
    ForumRepository forumRepository;
    MutableLiveData<Integer> resultingPostId = new MutableLiveData<>();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AddPostFragment thisFragment = this;
        android.view.View root = inflater.inflate(R.layout.add_forumpost, container, false);

        // Get tagName from whoever called fragment
        String tagName = getArguments().getString("tagName");

        forumRepository = ForumRepository.getInstance();
        EditText postTitle = root.findViewById(R.id.postTitle);
        EditText body = root.findViewById(R.id.body);
        //ChipGroup tagsGroup = root.findViewById(R.id.tagsGroup);
        Button saveButton = root.findViewById(R.id.savePostButton);

        resultingPostId.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumRepository.createForumPost(postTitle.getText().toString(), body.getText().toString(), tagName, resultingPostId);
            }


        });
        return root;
    }
}
