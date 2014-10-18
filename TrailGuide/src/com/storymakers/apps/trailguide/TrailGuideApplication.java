package com.storymakers.apps.trailguide;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseUser;
import com.storymakers.apps.trailguide.model.ParseClient;
import com.storymakers.apps.trailguide.model.TGUser;

public class TrailGuideApplication extends Application {
	public final static String APP_TAG = "TrailGuide";
	@SuppressWarnings("unused")
	private static ParseClient client;
	private static Context context;
	private static TGUser loggedInUser = null;

	@Override
	public void onCreate() {
		super.onCreate();

		TrailGuideApplication.context = this;
		client = ParseClient.getInstance(this);
		Log.i("Info", "I have setup the parse client");
		// Create global configuration and initialize ImageLoader with this
		// configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
		// getCurrentUser();
		// TGDraftStories.getInstance();
	}

	public static TGUser getCurrentUser() {
		ParseUser u = ParseUser.getCurrentUser();
		if (loggedInUser != null && u != null)
			return loggedInUser;

		if (u != null) {
			loggedInUser = new TGUser(u);
		}
		return loggedInUser;
	}

	public static Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

}
