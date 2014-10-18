package com.storymakers.apps.trailguide.model;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.interfaces.UploadProgressHandler;

public class RemoteDBClient {

	public static void getStoriesByUser(FindCallback<TGStory> callback,
			TGUser user, int from, int limit) {
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.orderByDescending("createdAt");
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

	/* Call only after you have the story in cache */
	public static TGStory getStoryById(String objectId) {
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
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
		query.whereEqualTo("creator", TrailGuideApplication.getCurrentUser().getUserIdentity());
		query.findInBackground(callback);
	}

	public static void getStories(FindCallback<TGStory> callback, int from,
			int limit) {
		getStoriesByUser(callback, null, from, limit);
	}

	public static void postLikeAStory(TGStory s) {
		s.addLike();
	}

	public static void getPostsForStory(TGStory s,
			final TGPost.PostListDownloadCallback callback) {
		ParseQuery<TGPost> query = ParseQuery.getQuery(TGPost.class);
		query.orderByAscending(TGPost.KEY_POST_SEQUENCE);
		query.whereEqualTo("story", s);
		query.findInBackground(new FindCallback<TGPost>() {

			@Override
			public void done(List<TGPost> objects, ParseException e) {
				if (e == null) {
					callback.done(objects);
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
