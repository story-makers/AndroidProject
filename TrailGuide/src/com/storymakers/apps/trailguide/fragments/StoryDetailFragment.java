package com.storymakers.apps.trailguide.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;

public class StoryDetailFragment extends Fragment {
	TGStory story;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story_detail, container,
				false);
		getStory();
		setupListFragment();
		return v;
	}

	private void setupListFragment() {
		// Begin the transaction
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flContainerPosts, new PostListFragment(), "post_list_fragment");
		// Execute the changes specified
		ft.commit();
		story.getPosts(new PostListDownloadCallback() {
			
			@Override
			public void fail(String reason) {
				Log.e("ERROR", reason);
			}
			
			@Override
			public void done(List<TGPost> objs) {
				PostListFragment fragment = (PostListFragment) getChildFragmentManager()
						.findFragmentByTag("post_list_fragment");
				fragment.addAll(objs);
			}
		});
	}

	private void getStory() {
		String storyId = getArguments().getString("hike");
		story = RemoteDBClient.getStoryById(storyId);
	}
}
