package com.storymakers.apps.trailguide.activities;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.fragments.CreateDialogFragment;
import com.storymakers.apps.trailguide.fragments.CustomMapFragment;
import com.storymakers.apps.trailguide.fragments.HikeCreateTimelineFragment;
import com.storymakers.apps.trailguide.fragments.StoryMapFragment;
import com.storymakers.apps.trailguide.interfaces.ProgressNotificationHandler;
import com.storymakers.apps.trailguide.interfaces.UploadProgressHandler;
import com.storymakers.apps.trailguide.listeners.FragmentTabListener;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGDraftStories;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUser;
import com.storymakers.apps.trailguide.model.TGUtils;

public class HikeCreateActivity extends FragmentActivity implements
		CreateDialogFragment.OnDialogDoneListener,
		StoryMapFragment.onGoogleMapCreationListener {

	private static final String TMP_PHOTO_NAME = "newPhoto.jpg";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

	private TGStory story = null;
	private TGUser user;

	private Button btnCreate;

	private Uri photoUriToSave;
	private String photoNametoSave;

	private boolean returnFromCamera;
	private boolean referencedStoryRequested;
	private String referencedStoryObjectId;
	private ProgressNotificationHandler addRefProgressHandler = null;
	private ProgressNotificationHandler progressbar;
	private View progressIndicatorView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hike_create);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		referencedStoryRequested = getIntent().hasExtra(
				getString(R.string.intent_key_add_ref));
		if (referencedStoryRequested) {
			referencedStoryObjectId = getIntent().getStringExtra(
					getString(R.string.intent_key_add_ref));
		}

		user = TrailGuideApplication.getCurrentUser();
		progressIndicatorView = findViewById(R.id.ivProgressView);
		progressbar = new ProgressNotificationHandler() {

			@Override
			public void endAction() {
				Log.i("CREATE_PROGRESS", "Progress is complete");
				if (progressIndicatorView != null) {
					progressIndicatorView.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beginAction() {
				if (progressIndicatorView != null) {
					progressIndicatorView.setVisibility(View.VISIBLE);
				}
				Log.i("CREATE_PROGRESS", "Begin progress bar");
			}
		};
		findDraftStory();
		initializeViews();
	}

	private void setupTabs(String hikeId) {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle fragmentArgs = new Bundle();
		fragmentArgs.putString("hike", hikeId);
		fragmentArgs.putString(getString(R.string.map_context_key),
				getString(R.string.create_hike_context));

		Tab hikeCreateTimelineTab = actionBar
				.newTab()
				.setText("Timeline")
				.setTag("HikeCreateTimelineFragment")
				.setTabListener(
						new FragmentTabListener<HikeCreateTimelineFragment>(
								R.id.flCreateContainer, this, "timeline",
								HikeCreateTimelineFragment.class, fragmentArgs));

		actionBar.addTab(hikeCreateTimelineTab);
		actionBar.selectTab(hikeCreateTimelineTab);

		Tab storyMapTab = actionBar
				.newTab()
				.setText("Map")
				.setTag("StoryMapFragment")
				.setTabListener(
						new FragmentTabListener<StoryMapFragment>(
								R.id.flCreateContainer, this, "map",
								StoryMapFragment.class, fragmentArgs));
		actionBar.addTab(storyMapTab);

	}

	private void findDraftStory() {
		final ProgressDialog d = new ProgressDialog(this);
		d.setTitle("Looking for drafts...");
		d.show();
		TGDraftStories.getInstance().getDraftStories(
				new FindCallback<TGStory>() {

					@Override
					public void done(List<TGStory> arg0, ParseException arg1) {
						if (arg1 == null && arg0.size() > 0) {
							story = arg0.get(0);
							/*
							 * .makeText(HikeCreateActivity.this,
							 * "Story found: " + story.getObjectId(),
							 * Toast.LENGTH_SHORT).show();
							 */
							HikeCreateActivity.this.getActionBar().setTitle(
									story.getTitle());
							if (story.getTitle().equals("New Hike"))
								showCreateDialog(PostType.METADATA,
										story.getTitle());
							story.saveData();
							setupTabs(story.getObjectId());
							if (referencedStoryRequested) {
								addReferencedStory(addRefProgressHandler);
							}
							d.cancel();
						}
						if (story == null) {
							// default name until someone fills in the title.
							story = TGDraftStories.getInstance()
									.createNewDraft(user, "New Hike",
											new ProgressNotificationHandler() {

												@Override
												public void endAction() {
													story.saveData();
													d.cancel();
													showCreateDialog(
															PostType.METADATA,
															story.getTitle());
													setupTabs(story
															.getObjectId());
													if (referencedStoryRequested) {
														addReferencedStory(addRefProgressHandler);
													}
												}

												@Override
												public void beginAction() {
													// TODO Auto-generated
													// method stub

												}
											});

						}

					}
				});
	}

	private void addReferencedStory(final ProgressNotificationHandler handler) {
		if (handler != null)
			handler.beginAction();
		final TGPost p = TGPost.createNewPost(story, PostType.REFERENCEDSTORY);
		TGStory refedStory = RemoteDBClient
				.getStoryById(referencedStoryObjectId);
		p.setReferencedStory(refedStory);
		story.addPost(p, new ProgressNotificationHandler() {

			@Override
			public void endAction() {
				HikeCreateTimelineFragment fragment = (HikeCreateTimelineFragment) getSupportFragmentManager()
						.findFragmentByTag("timeline");
				fragment.addPostToList(p);
				if (handler != null) {
					handler.endAction();
				}
			}

			@Override
			public void beginAction() {

			}
		});
	}

	private void showCreateDialog(PostType type, String content) {
		FragmentManager fm = getSupportFragmentManager();
		CreateDialogFragment createDialogFragment = CreateDialogFragment
				.newInstance(type.getNumVal(), content);
		createDialogFragment.show(fm, "fragment_create_dialog");
	}

	/*
	 * When we edit a post... private void showCreateDialog(TGPost post) {
	 * FragmentManager fm = getSupportFragmentManager();
	 * FragmentManager.enableDebugLogging(true); CreateDialogFragment
	 * createDialogFragment = CreateDialogFragment.newInstance(post);
	 * createDialogFragment.show(fm, "fragment_create_dialog"); }
	 */

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
		if (id == R.id.miProfile) {
			startActivity(ProfileActivity.getIntentForUserProfile(this));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDone(TGPost post) {
		story.addPost(post, progressbar);
		HikeCreateTimelineFragment fragment = (HikeCreateTimelineFragment) getSupportFragmentManager()
				.findFragmentByTag("timeline");
		fragment.addPostToList(post);
		Log.d("DEBUG",
				"Saved a " + post.getType().toString() + " story: "
						+ post.getStory().getObjectId() + " activity story: "
						+ story.getObjectId());
		/*
		 * Toast.makeText( this, "Saved a " + post.getType().toString() +
		 * " story: " + post.getStory().getObjectId() + " activity story: " +
		 * story.getObjectId(), Toast.LENGTH_SHORT).show();
		 */
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
						Toast.makeText(HikeCreateActivity.this,
								"Story completed", Toast.LENGTH_SHORT).show();
						Intent i = HikeDetailsActivity.getIntentForStory(
								HikeCreateActivity.this, story);
						HikeCreateActivity.this.startActivity(i);
						HikeCreateActivity.this.finish();
					}
				});
			}
		});
	}

	public void onRecordLocation(View v) {
		showCreateDialog(PostType.LOCATION, "");
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
				/*
				 * Toast.makeText(this, "Picture wasn't taken!",
				 * Toast.LENGTH_SHORT).show();
				 */
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

	public static Intent getIntentForCreateStory(Context ctx) {
		Intent i = new Intent(ctx, CreateStoryDispatchActivity.class);
		return i;
	}

	@Override
	public void onGoogleMapCreation(CustomMapFragment mapFragment,
			StoryMapFragment storyFragment) {
		// TODO Auto-generated method stub

	}
}
