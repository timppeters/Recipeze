package com.group2.recipeze.ui.forum;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.group2.recipeze.RecyclerViewAdapter;
import com.group2.recipeze.data.ForumRepository;
import com.group2.recipeze.data.model.ForumPost;
import com.group2.recipeze.data.model.Recipe;
import com.group2.recipeze.endlessScroll;
import com.group2.recipeze.ui.feed.FeedFragment;
import com.group2.recipeze.ui.search.SearchViewModel;
import java.util.ArrayList;
public class FoodForumFragment extends Fragment {
    RecyclerView forumRecyclerView;
    ForumRepository forumRepository;
    private MutableLiveData<ArrayList<ForumPost>> resultingPosts = new MutableLiveData<>();
    private String tagName;
    private ForumPostAdapter recyclerViewAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.food_forum_fragment, container, false);
        FoodForumFragment thisFragment = this;

        // Get tagName from whoever called fragment
        tagName = getArguments().getString("tagName");

        TextView forumTitle = root.findViewById(R.id.forumTitle);
        forumTitle.setText(tagName + " Forum");

        forumRepository = ForumRepository.getInstance();
        resultingPosts.observe(getViewLifecycleOwner(), new Observer<ArrayList<ForumPost>>() {

            @Override
            public void onChanged(ArrayList<ForumPost> posts) {
                forumRecyclerView = root.findViewById(R.id.forums);
                recyclerViewAdapter = new ForumPostAdapter(posts);
                forumRecyclerView.setAdapter(recyclerViewAdapter);
                forumRecyclerView.setLayoutManager(new LinearLayoutManager(thisFragment.getContext()));
                recyclerViewAdapter.setThisFragment(thisFragment);

            }
        });
        ImageButton backButton = root.findViewById(R.id.back_forum);
        Button addPostButton = root.findViewById(R.id.add_post);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tagName", tagName);
                NavHostFragment.findNavController(thisFragment).navigate(R.id.action_foodForumFragment_to_add_forumpost, bundle);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        forumRepository.getForumPostsInTag(tagName, resultingPosts);
    }

}