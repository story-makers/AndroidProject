package com.storymakers.apps.trailguide.interfaces;

import com.parse.ParseGeoPoint;

public interface LocationAvailableHandler {
	void foundLocation(ParseGeoPoint point);
	void onFail();
}
