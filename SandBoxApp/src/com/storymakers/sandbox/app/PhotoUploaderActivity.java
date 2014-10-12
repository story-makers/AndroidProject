package com.storymakers.sandbox.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PhotoUploaderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_uploader);
		ParseClient.getInstance(getApplicationContext());
		ParseClient.getCurrentUser();

	}

	
}
