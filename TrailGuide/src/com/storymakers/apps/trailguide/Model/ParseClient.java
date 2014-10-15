package com.storymakers.apps.trailguide.model;

import java.util.List;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseClient {
	private static final String PARSE_APP_ID = "EKY6Z6W0i9wPp5CWoUHMn0jhblyut1mZD1nRGLG7";
	private static final String PARSE_CLIENT_KEY = "VfGyXOoWrDFPfm9V36tZA0zImxQJNRswuHekQvfK";
	private static ParseClient client = null;
	private static TGUser parse_user = null;
	private Context context;

	private ParseClient(Context ctx) {
		ParseObject.registerSubclass(TGPost.class);
		ParseObject.registerSubclass(TGStory.class);
		Parse.initialize(ctx, PARSE_APP_ID, PARSE_CLIENT_KEY);
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

	public TGUser getCurrentUser() {
		if (parse_user == null) {
			RemoteDBClient.getUsersByEmail(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> objects, ParseException e) {
					if (e == null) {
						if (objects.size() > 0) {
							parse_user = new TGUser(objects.get(0));
						} else {
							ParseUser puser = ParseUser.getCurrentUser();
							puser.setEmail(TGUtils
									.getUserEmailOnDevice(context));
							puser.saveInBackground();
							parse_user = new TGUser(puser);
						}
					} else {
						e.printStackTrace();
					}
				}
			}, TGUtils.getUserEmailOnDevice(context));

		}
		return parse_user;
	}

}
