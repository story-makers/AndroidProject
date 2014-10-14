package com.storymakers.apps.trailguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGStory;

public class StoryDetailFragment extends Fragment {
	TGStory story;
	ImageView ivCoverPhoto;
	TextView tvLikes;
	TextView tvRefs;
	TextView tvTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_story_detail, container,
				false);
		setStoryAttributes(v);
		setupListFragment();
		return v;
	}

	private void setStoryAttributes(View v) {
		getStory();
		ivCoverPhoto = (ImageView) v.findViewById(R.id.ivCoverPhoto);
		tvLikes = (TextView) v.findViewById(R.id.tvLikes);
		tvRefs = (TextView) v.findViewById(R.id.tvRefs);
		tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		ivCoverPhoto.setImageResource(android.R.color.transparent);
		ImageLoader.getInstance().displayImage(story.getCoverPhotoURL(), ivCoverPhoto);
		tvLikes.setText(String.valueOf(story.getLikes()));
		tvRefs.setText(String.valueOf(story.getRefs()));
		tvTitle.setText(story.getTitle());
	}

	private void setupListFragment() {
		PostListFragment fragment = (PostListFragment) getFragmentManager().findFragmentById(R.id.fragmentPostList);
		fragment.addAll(story.getPosts());
	}

	private void getStory() {
		String storyId = getArguments().getString("hike");
		story = RemoteDBClient.getStoryById(storyId, null);
	}
}
