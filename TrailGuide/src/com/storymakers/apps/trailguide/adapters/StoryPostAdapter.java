package com.storymakers.apps.trailguide.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storymakers.apps.trailguide.FullscreenPhotoViewActivity;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGStory.StoryType;
import com.storymakers.apps.trailguide.model.TGUtils;

public class StoryPostAdapter extends ArrayAdapter<TGPost> {
	private ImageLoader imageLoader;

	public StoryPostAdapter(Context context, List<TGPost> objects) {
		super(context, 0, objects);
		imageLoader = ImageLoader.getInstance();
	}

	// Return an integer representing the type by fetching the enum type val
	@Override
	public int getItemViewType(int position) {
		return ((TGPost) getItem(position)).getType().getNumVal();
	}

	// Total number of types is the number of enum values
	@Override
	public int getViewTypeCount() {
		return TGPost.PostType.values().length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TGPost post = (TGPost) getItem(position);
		int type = getItemViewType(position);
		if (convertView == null) {
			convertView = getInflatedLayoutForType(type);
		}
		if (type == TGPost.PostType.PHOTO.getNumVal()) {
			ImageView ivPostPhoto = (ImageView) convertView
					.findViewById(R.id.ivPostPhoto);
			ivPostPhoto.setImageResource(android.R.color.transparent);
			TextView tvPostNote = (TextView) convertView
					.findViewById(R.id.tvPostNote);
			if (tvPostNote != null && post.getNote() != null) {
				tvPostNote.setText(post.getNote());
			} else {
				tvPostNote.setVisibility(View.GONE);
				ivPostPhoto.setLayoutParams(getLayoutParams());
			}
			if (post.getLocalImagePath() != null) {
				ivPostPhoto.setImageBitmap(TGUtils.getBitmapForLocalUri(Uri
						.parse(post.getLocalImagePath())));
			} else if (post.getPhoto_url().length() > 0) {
				imageLoader.displayImage(post.getPhoto_url(), ivPostPhoto);

			} else {
				ivPostPhoto.setVisibility(View.GONE);
			}
			if (post.getStory().getState() == StoryType.COMPLETE) {
				ivPostPhoto.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						getContext().startActivity(
								FullscreenPhotoViewActivity
										.getIntentForFullscreenPhotoActivity(
												getContext(), post));

					}
				});
			}
		}
		if (type == TGPost.PostType.NOTE.getNumVal()) {
			TextView tvPostNote = (TextView) convertView
					.findViewById(R.id.tvPostNote);
			if (tvPostNote != null && post.getNote() != null) {
				tvPostNote.setText(post.getNote());
			} else {
				tvPostNote.setVisibility(View.GONE);
			}
		}
		if (type == TGPost.PostType.LOCATION.getNumVal()) {
			TextView tvPostNote = (TextView) convertView
					.findViewById(R.id.tvPostNote);
			if (post.getNote() != null && post.getNote().length() > 0) {
				tvPostNote.setText("Location marker:" + post.getNote());
			} else {
				tvPostNote.setText("Location marker: "
						+ post.getLocationString());
			}
		}
		if (type == TGPost.PostType.PREAMBLE.getNumVal()) {
			setStoryAttributes(convertView, post.getStory());
			ImageView ivLinkedStoryIcon = (ImageView) convertView
					.findViewById(R.id.ivLinkedStoryIcon);
			ivLinkedStoryIcon.setVisibility(View.INVISIBLE);
		}
		if (type == TGPost.PostType.REFERENCEDSTORY.getNumVal()) {
			setStoryAttributes(convertView, post.getReferencedStory());
			ImageView ivLinkedStoryIcon = (ImageView) convertView
					.findViewById(R.id.ivLinkedStoryIcon);
			ivLinkedStoryIcon.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private void setStoryAttributes(View v, TGStory story) {

		ImageView ivCoverPhoto = (ImageView) v.findViewById(R.id.ivCoverPhoto);
		final TextView tvLikes = (TextView) v.findViewById(R.id.tvLikes);
		TextView tvRefs = (TextView) v.findViewById(R.id.tvRefs);
		TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		ImageView ivShareIcon = (ImageView) v.findViewById(R.id.ivShareIcon);

		ivCoverPhoto.setImageResource(android.R.color.transparent);
		ImageLoader.getInstance().displayImage(story.getCoverPhotoURL(),
				ivCoverPhoto);

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

	private LayoutParams getLayoutParams() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return params;
	}

	// Given the item type, responsible for returning the correct inflated XML
	// layout file
	private View getInflatedLayoutForType(int type) {
		if (type == TGPost.PostType.PHOTO.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_photo, null);
		} else if (type == TGPost.PostType.NOTE.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_note, null);
		} else if (type == TGPost.PostType.LOCATION.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_note, null);
		} else if (type == TGPost.PostType.PREAMBLE.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_coverphoto, null);
		} else if (type == TGPost.PostType.REFERENCEDSTORY.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_coverphoto, null);
		} else {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_null, null);
		}
	}

}
