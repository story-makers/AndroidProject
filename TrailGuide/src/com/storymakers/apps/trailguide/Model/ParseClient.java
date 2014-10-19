package com.storymakers.apps.trailguide.model;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

public class ParseClient {
	private static final String PARSE_APP_ID = "EKY6Z6W0i9wPp5CWoUHMn0jhblyut1mZD1nRGLG7";
	private static final String PARSE_CLIENT_KEY = "VfGyXOoWrDFPfm9V36tZA0zImxQJNRswuHekQvfK";
	private static final String TWITTER_APP_KEY = "Xl5vHPjcPdIwcm4LpqE7IqVwU";
	private static final String TWITTER_APP_SECRET = "tWK7UNj2T0gwbDpDTHv7s6nzROcUdh1sbJOfLx2JfscUpA1qx4";
	public static final int LOGIN_REQUEST = 501;
	private static ParseClient client = null;
	@SuppressWarnings("unused")
	private Context context;

	private ParseClient(Context ctx) {

		ParseObject.registerSubclass(TGPost.class);
		ParseObject.registerSubclass(TGStory.class);
		Parse.initialize(ctx, PARSE_APP_ID, PARSE_CLIENT_KEY);
		Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
		ParseTwitterUtils.initialize(TWITTER_APP_KEY, TWITTER_APP_SECRET);
		// ParseUser.enableAutomaticUser();
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

}
