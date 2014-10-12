package com.storymakers.sandbox.app;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.storymakers.sandbox.app.TGPost.PostType;

public class PhotoUploaderActivity extends FragmentActivity implements
		LocationServicesClient.OnConnectListener {
	private LocationServicesClient locationClient;
	private TGStory currentStory;
	private TGUser currentUser;

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_uploader);
		ParseClient.getInstance(getApplicationContext());
		currentUser = ParseClient.getCurrentUser();
		locationClient = LocationServicesClient.getInstance(this,
				CONNECTION_FAILURE_RESOLUTION_REQUEST);
		currentStory = TGStory.createNewStory(currentUser, "New Story");
	}

	public void onStartHike(View v) {
		locationClient.connect();
	}

	public void onEndHike(View v) {
		onConnect();
		locationClient.disconnect();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				locationClient.connect();
				break;
			}

		}
	}

	@Override
	public void onConnect() {
		Location loc = locationClient.getLastLocation();
		if (loc != null) {
			TGPost post = TGPost.createNewPost(currentStory, PostType.LOCATION);
			post.setLocation(loc.getLatitude(), loc.getLongitude());
			post.saveData();
			Toast.makeText(this, post.toString(), Toast.LENGTH_SHORT).show();
		}
	}
}
