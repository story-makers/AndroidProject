package com.storymakers.apps.trailguide.fragments;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;

public class StoryDetailFragment extends Fragment {
	TGStory story;
	ProgressBar pb;

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
		setupListFragment(v);
		return v;
	}

	private void setupListFragment(View v) {
		// Begin the transaction
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flContainerPosts, new PostListFragment(story),
				"post_list_fragment");
		// Execute the changes specified
		ft.commit();
		pb = (ProgressBar) v.findViewById(R.id.pbLoading);
		pb.setVisibility(ProgressBar.VISIBLE);
		// Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
		story.getPosts(new PostListDownloadCallback() {

			@Override
			public void fail(String reason) {
				Log.e("ERROR", reason);
			}

			@Override
			public void done(List<TGPost> objs) {
				// We would like to filter reference story posts for the hike's
				// story detail fragment
				List<TGPost> filteredPosts = TGStory.filterPosts(
						objs,
						new HashSet<PostType>(Arrays
								.asList(PostType.REFERENCEDSTORY)));
				PostListFragment fragment = (PostListFragment) getChildFragmentManager()
						.findFragmentByTag("post_list_fragment");
				// Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT)
				// .show();
				pb.setVisibility(ProgressBar.INVISIBLE);
				fragment.addAll(filteredPosts);
			}
		});
	}

	private void getStory() {
		String storyId = getArguments().getString("hike");
		story = RemoteDBClient.getStoryById(storyId);
	}
}
