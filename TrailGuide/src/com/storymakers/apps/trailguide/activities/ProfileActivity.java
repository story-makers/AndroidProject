package com.storymakers.apps.trailguide.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.storymakers.apps.trailguide.R;

public class ProfileActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	}

	public static Intent getIntentForUserProfile(Context ctx) {
		Intent i = new Intent(ctx, ProfileDispatchActivity.class);
		return i;
	}
}
