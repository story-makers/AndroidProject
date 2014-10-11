package com.storymakers.sandbox.app;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseClient {
	private static final String PARSE_APP_ID ="EKY6Z6W0i9wPp5CWoUHMn0jhblyut1mZD1nRGLG7";
	private static final String PARSE_CLIENT_KEY = "VfGyXOoWrDFPfm9V36tZA0zImxQJNRswuHekQvfK";
	private static ParseClient client = null;
	private static TGUser parse_user = null;
	
	private ParseClient(Context ctx) {
		ParseObject.registerSubclass(TGUser.class);
		ParseObject.registerSubclass(TGPost.class);
		ParseObject.registerSubclass(TGStory.class);
		Parse.initialize(ctx, PARSE_APP_ID, PARSE_CLIENT_KEY);
		ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        //defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
	}
	
	public static ParseClient getInstance(Context ctx) {
		//should probably synchronize this
		if (client == null) {
			client = new ParseClient(ctx);
		}
		return client;
	}
	
	public static TGUser getCurrentUser() {
		if (parse_user == null) {
			parse_user = TGUser.createNewUser(ParseUser.getCurrentUser(), "joe", "j.doe@gmail.com");
			
		}
		return parse_user;
	}
}
