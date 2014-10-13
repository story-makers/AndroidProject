package com.storymakers.apps.trailguide.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.R.id;
import com.storymakers.apps.trailguide.R.layout;
import com.storymakers.apps.trailguide.R.menu;
import com.storymakers.apps.trailguide.fragments.SearchHikesFragment;

public class HomeActivity extends FragmentActivity {

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

}
