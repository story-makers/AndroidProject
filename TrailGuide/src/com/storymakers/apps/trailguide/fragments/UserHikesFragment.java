package com.storymakers.apps.trailguide.fragments;

import java.util.List;

import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGStory;

public class UserHikesFragment extends HikesListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateHikesList();
	}

	public void populateHikesList() {
		RemoteDBClient.getStoriesByUser(new FindCallback<TGStory>() {
			
			@Override
			public void done(List<TGStory> objects, ParseException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					e.printStackTrace();
					return;
				}
				UserHikesFragment.this.addAll(objects);
			}
		}, TrailGuideApplication.getCurrentUser(), 0, 0);
		
	}

	
}
