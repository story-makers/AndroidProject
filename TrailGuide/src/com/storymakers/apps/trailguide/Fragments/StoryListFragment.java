package com.storymakers.apps.trailguide.Fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.StorySegmentAdapter;
import com.storymakers.apps.trailguide.Model.EndlessScrollListener;
import com.storymakers.apps.trailguide.Model.StorySegment;

public class StoryListFragment extends Fragment {
	private ArrayList<StorySegment> segments;
	private ArrayAdapter<StorySegment> aSegments;
	private ListView lvSegments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// non view init

		segments = new ArrayList<StorySegment>();
		// shoudl reach activity as least as possible!!!!
		aSegments = new StorySegmentAdapter(getActivity(), segments);
		// fragmentTweetsList = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_list_story, container,
				false);
		lvSegments = (ListView) v.findViewById(R.id.lvStorySegments);
		lvSegments.setAdapter(aSegments);
		// pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
		lvSegments.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				// pbLoading.setVisibility(ProgressBar.VISIBLE);
				// customLoadMoreDataFromApi(page);
				// or customLoadMoreDataFromApi(totalItemsCount);
			}
		});
		return v;
	}

	public void addAll(List<StorySegment> segments) {
		aSegments.addAll(segments);
	}
}
