package com.group2.recipeze.ui.forum;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    ForumPostAdapter forumRecyclerViewAdapter;
    endlessScrollForum endlessScrollManager;
    ForumRepository forumRepository;
    FoodForumViewModel mViewModel;
    public MutableLiveData<Integer> resultingPostId = new MutableLiveData<>();
    public MutableLiveData<ForumPost> resultingPost = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ForumPost>> resultingPosts = new MutableLiveData<>();
    ForumPost forumpost;
    private String tagName;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(FoodForumViewModel.class);
        View root = inflater.inflate(R.layout.food_forum_fragment, container, false);
        FoodForumFragment thisFragment = this;

        // Get tagName from whoever called fragment
        tagName = getArguments().getString("tagName");

        forumRepository = ForumRepository.getInstance();
        resultingPosts.observe(getViewLifecycleOwner(), new Observer<ArrayList<ForumPost>>() {

            @Override
            public void onChanged(ArrayList<ForumPost> resultingPosts) {
                // Populate endlessScroll with forums
                forumRecyclerView = root.findViewById(R.id.forums);
                endlessScrollManager = new endlessScrollForum(forumRecyclerView, tagName);
                endlessScrollManager.populateData(resultingPosts);
                endlessScrollManager.initAdapter(thisFragment);
                endlessScrollManager.initScrollListener();

            }
        });
        Button button = root.findViewById(R.id.add_forumpost);
        Fragment here = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(thisFragment).navigate(R.id.action_foodForumFragment_to_addPostFragment);
            }
        });
        /*
        forumRepository.createForumPost("Vegan", "I have been vegeterian for 2 weeks","Vegan",resultingPostId);
        forumRepository.addCommentToPost(1, "Very good recipe");
        forumRepository.likePost(1);
        forumRepository.updatePost(1,"Recently Vegan","I have been vegeterian for 2 weeks");
        forumRepository.readForumPost(1,resultingPost);*/

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        forumRepository.getForumPostsInTag(tagName, resultingPosts);
    }

}