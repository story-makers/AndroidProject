package com.storymakers.apps.trailguide;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storymakers.apps.trailguide.model.StorySegment;
import com.storymakers.apps.trailguide.model.StorySegment.SegmentTypes;

public class StorySegmentAdapter extends ArrayAdapter<StorySegment> {
	private Context context;
	static ImageView ivImage;

	public StorySegmentAdapter(Context context, List<StorySegment> storySegments) {
		super(context, 0, storySegments);
		this.context = context;
	}

	public int getItemViewType(int position) {
		return getItem(position).getStorySegmentType().ordinal();
	}

	// Total number of types is the number of enum values
	@Override
	public int getViewTypeCount() {
		return StorySegment.SegmentTypes.values().length;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		StorySegment segment = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			// Get the data item type for this position
			int type = getItemViewType(position);
			// Inflate XML layout based on the type
			convertView = getInflatedLayoutForType(type, parent, segment);
		}
		// Lookup view for data population

		// Return the completed view to render on screen
		return convertView;
	}

	private View getInflatedLayoutForType(int type, ViewGroup parent,
			StorySegment segment) {
		View v = null;
		if (type == SegmentTypes.HomeSegment.ordinal()) {
			v = LayoutInflater.from(getContext()).inflate(
					R.layout.activity_item_img, parent, false);
			ivImage = (ImageView) v.findViewById(R.id.ivActivityItemImage);
			ImageLoader imageLoader = ImageLoader.getInstance();
			// populate views
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage(segment.getImageUrl(), ivImage);
			// downloadImageAsync(segment.getImageUrl());
		} else if (type == SegmentTypes.MapDescr.ordinal()) {
			v = LayoutInflater.from(getContext()).inflate(
					R.layout.activity_item_text_and_map, parent, false);
			TextView tview = (TextView) v.findViewById(R.id.itTextMapText);
			ivImage = (ImageView) v.findViewById(R.id.ivTextMapImage);

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage(segment.getImageUrl(), ivImage);
			// downloadImageAsync(segment.getImageUrl());
		} else if (type == SegmentTypes.TextNote.ordinal()) {
			v = LayoutInflater.from(getContext()).inflate(
					R.layout.activity_item_text, parent, false);
			TextView tview = (TextView) v.findViewById(R.id.tvTextItemText);
			tview.setText(segment.getText());
		} else {
			return null;
		}
		return v;
	}

	private void downloadImageAsync(String url) {
		AsyncHttpClient client = new AsyncHttpClient();
		// can use binaryHTT, json or httpresonce that will allow to manage
		// download yourself
		String[] imageTypes = new String[] { "image/jpeg" };
		client.get(url, new BinaryHttpResponseHandler(imageTypes) {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				InputStream is = new ByteArrayInputStream(bytes);
				Bitmap bmp = BitmapFactory.decodeStream(is);
				ivImage.setImageBitmap(bmp);
			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes,
					Throwable throwable) {
				// can't use this as first parameter to toast, because this is
				// anonymous class not activity. can also use MainActivity.this
				Toast.makeText(context, "Image can't be loaded",
						Toast.LENGTH_SHORT).show();

			}

		});
	}

}
