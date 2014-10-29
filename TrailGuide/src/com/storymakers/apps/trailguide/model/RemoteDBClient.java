package com.storymakers.apps.trailguide.model;

import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.interfaces.UploadProgressHandler;

public class RemoteDBClient {
	public static void removeAllCachedData(){
		ParseObject.unpinAllInBackground();
	}
	public static void getCompletedStoriesByUser(FindCallback<TGStory> callback,
			TGUser user, int from, int limit) {
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.orderByDescending("endDate");
		query.whereEqualTo("state", "COMPLETE");
		if (from != 0) {
			query.setSkip(from);
		}
		if (limit != 0) {
			query.setLimit(limit);
		}
		if (user != null) {
			query.whereEqualTo("creator", user.getUserIdentity());
		}

		query.findInBackground(callback);
	}

	public static void getStoriesBySearchString(FindCallback<TGStory> callback,
			TGUser user, int from, int limit, String searchQry) {
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.orderByDescending("endDate");
		query.whereEqualTo("state", "COMPLETE");
		if (from != 0) {
			query.setSkip(from);
		}
		if (limit != 0) {
			query.setLimit(limit);
		}
		if (user != null) {
			query.whereEqualTo("creator", user.getUserIdentity());
		}
		if (searchQry != null && searchQry.length() > 0) {
			query.whereContains("title", searchQry);
		}

		query.findInBackground(callback);
	}

	/* Call only after you have the story in cache */
	public static TGStory getStoryById(String objectId) {
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.fromPin();
		try {
			TGStory s = query.get(objectId);
			if (s == null){
				throw new NullPointerException();
			}else 
				return s;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	/* Call only after you have the story in cache */
	public static TGPost getPostById(String objectId) {
		ParseQuery<TGPost> query = ParseQuery.getQuery(TGPost.class);
		query.fromPin();
		try {
			return query.get(objectId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void getDraftStoriesByUser(FindCallback<TGStory> callback) {
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.orderByDescending("createdAt");
		query.whereEqualTo("state", "DRAFT");
		query.whereEqualTo("creator", TrailGuideApplication.getCurrentUser()
				.getUserIdentity());
		query.findInBackground(callback);
	}

	public static void getStories(final FindCallback<TGStory> callback, int from,
			int limit, String searchString) {
		if (searchString == null)
			getCompletedStoriesByUser(new FindCallback<TGStory>() {
				
				@Override
				public void done(List<TGStory> arg0, ParseException arg1) {
					ParseObject.pinAllInBackground(arg0);
					callback.done(arg0, arg1);
				}
			}, null, from, limit);
		else
			getStoriesBySearchString(callback, null, from, limit, searchString);
	}

	public static void getPostsForStoryById(String objid, TGPost.PostListDownloadCallback callback) {
		TGStory s = TGStory.createWithoutData(TGStory.class, objid);
		getPostsForTGStory(s, callback);
	}

	public static void getPostsForTGStory(final TGStory s,
			final TGPost.PostListDownloadCallback callback) {
		ParseQuery<TGPost> query = ParseQuery.getQuery(TGPost.class);
		query.orderByAscending(TGPost.KEY_POST_SEQUENCE);
		query.whereEqualTo("story", s);
		query.fromPin();
		query.findInBackground(new FindCallback<TGPost>() {

			@Override
			public void done(List<TGPost> objects, ParseException e) {
				if (e == null) {
					if (objects != null && objects.size() > 0) {
						callback.done(objects);
					} else {
						fetchPostsForStory(s, callback);
					}
				} else {
					fetchPostsForStory(s, callback);
					//callback.fail(e.getMessage());
				}
			}
		});
	}

	private static void fetchPostsForStory(final TGStory s,
			final TGPost.PostListDownloadCallback callback) {
		Log.d("INTERNET", "fetching posts for story with id: " + s.getObjectId());
		ParseQuery<TGPost> query = ParseQuery.getQuery(TGPost.class);
		query.orderByAscending(TGPost.KEY_POST_SEQUENCE);
		query.whereEqualTo("story", s);
		query.findInBackground(new FindCallback<TGPost>() {

			@Override
			public void done(List<TGPost> objects, ParseException e) {
				if (e == null) {
					callback.done(objects);
					if (s.isCompleted()){
						ParseObject.pinAllInBackground(objects);
					}
				} else {

					callback.fail(e.getMessage());
				}
			}
		});
	}

	public static void saveDraftStoryRef(TGStory s, UploadProgressHandler handle) {
		s.saveInBackground();
		s.pinInBackground();
		return;
	}
}
