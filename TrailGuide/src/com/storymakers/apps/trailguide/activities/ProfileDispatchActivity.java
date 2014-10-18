package com.storymakers.apps.trailguide.activities;

import com.parse.ui.ParseLoginDispatchActivity;

public class ProfileDispatchActivity extends ParseLoginDispatchActivity {

	@Override
	protected Class<?> getTargetClass() {
		return ProfileActivity.class;
	}

}
