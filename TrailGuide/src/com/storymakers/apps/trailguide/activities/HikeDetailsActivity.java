package com.storymakers.apps.trailguide.activities;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.fragments.CustomMapFragment;
import com.storymakers.apps.trailguide.fragments.StoryDetailFragment;
import com.storymakers.apps.trailguide.fragments.StoryMapFragment;
import com.storymakers.apps.trailguide.fragments.StoryMapFragment.onGoogleMapCreationListener;
import com.storymakers.apps.trailguide.listeners.FragmentTabListener;
import com.storymakers.apps.trailguide.model.TGStory;

public class HikeDetailsActivity extends FragmentActivity implements
		onGoogleMapCreationListener {

	protected static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	private Button btnAddToHike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hike_details);
		setupTabs();
		btnAddToHike = (Button) findViewById(R.id.btnAddToMyHike);
		final String requestedHikeObjectId = getIntent().getStringExtra("hike");
		btnAddToHike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("cliked", "am being asked to add to hike");
				Intent i = HikeCreateActivity
						.getIntentForCreateStory(HikeDetailsActivity.this);
				i.putExtra(getString(R.string.intent_key_add_ref),
						requestedHikeObjectId);
				startActivity(i);
			}
		});
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
		switch (id) {
		case R.id.miProfile:
			startActivity(ProfileActivity.getIntentForUserProfile(this));
			return true;
		case R.id.miNewHike:
			startActivity(HikeCreateActivity.getIntentForCreateStory(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

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
				.setTabListener(
						new FragmentTabListener<StoryDetailFragment>(
								R.id.flContainer, this, "home",
								StoryDetailFragment.class, fragmentArgs));

		actionBar.addTab(hikeDetailTab);
		actionBar.selectTab(hikeDetailTab);

		Tab storyMapTab = actionBar
				.newTab()
				.setText("Map")
				.setTag("StoryMapFragment")
				.setTabListener(
						new FragmentTabListener<StoryMapFragment>(
								R.id.flContainer, this, "mentions",
								StoryMapFragment.class, fragmentArgs));
		actionBar.addTab(storyMapTab);

	}

	@Override
	public void onGoogleMapCreation(CustomMapFragment mapFragment,
			StoryMapFragment storyFragment) {
		// TODO Auto-generated method stub

	}

	public static Intent getIntentForStory(Context ctx, TGStory story) {
		Intent i = new Intent(ctx, HikeDetailsActivity.class);
		i.putExtra("hike", story.getObjectId());
		return i;
	}

	/*
	 * private void setupFragment() {
	 * 
	 * FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	 * StoryDetailFragment storyDetails = new StoryDetailFragment();
	 * ft.replace(R.id.flContainer, storyDetails); ft.commit(); }
	 */
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
