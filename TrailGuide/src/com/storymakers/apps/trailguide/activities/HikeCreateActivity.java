package com.storymakers.apps.trailguide.activities;

import java.io.File;
import java.util.List;

import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.storymakers.apps.trailguide.ClickableButtonEditText;
import com.storymakers.apps.trailguide.DrawableClickListener;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.fragments.PostListFragment;
import com.storymakers.apps.trailguide.interfaces.LoactionAvailableHandler;
import com.storymakers.apps.trailguide.interfaces.ProgressNotificationHandler;
import com.storymakers.apps.trailguide.interfaces.UploadProgressHandler;
import com.storymakers.apps.trailguide.model.ParseClient;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGDraftStories;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUser;
import com.storymakers.apps.trailguide.model.TGUtils;

public class HikeCreateActivity extends FragmentActivity {

	private static final String TMP_PHOTO_NAME = "newPhoto.jpg";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

	private TGStory story = null;
	private TGUser user;

	private ClickableButtonEditText etNewNote;
	private ImageView ivCamera;
	private ImageView ivGeoIcon;
	private Button btnCreate;

	private Uri photoUriToSave;
	private String photoNametoSave;
	private PostListFragment postlistFragment;
	private ProgressNotificationHandler progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hike_create);
		progressbar = new ProgressNotificationHandler() {
			
			@Override
			public void endAction() {
				Log.i("CREATE_PROGRESS", "Progress is complete");
				Toast.makeText(HikeCreateActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void beginAction() {
				Log.i("CREATE_PROGRESS", "Begin progress bar");
				
			}
		};
		user = TrailGuideApplication.getCurrentUser();
		
		postlistFragment = (PostListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentPostList);
		findDraftStory();
		initializeViews();
		setEditTextClickListener();
		
	}

	private void findDraftStory() {
		final ProgressDialog d = new ProgressDialog(this);
		d.setTitle("Looking for drafts...");
		d.show();
		RemoteDBClient.getDraftStoriesByUser(new FindCallback<TGStory>() {
			
			@Override
			public void done(List<TGStory> arg0, ParseException arg1) {
				if (arg1 == null && arg0.size() > 0){
					story = arg0.get(0);
					story.getPosts(null);
				}
				d.cancel();
				
			}
		});
		
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (ParseUser.getCurrentUser() == null){
			showloginwindow();
		}
	}
	private void showloginwindow() {
			ParseLoginBuilder loginBuilder = new ParseLoginBuilder(HikeCreateActivity.this);
			startActivityForResult(loginBuilder.build(), ParseClient.LOGIN_REQUEST);
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
		btnCreate = (Button) findViewById(R.id.btnCreateStory);
		if (story == null) {
			btnCreate.setText("Add Title");
		}else {
			btnCreate.setText("complete \""+story.getTitle()+"\"");
			postlistFragment.addAll(story.getPosts());
		}
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = etNewNote.getText().toString();
				if (story == null && title.length() > 0) {
					story = TGDraftStories.getInstance().createNewDraft(user,
							title);
					etNewNote.setText("");
					btnCreate.setText("Create Story");
				} else {
					story.completeStory(new UploadProgressHandler() {

						@Override
						public void progress(long item_completed) {
							// TODO Auto-generated method stub

						}

						@Override
						public void complete() {
							Toast.makeText(HikeCreateActivity.this, "complete",
									Toast.LENGTH_SHORT).show();
							HikeCreateActivity.this.story = null;
						}
					});
				}

			}
		});

		ivGeoIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final TGPost p = TGPost.createNewPost(story, PostType.LOCATION);
				TGUtils.getCurrentLocation(new LoactionAvailableHandler() {

					@Override
					public void onFail() {
						// TODO Auto-generated method stub

					}

					@Override
					public void foundLocation(ParseGeoPoint point) {
						p.setLocation(point.getLatitude(), point.getLongitude());
						
						story.addPost(p, null);
						postlistFragment.addPost(p);
					}
				});

			}
		});
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
		if (text.startsWith("dummycreate")) {
			createEntireDummyStory();
		}
		if (text.length() > 0) {
			TGPost p = TGPost.createNewPost(story, PostType.NOTE);
			p.setNote(text);
			story.addPost(p, progressbar);
			postlistFragment.addPost(p);
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
				/*
				 * ImageView ivPreview = (ImageView)
				 * findViewById(R.id.ivPreview);
				 * ivPreview.setImageBitmap(takenImage);
				 */
				TGPost p = TGPost.createNewPost(story, TGPost.PostType.PHOTO);
				p.setPhotoFromUri(this, takenPhotoUri);
				story.addPost(p, progressbar);
				postlistFragment.addPost(p);
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
				TrailGuideApplication.APP_TAG);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			Log.d(TrailGuideApplication.APP_TAG, "failed to create directory");
		}

		// Return the file target for the photo based on filename
		return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
				+ fileName));
	}

	public void createEntireDummyStory() {
		TGStory story = TGDraftStories.getInstance().createNewDraft(
				TrailGuideApplication.getCurrentUser(), "Laguna Trail");
		TGPost p = TGPost.createNewPost(story, PostType.PHOTO);

		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i1));
		p.setLocation(38.015647, -122.8583851);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i2));
		p.setLocation(38.015647, -122.8583851);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i3));
		p.setLocation(38.0180459, -122.8582825);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.NOTE);
		p.setNote("This was a long and fun trail");
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i4));
		p.setLocation(38.0185489, -122.8625654);
		story.addPost(p, null);
		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i5));
		p.setLocation(38.0191769, -122.8565391);
		story.addPost(p, null);
		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i6));
		p.setLocation(38.0219532, -122.8579237);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i7));
		p.setLocation(38.0283639, -122.8588262);
		p.setNote("Somebody is hungry for mushrooms :).");
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.NOTE);
		p.setNote("Yayy!!");
		// story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i8));
		p.setLocation(38.0351291, -122.856799);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.NOTE);
		p.setNote("Sunsets are really beautiful here!.");
		p.setLocation(38.0351291, -122.856799);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i9));
		p.setLocation(38.0351291, -122.856799);
		story.addPost(p, null);
		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i10));
		p.setLocation(38.0362427, -122.8558596);
		story.addPost(p, null);
		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i11));
		p.setLocation(38.0379302, -122.8564391);
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.NOTE);
		p.setNote("Almost there.");
		story.addPost(p, null);

		p = TGPost.createNewPost(story, PostType.PHOTO);
		p.setPhotoFromUri(
				getApplicationContext(),
				Uri.parse("android.resource://" + this.getPackageName() + "/"
						+ R.raw.i12));
		p.setLocation(38.0423209, -122.8579315);

		p = TGPost.createNewPost(story, PostType.NOTE);
		p.setNote("Finally reached the end! Hurray.");
		story.addPost(p, null);
		story.addPost(p, null);
		story.saveData();
		story.completeStory(new UploadProgressHandler() {

			@Override
			public void progress(long item_completed) {
				// TODO Auto-generated method stub

			}

			@Override
			public void complete() {
				Toast.makeText(getApplicationContext(),
						"Hello the dummy data is created", Toast.LENGTH_SHORT)
						.show();

			}
		});
	}
}
