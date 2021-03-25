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
    AddPostView addPostView;
    ForumRepository forumRepository;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AddPostFragment thisFragment = this;
        addPostView=
                new ViewModelProvider(this).get(AddPostView.class);
        android.view.View root = inflater.inflate(R.layout.add_forumpost, container, false);
        EditText postTitle = root.findViewById(R.id.postTitle);
        EditText body = root.findViewById(R.id.body);
        EditText tags = root.findViewById(R.id.tags2);
        //ChipGroup tagsGroup = root.findViewById(R.id.tagsGroup);
        Button button = root.findViewById(R.id.savePostButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumRepository = ForumRepository.getInstance();
                MutableLiveData<Integer> resultingPost = new MutableLiveData<>();
                /*List<Integer> ids = tagsGroup.getCheckedChipIds();
                for (Integer id:ids){
                    Chip chip = tagsGroup.findViewById(id);
                    tagNames.add(chip.getText().toString());
                }

                 */
                forumRepository.createForumPost(postTitle.getText().toString(),body.getText().toString(),tags.getText().toString(),resultingPost);
                NavHostFragment.findNavController(thisFragment).navigate(R.id.action_add_forumpost_to_foodForumFragment);
            }


        });
        return root;
    }
}
