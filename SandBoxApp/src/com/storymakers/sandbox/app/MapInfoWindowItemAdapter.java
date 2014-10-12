package com.storymakers.sandbox.app;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MapInfoWindowItemAdapter extends ArrayAdapter<TGPost> {

	private ImageLoader imageLoader;

	public MapInfoWindowItemAdapter(Context context, List<TGPost> posts) {
		super(context, 0, posts);
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

	// Return an integer representing the type by fetching the enum type ordinal
	@Override
	public int getItemViewType(int position) {
		return ((TGPost) getItem(position)).getType().ordinal();
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
		TextView tvPostInfoText = (TextView) convertView.findViewById(R.id.tvPostInfoText);
		if (tvPostInfoText != null) {
			tvPostInfoText.setText(post.getNote());
		}
		ImageView ivPostInfoImage = (ImageView) convertView.findViewById(R.id.ivPostInfoImage);
		if (ivPostInfoImage != null) {
			ivPostInfoImage.setImageResource(android.R.color.transparent);
			imageLoader.displayImage(post.getPhoto_url(), ivPostInfoImage);
		}
		return convertView;
	}

	// Given the item type, responsible for returning the correct inflated XML layout file
	private View getInflatedLayoutForType(int type) {
		if (type == TGPost.PostType.PHOTO.ordinal()) {
			return LayoutInflater.from(getContext()).inflate(R.layout.item_info_image, null);
		} else if (type == TGPost.PostType.NOTE.ordinal()) {
			return LayoutInflater.from(getContext()).inflate(R.layout.item_info_text, null);
		} else {
			return null;
		}
	}
}
