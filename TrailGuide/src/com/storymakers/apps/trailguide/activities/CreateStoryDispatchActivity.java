package com.storymakers.apps.trailguide.activities;

import android.os.Bundle;

import com.parse.ui.ParseLoginDispatchActivity;

public class CreateStoryDispatchActivity extends ParseLoginDispatchActivity {

	@Override
	protected Class<?> getTargetClass() {
		return HikeCreateActivity.class;
	}

	@Override
	protected Bundle getBundledExtras() {
		return getIntent().getExtras();
	}

}
