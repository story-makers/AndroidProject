package com.storymakers.apps.trailguide.adapters;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGUtils;

public class MapInfoWindowItemAdapter extends ArrayAdapter<TGPost> {

	private ImageLoader imageLoader;

	public MapInfoWindowItemAdapter(Context context, List<TGPost> posts) {
		super(context, 0, posts);
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
			convertView = getInflatedLayoutForType(post.getType(), parent);
		}

		if (type == TGPost.PostType.PHOTO.getNumVal()) {
			ImageView ivPostInfoImage = (ImageView) convertView
					.findViewById(R.id.ivPostInfoImage);
			ivPostInfoImage.setImageResource(android.R.color.transparent);
			TextView tvPostInfoText = (TextView) convertView
					.findViewById(R.id.tvPostInfoText);
			if (tvPostInfoText != null && post.getNote() != null) {
				tvPostInfoText.setText(post.getNote());
			}
			if (post.getLocalImagePath() != null) {
				ivPostInfoImage.setImageBitmap(TGUtils.getBitmapForLocalUri(Uri
						.parse(post.getLocalImagePath())));
			} else if (post.getPhoto_url().length() > 0) {
				imageLoader.displayImage(post.getPhoto_url(), ivPostInfoImage);
			}
		}
		if (type == TGPost.PostType.NOTE.getNumVal() || type == TGPost.PostType.LOCATION.getNumVal()) {
			TextView tvPostInfoText = (TextView) convertView
					.findViewById(R.id.tvPostInfoText);
			if (tvPostInfoText != null && post.getNote() != null) {
				tvPostInfoText.setText(post.getNote());
			}
		}
		return convertView;
	}

	// Given the item type, responsible for returning the correct inflated XML layout file
	private View getInflatedLayoutForType(TGPost.PostType type, ViewGroup parent) {
		if (type == TGPost.PostType.PHOTO) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_info_image, parent, false);
		} else if (type == TGPost.PostType.NOTE || type == PostType.LOCATION) {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_info_text, parent, false);
		} else {
			return LayoutInflater.from(getContext()).inflate(
					R.layout.item_post_null, parent, false);
		}
	}
}
