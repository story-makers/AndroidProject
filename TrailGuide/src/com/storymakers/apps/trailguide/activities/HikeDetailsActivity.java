package com.storymakers.apps.trailguide.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.fragments.StoryDetailFragment;
import com.storymakers.apps.trailguide.fragments.StoryMapFragment;
import com.storymakers.apps.trailguide.listeners.FragmentTabListener;
import com.storymakers.apps.trailguide.model.TGStory;

public class HikeDetailsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hike_details);
		setupTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hike_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle fragmentArgs = new Bundle();
		fragmentArgs.putString("hike", getIntent().getStringExtra("hike"));

		Tab hikeDetailTab = actionBar
		    .newTab()
		    .setText("Timeline")
		    .setTag("StoryDetailFragment")
		    .setTabListener(new FragmentTabListener<StoryDetailFragment>(R.id.flContainer, this,
                        "home", StoryDetailFragment.class, fragmentArgs));

		actionBar.addTab(hikeDetailTab);
		actionBar.selectTab(hikeDetailTab);

		Tab storyMapTab = actionBar
		    .newTab()
		    .setText("Map")
		    .setTag("StoryMapFragment")
		    .setTabListener(new FragmentTabListener<StoryMapFragment>(R.id.flContainer, this,
                        "mentions", StoryMapFragment.class, fragmentArgs));
		actionBar.addTab(storyMapTab);
		
	}

	/*private void setupFragment() {
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		StoryDetailFragment storyDetails = new StoryDetailFragment();
		ft.replace(R.id.flContainer, storyDetails);
		ft.commit();
	}*/
}
