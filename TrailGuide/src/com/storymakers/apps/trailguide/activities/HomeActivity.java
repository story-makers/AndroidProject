package com.storymakers.apps.trailguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.fragments.HikesListFragment;
import com.storymakers.apps.trailguide.fragments.HikesListFragment.TGStory;
import com.storymakers.apps.trailguide.fragments.SearchHikesFragment;

public class HomeActivity extends FragmentActivity implements
		HikesListFragment.OnListItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// getActionBar().setTitle("TEST");
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		/*
		 * ActionBar actionBar = getActionBar();
		 * actionBar.setDisplayShowHomeEnabled(false); // displaying custom
		 * ActionBar View mActionBarView = getLayoutInflater().inflate(
		 * R.layout.action_bar_custom, null);
		 * actionBar.setCustomView(mActionBarView);
		 * actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		 */
		return true;
	}

	public void onListItemClicked(View v) {
		SearchHikesFragment fragment = (SearchHikesFragment)
				getSupportFragmentManager().findFragmentById(R.id.fragmentSearchHikes);
		fragment.onListItemClicked(v);
	}

	@Override
	public void onListItemClicked(TGStory story) {
		Intent i = new Intent(this, HikeDetailsActivity.class);
		i.putExtra("hike", story);
		startActivity(i);
	}
}
