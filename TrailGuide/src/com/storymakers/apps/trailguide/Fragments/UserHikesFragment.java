package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;

import android.os.Bundle;

import com.storymakers.apps.trailguide.DummyData;

public class UserHikesFragment extends HikesListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateHikesList();
	}

	public void populateHikesList() {
		ArrayList<TGStory> searchedHikes = DummyData.getHikes();
		addAll(searchedHikes);
	}

	
}
