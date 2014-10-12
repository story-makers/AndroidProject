package com.storymakers.apps.trailguide;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.storymakers.apps.trailguide.fragments.SearchHikesFragment;
import com.storymakers.apps.trailguide.fragments.MapFragment;

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
								SearchHikesFragment.class));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
		Tab tab2 = actionBar.newTab()
				.setText("Mentions")
				// .setIcon(R.drawable.ic_profile)
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new FragmentTabListener<MapFragment>(
								R.id.flContainer, this, "mentions",
								MapFragment.class));
		actionBar.addTab(tab2);

	}
}
