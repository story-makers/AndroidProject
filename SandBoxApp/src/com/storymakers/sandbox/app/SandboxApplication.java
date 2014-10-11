package com.storymakers.sandbox.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;


public class SandboxApplication extends Application {
	public static final String PARSE_APPLICATION_ID ="EKY6Z6W0i9wPp5CWoUHMn0jhblyut1mZD1nRGLG7";
	public static final String PARSE_CLIENT_KEY = "VfGyXOoWrDFPfm9V36tZA0zImxQJNRswuHekQvfK";
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        //defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}
