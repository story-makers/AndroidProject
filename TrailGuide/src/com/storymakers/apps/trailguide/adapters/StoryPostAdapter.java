package com.storymakers.apps.trailguide.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGPost;

public class StoryPostAdapter extends ArrayAdapter<TGPost> {
	private ImageLoader imageLoader;

	public StoryPostAdapter(Context context, List<TGPost> objects) {
		super(context, 0, objects);
		imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited()) {
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
					cacheInMemory().cacheOnDisc().build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.defaultDisplayImageOptions(defaultOptions)
					.build();
			imageLoader.init(config);
		}
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
		TGPost post = (TGPost) getItem(position);
		if (convertView == null) {
			int type = getItemViewType(position);
			convertView = getInflatedLayoutForType(type);
		}
		ImageView ivPostPhoto = (ImageView) convertView.findViewById(R.id.ivPostPhoto);
		if (ivPostPhoto != null) {
			ivPostPhoto.setImageResource(android.R.color.transparent);
			imageLoader.displayImage(post.getPhoto_url(), ivPostPhoto);
		}
		TextView tvPostNote = (TextView) convertView.findViewById(R.id.tvPostNote);
		if (tvPostNote != null && post.getNote() != null) {
			tvPostNote.setText(post.getNote());
		} else {
			tvPostNote.setVisibility(View.GONE);
			ivPostPhoto.setLayoutParams(getLayoutParams());
		}
		return convertView;
	}

	private LayoutParams getLayoutParams() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return params;
	}

	// Given the item type, responsible for returning the correct inflated XML layout file
	private View getInflatedLayoutForType(int type) {
		if (type == TGPost.PostType.PHOTO.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(R.layout.item_post_photo, null);
		} else if (type == TGPost.PostType.NOTE.getNumVal()) {
			return LayoutInflater.from(getContext()).inflate(R.layout.item_post_note, null);
		} else {
			return null;
		}
	}

}
