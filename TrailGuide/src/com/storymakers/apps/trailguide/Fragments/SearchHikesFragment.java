package com.storymakers.apps.trailguide.fragments;

import java.util.List;

import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGStory;

public class SearchHikesFragment extends HikesListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateHikesList();
	}

	public void populateHikesList() {
		RemoteDBClient.getStories(new FindCallback<TGStory>() {
			
			@Override
			public void done(List<TGStory> objects, ParseException e) {
				if (e!= null) {
					e.printStackTrace();
					return;
				}
				SearchHikesFragment.this.addAll(objects);
				
			}
		}, 0, 0);
		
	}

	
}
