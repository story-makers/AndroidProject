package com.storymakers.apps.trailguide.tbd;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.R.id;
import com.storymakers.apps.trailguide.R.layout;
import com.storymakers.apps.trailguide.fragments.SearchHikesFragment;
import com.storymakers.apps.trailguide.fragments.StoryMapFragment;
import com.storymakers.apps.trailguide.listeners.FragmentTabListener;

public class CopyOfTripsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tab1 = actionBar.newTab()
				.setText("Home")
				// .setIcon(R.drawable.ic_profile)
				.setTag("HomeTimelineFragment")
				.setTabListener(
						new FragmentTabListener<SearchHikesFragment>(
								R.id.flContainer, this, "home",
								SearchHikesFragment.class, new Bundle()));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
		Tab tab2 = actionBar.newTab()
				.setText("Mentions")
				// .setIcon(R.drawable.ic_profile)
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new FragmentTabListener<StoryMapFragment>(
								R.id.flContainer, this, "mentions",
								StoryMapFragment.class, new Bundle()));
		actionBar.addTab(tab2);

	}
}
