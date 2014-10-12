package com.storymakers.sandbox.app;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class RemoteDBClient {
	public void getStoriesByUser(FindCallback<TGStory> callback, TGUser user, int from, int limit){
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.orderByDescending("createdAt");
		if (from != 0) {
			query.setSkip( from);
		}
		if (limit != 0) {
			query.setLimit(limit);
		}
		if (user != null) {
			query.whereEqualTo("creator", user.getUserIdentity());	
		}
		query.findInBackground(callback);
	}
	
	public void getStories(FindCallback<TGStory> callback, int from, int limit){
		getStoriesByUser(callback, null, from, limit);
	}
	
	public void postLikeAStory(TGStory s) {
		s.addLike();
	}
	
	public void getPostsForStory(TGStory s) {
		
	}

}
