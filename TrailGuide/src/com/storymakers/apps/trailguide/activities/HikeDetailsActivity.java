package com.storymakers.apps.trailguide.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.fragments.StoryDetailFragment;
import com.storymakers.apps.trailguide.fragments.StoryMapFragment;
import com.storymakers.apps.trailguide.listeners.FragmentTabListener;
import com.storymakers.apps.trailguide.listeners.OnPostClickListener;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUtils;

public class HikeDetailsActivity extends FragmentActivity implements OnPostClickListener {

	protected static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	private TGStory story;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hike_details);
		Intent intentReceived = getIntent();
		if (intentReceived.getAction() == Intent.ACTION_VIEW) {
			Uri url = intentReceived.getData();
			String storyId = url.getQueryParameter("id");
			story = RemoteDBClient.getStoryById(storyId);
		} else {
			story = RemoteDBClient.getStoryById(getIntent().getStringExtra("hike"));
		}
		setupTabs();
		getActionBar().setTitle(TGUtils.getElipsizedText(story.getTitle()));
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
			overridePendingTransition(R.anim.slide_in_from_corner,
					R.anim.scale_down);
			return true;
		case R.id.miNewHike:
			startActivity(HikeCreateActivity.getIntentForCreateStory(this));
			overridePendingTransition(R.anim.slide_in_from_corner,
					R.anim.scale_down);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.scale_up, R.anim.right_out);
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle fragmentArgs = new Bundle();
		fragmentArgs.putString("hike", story.getObjectId());

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

	public static Intent getIntentForStory(Context ctx, TGStory story) {
		Intent i = new Intent(ctx, HikeDetailsActivity.class);
		i.putExtra("hike", story.getObjectId());
		return i;
	}

	@Override
	public void onPostClick(TGPost post) {
		// Do nothing
	}

	@Override
	public void onTitleClick(String title) {
		// Do nothing
	}

}
