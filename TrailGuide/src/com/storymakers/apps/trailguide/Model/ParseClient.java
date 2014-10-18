package com.storymakers.apps.trailguide.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;
import com.storymakers.apps.trailguide.interfaces.ProgressNotificationHandler;

public class ParseClient {
	private static final String PARSE_APP_ID = "EKY6Z6W0i9wPp5CWoUHMn0jhblyut1mZD1nRGLG7";
	private static final String PARSE_CLIENT_KEY = "VfGyXOoWrDFPfm9V36tZA0zImxQJNRswuHekQvfK";
	private static final String TWITTER_APP_KEY = "Xl5vHPjcPdIwcm4LpqE7IqVwU";
	private static final String TWITTER_APP_SECRET = "tWK7UNj2T0gwbDpDTHv7s6nzROcUdh1sbJOfLx2JfscUpA1qx4";
	public static final int LOGIN_REQUEST = 501;
	private static ParseClient client = null;
	private static TGUser parse_user = null;
	private Context context;

	private ParseClient(Context ctx) {
		ParseObject.registerSubclass(TGPost.class);
		ParseObject.registerSubclass(TGStory.class);
		Parse.initialize(ctx, PARSE_APP_ID, PARSE_CLIENT_KEY);
		Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
		ParseTwitterUtils.initialize(TWITTER_APP_KEY, TWITTER_APP_SECRET);
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		Parse.enableLocalDatastore(ctx);
		context = ctx;
		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}

	public static ParseClient getInstance(Context ctx) {
		// should probably synchronize this
		if (client == null) {
			client = new ParseClient(ctx);
		}
		return client;
	}

	public TGUser getCurrentUser(ProgressNotificationHandler callback) {
		final SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String userid = pref.getString("user_session_token", "");
		if (userid.length() > 0) {
			ParseUser i = ParseUser.getCurrentUser();
			if (i.isNew()) {
				Log.e("ERROR", "I should not have got a new user.");
			}
			parse_user = new TGUser(i);
		}
		if (parse_user == null) {
			if (callback != null) {
				callback.beginAction();
			}
			parse_user = RemoteDBClient.getUserByEmail(TGUtils
					.getUserEmailOnDevice(context));
			if (parse_user == null) {
				/* need to create new user account */
				final ParseUser u = ParseUser.getCurrentUser();
				parse_user = new TGUser(u);
				parse_user.setUserEmail(TGUtils.getUserEmailOnDevice(context));
				parse_user.getUserIdentity().saveEventually(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							Editor edit = pref.edit();
							edit.putString("user_session_token",
									u.getSessionToken());
							edit.commit();
						}

					}
				});

			} else {
				Editor edit = pref.edit();
				edit.putString("user_session_token", parse_user
						.getUserIdentity().getSessionToken());
				edit.commit();
			}
			if (callback != null) {
				callback.endAction();
			}

		}
		return parse_user;
	}

}
