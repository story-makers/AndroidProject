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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.activities.FullscreenPhotoViewActivity;
import com.storymakers.apps.trailguide.listeners.OnPostClickListener;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;
import com.storymakers.apps.trailguide.model.TGUtils;

public class StoryPostAdapter extends ArrayAdapter<TGPost> {
	private ImageLoader imageLoader;
	private OnClickListener onMapClickListener;

	private OnPostClickListener postClickListener;
	
	public StoryPostAdapter(Context context, List<TGPost> objects) {
		super(context, 0, objects);
		imageLoader = ImageLoader.getInstance();
		onMapClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				TGPost p = (TGPost) v.getTag(R.string.object_post_key);
				float mLatitude = (float) p.getLocation().getLatitude();
				float mLongitude = (float) p.getLocation().getLongitude();
				int mZoom = 9;
				StoryPostAdapter.this.getContext()
						.startActivity(
								new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"
										+ mLatitude + "," + mLongitude + "?q="
										+ mLatitude + "," + mLongitude + "&z="
										+ mZoom)));

			}
		};
	}

	public void setPostClickListener(OnPostClickListener listener) {
		postClickListener = listener;
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
			convertView = getInflatedLayoutForPost(post);
		}
		convertView.setTag(R.string.post_type_key, post.getType());
		convertView.setTag(R.string.object_post_key, post);

		if (post.getType() != convertView.getTag(R.string.post_type_key)) {
			Log.e("ERROR", "Convert view is for different type than type"
					+ post.getType());
		}

		if (type == TGPost.PostType.PHOTO.getNumVal()) {
			TextView tvPostDateTime = (TextView) convertView
					.findViewById(R.id.tvPostDateTime);
			tvPostDateTime.setText(post.getFormattedCreateTime());
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
				final ProgressBar pbLoading = (ProgressBar) convertView.findViewById(R.id.pbLoading);
				pbLoading.setVisibility(View.VISIBLE);
				imageLoader.displayImage(post.getPhoto_url(), ivPostPhoto, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						pbLoading.setVisibility(View.INVISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						// TODO Auto-generated method stub
						pbLoading.setVisibility(View.INVISIBLE);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						pbLoading.setVisibility(View.INVISIBLE);
					}
				});

			} else {
				ivPostPhoto.setVisibility(View.GONE);
			}
			if (post.getStory().isCompleted()) {
				ivPostPhoto.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (post.getStory().isCompleted()) {
							getContext().startActivity(
									FullscreenPhotoViewActivity
											.getIntentForFullscreenPhotoActivity(
													getContext(), post));
						}
					}
				});
			}
		}
		if (type == TGPost.PostType.NOTE.getNumVal()) {
			TextView tvPostDateTime = (TextView) convertView
					.findViewById(R.id.tvPostDateTime);
			tvPostDateTime.setText(post.getFormattedCreateTime());
			TextView tvPostNote = (TextView) convertView
					.findViewById(R.id.tvPostNote);
			if (tvPostNote != null && post.getNote() != null) {
				tvPostNote.setText(post.getNote());
			} else {
				tvPostNote.setVisibility(View.GONE);
			}
		}
		if (type == TGPost.PostType.LOCATION.getNumVal()
				&& post.getNote().length() > 0) {
			TextView tvPostNote = (TextView) convertView
					.findViewById(R.id.tvPostNote);
			if (post.getNote() != null && post.getNote().length() > 0) {
				tvPostNote.setText(post.getNote());
			} else {
				tvPostNote.setText("Location marker: "
						+ post.getLocationString());
			}
			ImageView gsmvStaticMap = (ImageView) convertView
					.findViewById(R.id.gsmvStaticLocationView);
			ParseGeoPoint geoPoint = post.getLocation();
			if (geoPoint != null) {
				Uri staticMapUri = TrailGuideApplication.getStaticMapObject()
						.getMap((float) geoPoint.getLatitude(),
								(float) geoPoint.getLongitude(), 240, 240,
								true, null);

				Log.d("STATIC MAP URL", staticMapUri.toString());
				gsmvStaticMap.setTag(R.string.object_post_key, post);
				gsmvStaticMap.setOnClickListener(onMapClickListener);
				imageLoader
						.displayImage(staticMapUri.toString(), gsmvStaticMap);
			} else {
				Log.e("ERROR", "How come geoPoint is null?");
			}
		}
		if (type == TGPost.PostType.PREAMBLE.getNumVal()) {
			//do nothing
		}
		if (type == TGPost.PostType.REFERENCEDSTORY.getNumVal()) {
			setReferenceStoryAttributes(convertView, post.getReferencedStory());
		}

		if (post.getStory().isDraft()) {
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TGPost post = (TGPost) v.getTag(R.string.object_post_key);
					postClickListener.onPostClick(post);
				}
			});
		}
		return convertView;
	}

	private void setReferenceStoryAttributes(View convertView,
			TGStory referencedStory) {
		
	}

	private LayoutParams getLayoutParams() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return params;
	}

	// Given the item type, responsible for returning the correct inflated XML
	// layout file
	private View getInflatedLayoutForPost(TGPost post) {
		PostType type = post.getType();
		if (type == TGPost.PostType.PHOTO) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_photo, null);
		} else if (type == TGPost.PostType.NOTE) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_note, null);
		} else if (type == TGPost.PostType.LOCATION
				&& post.getNote().length() > 0) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_location, null);
		} else if (type == TGPost.PostType.REFERENCEDSTORY) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_refstory, null);
		} else {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_null, null);
		}
	}

}
