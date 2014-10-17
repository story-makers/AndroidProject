package com.storymakers.apps.trailguide.activities;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.storymakers.apps.trailguide.ClickableButtonEditText;
import com.storymakers.apps.trailguide.DrawableClickListener;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.model.TGDraftStories;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUser;

public class HikeCreateActivity extends FragmentActivity {
	public final String APP_TAG = "TrailGuide";
	private static final String TMP_PHOTO_NAME = "newPhoto.jpg";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

	private TGStory story;
	private TGUser user;

	private ClickableButtonEditText etNewNote;
	private ImageView ivCamera;
	private ImageView ivGeoIcon;

	private Uri photoUriToSave;
	private String photoNametoSave;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hike_create);

		user = TrailGuideApplication.getCurrentUser();
		story = TGDraftStories.getInstance().getDraftStory();

		initializeViews();
		setEditTextClickListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hike_create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initializeViews() {
		etNewNote = (ClickableButtonEditText) findViewById(R.id.etNewNote);
		ivCamera = (ImageView) findViewById(R.id.ivCameraIcon);
		ivGeoIcon = (ImageView) findViewById(R.id.ivGeoIcon);
	}

	private void setEditTextClickListener() {
		etNewNote.setDrawableClickListener(new DrawableClickListener() {
			@Override
			public void onClick(DrawablePosition target) {
				if (target == DrawablePosition.RIGHT) {
					HikeCreateActivity.this.addNote();
				}
			}
		});
	}

	public void recordLocation(View v) {
		
	}

	private void addNote() {
		String text = etNewNote.getText().toString();
		if (text.length() > 0) {
			TGPost p = TGPost.createNewPost(story, PostType.NOTE);
			p.setNote(text);
			story.addPost(p);
			Toast.makeText(this, "Saved a note", Toast.LENGTH_SHORT).show();
			etNewNote.setText("");
		} else {
			Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void launchCamera(View view) {
		photoNametoSave = Long.toString(System.currentTimeMillis())
				+ TMP_PHOTO_NAME;
		photoUriToSave = getPhotoFileUri(photoNametoSave);

		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUriToSave); // set the
																	// image
																	// file name
		// Start the image capture intent to take photo
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri takenPhotoUri = photoUriToSave; // getPhotoFileUri(photoFileName);
				// by this point we have the camera photo on disk
				Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri
						.getPath());
				// Load the taken image into a preview dialog fragment
				/*ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
				ivPreview.setImageBitmap(takenImage);*/
				TGPost p = TGPost.createNewPost(story, TGPost.PostType.PHOTO);
				p.setPhotoFromUri(this, takenPhotoUri);
				story.addPost(p);
				Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT)
						.show();
			} else { // Result was a failure
				Toast.makeText(this, "Picture wasn't taken!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Returns the Uri for a photo stored on disk given the fileName
	public Uri getPhotoFileUri(String fileName) {

		// Get safe storage directory for photos
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				APP_TAG);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			Log.d(APP_TAG, "failed to create directory");
		}

		// Return the file target for the photo based on filename
		return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
				+ fileName));
	}
}
