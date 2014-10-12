package com.storymakers.apps.trailguide;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.storymakers.apps.trailguide.Fragments.HomeTripListFragment;

public class TripsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trips);
		// getActionBar().setTitle("TEST");
		setupFragment();
	}

	private void setupFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		HomeTripListFragment userTimeline = HomeTripListFragment.newInstance();
		ft.replace(R.id.flContainer, userTimeline);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_trips_actions, menu);
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

}
