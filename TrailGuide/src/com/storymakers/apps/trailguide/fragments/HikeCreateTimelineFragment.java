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

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGStory;

public class HikeCreateTimelineFragment extends Fragment {
	private TGStory story;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_hike_create, container,
				false);
		
		getStory();
		setupListFragment();
		return v;
	}

	private void setupListFragment() {
		// Begin the transaction
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		PostListFragment postListfragment = new PostListFragment(story);
		// Replace the container with the new fragment
		ft.replace(R.id.flContainerPosts, postListfragment,
				"post_list_fragment");
		// Execute the changes specified
		ft.commit();
		story.getPosts(new PostListDownloadCallback() {
			
			@Override
			public void fail(String reason) {
				Log.e("ERROR", reason);
			}

			@Override
			public void done(List<TGPost> objs) {
				// We would not like to filter any posts for hike create timeline fragment.
				PostListFragment fragment = (PostListFragment) getChildFragmentManager()
						.findFragmentByTag("post_list_fragment");
				fragment.addAll(objs);
				fragment.scrollToEnd();
			}
		});
	}

	public void addPostToList(TGPost post) {
		PostListFragment fragment = (PostListFragment) getChildFragmentManager()
				.findFragmentByTag("post_list_fragment");
		fragment.addPost(post);
	}

	public void notifyAdapter() {
		PostListFragment fragment = (PostListFragment) getChildFragmentManager()
				.findFragmentByTag("post_list_fragment");
		fragment.notifyAdapter();
	}

	private void getStory() {
		String storyId = getArguments().getString("hike");
		story = RemoteDBClient.getStoryById(storyId);
	}
}
