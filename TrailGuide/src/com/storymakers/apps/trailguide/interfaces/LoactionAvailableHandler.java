package com.storymakers.apps.trailguide.interfaces;

import com.parse.ParseGeoPoint;

public interface LoactionAvailableHandler {
	void foundLocation(ParseGeoPoint point);
	void onFail();
}
