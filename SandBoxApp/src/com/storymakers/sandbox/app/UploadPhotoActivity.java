package com.storymakers.sandbox.app;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.storymakers.sandbox.app.TGPost.PostType;
import com.storymakers.sandbox.app.TGStory.StoryType;

public class UploadPhotoActivity extends Activity {
	public final String APP_TAG = "MyCustomApp";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
	public String photoFileName = "newpo.jpg";
	private Uri photoUriToSave;
	private String photoNametoSave;

	TGStory story;
	TGUser u;
	EditText etNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		etNote = (EditText) findViewById(R.id.etNote);
		u = ParseClient.getCurrentUser();
		story = TGDraftStories.getInstance().getDraftStory();
	}

	public void runListPhotos(View v) {
		String command = etNote.getText().toString();
		if (command.startsWith("create")) {
			story = TGDraftStories.getInstance().createNewDraft(u,
					etNote.getText().toString().replace("create ", ""));
			story.saveData();
			Toast.makeText(this, "Created a story", Toast.LENGTH_SHORT).show();
		} else if (command.startsWith("refresh ")) {
			RemoteDBClient.getStoriesByUser(new FindCallback<TGStory>() {

				@Override
				public void done(List<TGStory> objects, ParseException e) {
					if (e != null) {
						e.printStackTrace();
						return;
					}
					for (TGStory o : objects) {
						if (o.getState() == StoryType.DRAFT) {
							story = o;
							Toast.makeText(UploadPhotoActivity.this,
									"Found the story: " + o.getTitle(),
									Toast.LENGTH_SHORT).show();
						}
						break;
					}

				}
			}, u, 0, 0);

		} else if (command.startsWith("save")) {
			story.saveData();
			Toast.makeText(this, "Saved the story: " + story.getTitle(),
					Toast.LENGTH_SHORT).show();
			etNote.setText("");
		} else if (command.startsWith("get ")) {
			RemoteDBClient.getStories(new FindCallback<TGStory>() {

				@Override
				public void done(List<TGStory> objects, ParseException e) {
					if (e != null) {
						e.printStackTrace();
						return;
					}
					for (TGStory o : objects) {
						Toast.makeText(UploadPhotoActivity.this,
								"story:" + o.getTitle(), Toast.LENGTH_SHORT)
								.show();
					}

				}
			}, 0, 0);

		} else if (command.startsWith("note ")) {
			addNoteAction(v);

		} else if (command.startsWith("complete")) {
			story.completeStory(new UploadProgressHandler() {

				@Override
				public void progress(long item_completed) {

					Toast.makeText(UploadPhotoActivity.this, "story progress",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void complete() {
					Toast.makeText(UploadPhotoActivity.this, "Completed story",
							Toast.LENGTH_SHORT).show();

				}
			});

			story = null;
		}

	}

	public void addNoteAction(View v) {

		String text = etNote.getText().toString();
		if (text.length() > 0) {
			TGPost p = TGPost.createNewPost(story, PostType.NOTE);
			p.setNote(text);
			story.addPost(p);
			Toast.makeText(this, "Saved a note", Toast.LENGTH_SHORT).show();
			etNote.setText("");
		} else {
			Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void onLaunchCamera(View view) {
		photoNametoSave = Long.toString(System.currentTimeMillis())
				+ photoFileName;
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
				// Load the taken image into a preview
				ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
				ivPreview.setImageBitmap(takenImage);
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
