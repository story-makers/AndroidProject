package com.storymakers.apps.trailguide.listeners;

import com.storymakers.apps.trailguide.model.TGPost;

public interface OnPostClickListener {
	public void onPostClick(TGPost post);
	public void onTitleClick(String title);
}
