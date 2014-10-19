package com.storymakers.apps.trailguide.tbd;

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
import com.storymakers.apps.trailguide.listeners.EndlessScrollListener;

public class CopyOfStoryDetailFragment extends Fragment {
	private ArrayList<StorySegment> segments;
	private ArrayAdapter<StorySegment> aSegments;
	private ListView lvSegments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		segments = new ArrayList<StorySegment>();
		// shoudl reach activity as least as possible!!!!
		aSegments = new StorySegmentAdapter(getActivity(), segments);
		// fragmentTweetsList = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_story_detail1, container,
				false);
		lvSegments = (ListView) v.findViewById(R.id.lvStoryPosts);
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
		populateTimeLine(-1);
		return v;
	}

	public void populateTimeLine(long max_id) {
		StorySegment seg1 = new StorySegment("text1 one two three",
				StorySegment.SegmentTypes.TextNote);
		StorySegment seg2 = new StorySegment("text1 one two three",
				StorySegment.SegmentTypes.HomeSegment,
				"http://2.gravatar.com/avatar/858dfac47ab8176458c005414d3f0c36?s=256&d=&r=G");
		StorySegment seg3 = new StorySegment("text1 one two three",
				StorySegment.SegmentTypes.MapDescr,
				"http://2.gravatar.com/avatar/858dfac47ab8176458c005414d3f0c36?s=256&d=&r=G");
		List<StorySegment> segments = new ArrayList<StorySegment>();
		segments.add(seg1);
		segments.add(seg2);
		segments.add(seg3);
		aSegments.addAll(segments);
	}
}
