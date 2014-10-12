package com.storymakers.sandbox.app;

public interface UploadProgressHandler {
	void progress(long item_completed);
	void complete();
}
