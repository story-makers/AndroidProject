package com.storymakers.apps.trailguide.interfaces;

public interface UploadProgressHandler {
	void progress(long item_completed);
	void complete();
}
