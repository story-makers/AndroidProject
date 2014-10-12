package com.storymakers.apps.trailguide.Fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storymakers.apps.trailguide.Model.StorySegment;

public class HomeTripListFragment extends StoryListFragment {
	private HomeTripListFragment fragmentHomeTripList;

	public static HomeTripListFragment newInstance() {
		HomeTripListFragment fragmentDemo = new HomeTripListFragment();
		Bundle args = new Bundle();
		fragmentDemo.setArguments(null);
		return fragmentDemo;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentHomeTripList = this;
		// client = TwitterApplication.getRestClient();
		// populateTimeLine(-1);

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
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
		fragmentHomeTripList.addAll(segments);
	}
}
