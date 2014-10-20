package com.storymakers.apps.trailguide.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGStory;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class SearchHikesFragment extends HikesListFragment {
	private enum DISPLAYMODES {
		ALL, PULLTOREFRESH, SEARCH
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateHikesList(DISPLAYMODES.ALL, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lvHikesList.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// pbLoading.setVisibility(ProgressBar.VISIBLE);
				// refreshNewData();
				populateHikesList(DISPLAYMODES.PULLTOREFRESH, null);
			}
		});
		return v;
	}

	public void onSearchSubmit(String string) {
		populateHikesList(DISPLAYMODES.SEARCH, string);
	}

	// TBD: modify function to pull only new data and accommodate search
	public void populateHikesList(final DISPLAYMODES mode,
			@Nullable String searchString) {
		RemoteDBClient.getStories(new FindCallback<TGStory>() {

			@Override
			public void done(List<TGStory> objects, ParseException e) {
				if (e != null) {
					e.printStackTrace();
					return;
				}
				if (mode == DISPLAYMODES.SEARCH) {
					// Toast.makeText(getActivity(), "searching",
					// Toast.LENGTH_SHORT).show();
					SearchHikesFragment.this.clear();
					SearchHikesFragment.this.addAll(objects);
				} else if (mode == DISPLAYMODES.PULLTOREFRESH) {
					SearchHikesFragment.this.clear();
					SearchHikesFragment.this.addAll(objects);
					lvHikesList.onRefreshComplete();
				} else {// ALL
					SearchHikesFragment.this.addAll(objects);
				}

			}
		}, 0, 0);
	}
}
