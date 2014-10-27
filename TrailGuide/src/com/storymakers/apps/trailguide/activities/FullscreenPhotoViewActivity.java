package com.storymakers.apps.trailguide.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.R.id;
import com.storymakers.apps.trailguide.R.layout;
import com.storymakers.apps.trailguide.adapters.PhotoViewAdapter;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGPost.PostType;

public class FullscreenPhotoViewActivity extends Activity {

	private PhotoViewAdapter adapter;
	private ViewPager viewPager;
	String begin_post_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_photo_view);
		getActionBar().hide();
		final String requestedHikeObjectId = getIntent().getStringExtra("hike");

		viewPager = (ViewPager) findViewById(R.id.pager);

		Intent i = getIntent();
		begin_post_id = i.getStringExtra("position_post");
		// show the spinner
		RemoteDBClient.getPostsForStoryById(requestedHikeObjectId,
				new PostListDownloadCallback() {

					@Override
					public void fail(String reason) {
						// failed. better bail.
						Log.e("ERROR FULLSCREENPHOTO", reason);
						FullscreenPhotoViewActivity.this.finish();

					}

					@Override
					public void done(List<TGPost> objs) {
						int position_start = 0;
						int count = -1;
						ArrayList<TGPost> posts = new ArrayList<TGPost>();
						for (TGPost p : objs) {
							if (p.getType() == PostType.PHOTO) {
								posts.add(p);
								count += 1;
							}
							if (p.getObjectId().equals(begin_post_id))
								position_start = count;

						}
						
						if (position_start < 0) {
							Log.e("ERROR", "could not find position start for posts");
							position_start = 0;
						}
						// cancel the spinner
						adapter = new PhotoViewAdapter(
								FullscreenPhotoViewActivity.this, posts);

						viewPager.setAdapter(adapter);

						// displaying selected image first
						viewPager.setCurrentItem(position_start);

					}
				});

	}

	public static Intent getIntentForFullscreenPhotoActivity(Context ctx,
			TGPost forPost) {
		Intent i = new Intent(ctx, FullscreenPhotoViewActivity.class);
		i.putExtra("position_post", forPost.getObjectId());
		i.putExtra("hike", forPost.getStory().getObjectId());
		return i;
	}
}
