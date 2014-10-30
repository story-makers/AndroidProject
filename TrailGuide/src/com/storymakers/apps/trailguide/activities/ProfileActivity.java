package com.storymakers.apps.trailguide.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.fragments.HikesListFragment;
import com.storymakers.apps.trailguide.fragments.HikesListFragment.OnListItemClickListener;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUser;

public class ProfileActivity extends FragmentActivity implements
		OnListItemClickListener {
	private TGUser user;
	private TextView tvUserName;
	private Button btnLogout;
	private HikesListFragment hikesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(false);

		user = TrailGuideApplication.getCurrentUser();
		if (user != null) {
			getActionBar().setTitle(user.getName() + " : Profile");
		}
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		hikesFragment = new HikesListFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.flUserHikes, hikesFragment);
		ft.commit();
		populateUserHikes();

		tvUserName.setText(user.getName() + "\n" + user.getUserEmail());
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.scale_up, R.anim.slide_out_to_corner);
	}

	private void populateUserHikes() {
		RemoteDBClient.getCompletedStoriesByUser(new FindCallback<TGStory>() {

			@Override
			public void done(List<TGStory> arg0, ParseException arg1) {
				if (arg1 == null) {
					// hikesFragment.setDeleteActionAllowed(true);
					hikesFragment.addAll(arg0);
				}
			}
		}, user, 0, 0);

		RemoteDBClient.getDraftStoriesByUser(new FindCallback<TGStory>() {

			@Override
			public void done(List<TGStory> arg0, ParseException arg1) {
				if (arg1 == null) {
					// hikesFragment.setDeleteActionAllowed(true);
					hikesFragment.addAll(arg0);
				}
			}
		});
	}

	public static Intent getIntentForUserProfile(Context ctx) {
		Intent i = new Intent(ctx, ProfileDispatchActivity.class);
		return i;
	}

	public void onLogoutAction(View v) {
		TrailGuideApplication.logOutCurrentUser();
		finish();
	}

	public void onListItemClicked(View v) {
		hikesFragment.onListItemClicked(v);
	}

	@Override
	public void onListItemClicked(TGStory story) {
		Intent i = HikeDetailsActivity.getIntentForStory(this, story);
		startActivity(i);
	}
}
