package com.storymakers.apps.trailguide.activities;

import com.parse.ui.ParseLoginDispatchActivity;

public class CreateStoryDispatchActivity extends ParseLoginDispatchActivity {

	@Override
	protected Class<?> getTargetClass() {
		return HikeCreateActivity.class;
	}

}
