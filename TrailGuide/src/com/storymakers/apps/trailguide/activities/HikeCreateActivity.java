package com.storymakers.apps.trailguide.activities;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.fragments.CreateDialogFragment;
import com.storymakers.apps.trailguide.fragments.PostListFragment;
import com.storymakers.apps.trailguide.interfaces.LoactionAvailableHandler;
import com.storymakers.apps.trailguide.interfaces.ProgressNotificationHandler;
import com.storymakers.apps.trailguide.interfaces.UploadProgressHandler;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGDraftStories;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUser;
import com.storymakers.apps.trailguide.model.TGUtils;

public class HikeCreateActivity extends FragmentActivity implements
		CreateDialogFragment.OnDialogDoneListener {

	private static final String TMP_PHOTO_NAME = "newPhoto.jpg";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

	private TGStory story = null;
	private TGUser user;

	private Button btnCreate;

	private Uri photoUriToSave;
	private String photoNametoSave;
	private PostListFragment postListFragment;
	private ProgressNotificationHandler progressbar;
	private boolean returnFromCamera;

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
		
		postListFragment = (PostListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentPostList);
		findDraftStory();
		initializeViews();
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
					story.getPosts(new PostListDownloadCallback() {
						
						@Override
						public void fail(String reason) {
							Log.e("ERROR", reason);
						}
						
						@Override
						public void done(List<TGPost> objs) {
							postListFragment.addAll(objs);
						}
					});
					HikeCreateActivity.this.getActionBar().setTitle(story.getTitle());
				}
				if (story == null) {
					// default name until someone fills in the title.
					story = TGDraftStories.getInstance().createNewDraft(user, "New Hike");
					showCreateDialog(PostType.METADATA, story.getTitle());
				}
				d.cancel();
			}
		});
	}

	private void showCreateDialog(PostType type, String content) {
		FragmentManager fm = getSupportFragmentManager();
	    CreateDialogFragment createDialogFragment = CreateDialogFragment.newInstance(
	    		type.getNumVal(), content);
	    createDialogFragment.show(fm, "fragment_create_dialog");
	}

	private void showCreateDialog(TGPost post) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentManager.enableDebugLogging(true);
	    CreateDialogFragment createDialogFragment = CreateDialogFragment.newInstance(post);
	    createDialogFragment.show(fm, "fragment_create_dialog");
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

	@Override
	public void onDone(TGPost post) {
		story.addPost(post, progressbar);
		postListFragment.addPost(post);
		Toast.makeText(this, "Saved a " + post.getType().toString(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoneTitle(String title) {
		getActionBar().setTitle(title);
		story.setTitle(title);
	}

	private void initializeViews() {
		btnCreate = (Button) findViewById(R.id.btnCreateStory);
		btnCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		});
	}

	public void onRecordLocation(View v) {
		TGUtils.getCurrentLocation(new LoactionAvailableHandler() {
			@Override
			public void onFail() {
			}

			@Override
			public void foundLocation(ParseGeoPoint point) {
				TGPost p = TGPost.createNewPost(story, PostType.LOCATION);
				p.setLocation(point.getLatitude(), point.getLongitude());
				showCreateDialog(p);
			}
		});
	}

	public void onAddNote(View v) {
		showCreateDialog(PostType.NOTE, "");
	}

	public void onLaunchCamera(View view) {
		photoNametoSave = Long.toString(System.currentTimeMillis())
				+ TMP_PHOTO_NAME;
		photoUriToSave = TGUtils.getPhotoFileUri(photoNametoSave);

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
				returnFromCamera = true;
			} else { // Result was a failure
				Toast.makeText(this, "Picture wasn't taken!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		if (returnFromCamera) {
			showCreateDialog(PostType.PHOTO, photoUriToSave.toString());
		}
		returnFromCamera = false;
	}
}
