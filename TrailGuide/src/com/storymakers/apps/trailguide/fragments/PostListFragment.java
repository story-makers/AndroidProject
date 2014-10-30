package com.storymakers.apps.trailguide.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.StoryPostAdapter;
import com.storymakers.apps.trailguide.listeners.OnPostClickListener;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.views.ParallaxListView;

public class PostListFragment extends Fragment {
	private ArrayList<TGPost> posts;
	private StoryPostAdapter storyPostAdapter;
	private ParallaxListView  lvStoryPosts;
	private View storyCoverPhotoView;
	private TGStory story;

	public PostListFragment(TGStory s) {
		story = s;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//storyPostAdapter = new StoryPostAdapter(getActivity(), posts);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		posts = new ArrayList<TGPost>();
		storyPostAdapter = new StoryPostAdapter(activity, posts);
		if (activity instanceof OnPostClickListener) {
			storyPostAdapter.setPostClickListener((OnPostClickListener)activity);
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement OnPostClickListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_post_list, container, false);
		 storyCoverPhotoView = inflater.inflate(R.layout.item_post_coverphoto,
		 null, false);
		 
		setStoryAttributes(storyCoverPhotoView, story);
		lvStoryPosts = (ParallaxListView) v.findViewById(R.id.lvStoryPosts);
		//completionFooterView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 400));
		lvStoryPosts.addParallaxedHeaderView(storyCoverPhotoView);
		TextView tv = new TextView(getActivity());
		tv.setText("PARALLAXED");
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(40);
		tv.setHeight(500);
		//tv.setBackgroundResource(R.drawable.ic_mountain);
		//lvStoryPosts.addParallaxedHeaderView(tv);
		lvStoryPosts.setAdapter(storyPostAdapter);

		return v;
	}

	public void notifyAdapter() {
		storyPostAdapter.notifyDataSetChanged();
	}

	public void addAll(List<TGPost> postsList) {
		storyPostAdapter.addAll(postsList);
		
	}

	public void addPost(TGPost p) {
		storyPostAdapter.add(p);
		scrollToEnd();

	}

	public void scrollToEnd() {
		if (storyPostAdapter.getCount() > 0)
			lvStoryPosts
					.smoothScrollToPosition(storyPostAdapter.getCount() - 1);
	}

	public void setFooterView(View v) {
		lvStoryPosts.addFooterView(v);
	}
	
	private void setStoryAttributes(View v, TGStory story) {

		ImageView ivCoverPhoto = (ImageView) v.findViewById(R.id.ivCoverPhoto);
		final TextView tvLikes = (TextView) v.findViewById(R.id.tvLikes);
		TextView tvRefs = (TextView) v.findViewById(R.id.tvRefs);
		TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		ImageView ivShareIcon = (ImageView) v.findViewById(R.id.ivShareIcon);

		ivCoverPhoto.setImageResource(android.R.color.transparent);
		if (story.getCoverPhotoURL() != null) {
			ImageLoader.getInstance().displayImage(story.getCoverPhotoURL(),
					ivCoverPhoto);
		}
		tvLikes.setText(String.valueOf(story.getLikes()));
		tvRefs.setText(String.valueOf(story.getRefs()));
		tvTitle.setText(story.getTitle());
		tvLikes.setTag(R.string.object_key, story);
		tvLikes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TGStory story = (TGStory) v.getTag(R.string.object_key);
				TextView tvLikes = (TextView) v;
				if (tvLikes.getCurrentTextColor() != Color.GREEN) {
					tvLikes.setTextColor(Color.GREEN);
					story.addLike(null);
					tvLikes.setText(String.valueOf(story.getLikes()));
				}
			}
		});
		ivShareIcon.setTag(R.string.object_key, story);
		ivShareIcon.setTag(R.string.cover_photo_key, ivCoverPhoto);
		ivShareIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TGStory story = (TGStory) v.getTag(R.string.object_key);
				ImageView iv = (ImageView) v.getTag(R.string.cover_photo_key);
				BitmapDrawable bitmapd = (BitmapDrawable) iv.getDrawable();
				Bitmap bitmap = bitmapd.getBitmap();
				// Save this bitmap to a file.
				File cacheDir = v.getContext().getExternalCacheDir();
				File downloadingMediaFile = new File(cacheDir, "abc.png");
				try {
					FileOutputStream out = new FileOutputStream(
							downloadingMediaFile);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				// Now send it out to share
				Intent share = new Intent(android.content.Intent.ACTION_SEND);
				share.setType("image/*");
				Uri photouri = Uri.parse("file://" + downloadingMediaFile);
				share.putExtra(Intent.EXTRA_STREAM, photouri);
				// share.putExtra(Intent.EXTRA_TITLE,
				// "my awesome caption in the EXTRA_TITLE field");
				share.putExtra(Intent.EXTRA_TEXT,
						"Click here: http://trailguide.storymakers.com/story?id="
								+ story.getObjectId());

				// share.putExtra(Intent.EXTRA_SUBJECT, "Subject");
				try {
					v.getContext().startActivity(
							Intent.createChooser(share, "Send Image."));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
	}

}
