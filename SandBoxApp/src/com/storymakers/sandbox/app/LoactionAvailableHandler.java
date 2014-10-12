package com.storymakers.sandbox.app;

import com.parse.ParseGeoPoint;

public interface LoactionAvailableHandler {
	void foundLocation(ParseGeoPoint point);
	void onFail();
}
