package com.storymakers.apps.trailguide.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGStory;

public class HikeArrayAdapter extends ArrayAdapter<TGStory> {
	private ImageLoader imageLoader;
	//private Boolean deleteActionAllowed;

	ProgressDialog shareWaitDialog;

	private static class ViewHolder {
		ImageView ivCoverPhoto;
		//ImageView ivDeleteStory;
		TextView tvLikes;
		TextView tvTitle;
		TextView tvRefs;
		Button ivShareHikeIcon;
		ProgressBar pbLoading;
		TextView dateTime;
	}

	public HikeArrayAdapter(Context context, List<TGStory> stories) {
		super(context, R.layout.hike_list_item, stories);
		imageLoader = ImageLoader.getInstance();
		//deleteActionAllowed = false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TGStory story = getItem(position);

		ViewHolder viewHolder;

		if (convertView == null) { // No recycled view.
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.hike_list_item, parent,
					false);
			initializeViews(viewHolder, convertView);
		} else {
			viewHolder = (ViewHolder) convertView
					.getTag(R.string.view_holder_key);
		}

		viewHolder.ivCoverPhoto.setImageResource(android.R.color.transparent);
		viewHolder.pbLoading.setVisibility(ProgressBar.VISIBLE);
		imageLoader.displayImage(story.getCoverPhotoURL(),
				viewHolder.ivCoverPhoto, new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						ViewHolder viewHolder = (ViewHolder) view
								.getTag(R.string.view_holder_key);
						viewHolder.pbLoading.setVisibility(ProgressBar.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						ViewHolder viewHolder = (ViewHolder) view
								.getTag(R.string.view_holder_key);
						viewHolder.pbLoading
								.setVisibility(ProgressBar.INVISIBLE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ViewHolder viewHolder = (ViewHolder) view
								.getTag(R.string.view_holder_key);
						viewHolder.pbLoading
								.setVisibility(ProgressBar.INVISIBLE);
					}
				});

		viewHolder.tvLikes.setText(String.valueOf(story.getLikes()));
		viewHolder.tvRefs.setText(String.valueOf(story.getRefs()));
		viewHolder.tvTitle.setText(story.getTitle());
		convertView.setTag(R.string.object_key, story);
		/*viewHolder.ivDeleteStory.setTag(R.string.object_key, story);
		if (deleteActionAllowed)
			viewHolder.ivDeleteStory.setVisibility(View.VISIBLE);*/
		// viewHolder.tvLikes.setOnClickListener();
		viewHolder.ivShareHikeIcon.setTag(R.string.object_key, story);
		String dateTime = story.getDisplayDate();
		if (dateTime != null) {
			viewHolder.dateTime.setText(story.getDisplayDate());
		}

		/*
		 * viewHolder.ivCoverPhoto .setOnLongClickListener(new
		 * View.OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View v) { ImageView imageView =
		 * (ImageView) v; BitmapDrawable bitmapDrawable = (BitmapDrawable)
		 * imageView .getDrawable(); Bitmap bitmap = bitmapDrawable.getBitmap();
		 * 
		 * // Save this bitmap to a file. File cacheDir =
		 * v.getContext().getExternalCacheDir(); File downloadingMediaFile = new
		 * File(cacheDir, "abc.png"); try { FileOutputStream out = new
		 * FileOutputStream( downloadingMediaFile);
		 * bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); out.flush();
		 * out.close(); } catch (IOException e) { e.printStackTrace(); }
		 * 
		 * // Now send it out to share Intent share = new Intent(
		 * android.content.Intent.ACTION_SEND); share.setType("image/*");
		 * share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" +
		 * downloadingMediaFile)); // share.putExtra(Intent.EXTRA_TITLE, //
		 * "my awesome caption in the EXTRA_TITLE field");
		 * share.putExtra(Intent.EXTRA_TEXT,
		 * "Click here: www.dummytrailhike.com");
		 * 
		 * // share.putExtra(Intent.EXTRA_SUBJECT, "Subject"); try {
		 * v.getContext().startActivity( Intent.createChooser(share,
		 * "Send Image.")); } catch (Exception ex) { ex.printStackTrace(); } //
		 * just for testing // String address =
		 * TGUtils.getCompleteAddressString( // v.getContext(), 38.0351291,
		 * -122.856799); // Toast.makeText(v.getContext(), address, //
		 * Toast.LENGTH_LONG).show(); return true; } });
		 */
		return convertView;
	}

	private void initializeViews(ViewHolder viewHolder, View convertView) {
		viewHolder.ivCoverPhoto = (ImageView) convertView
				.findViewById(R.id.ivCoverPhoto);
		/*viewHolder.ivDeleteStory = (ImageView) convertView
				.findViewById(R.id.ivDeleteStory);*/
		viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		viewHolder.tvRefs = (TextView) convertView.findViewById(R.id.tvRefs);
		viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		convertView.setTag(R.string.view_holder_key, viewHolder);
		viewHolder.tvLikes.setTag(R.string.view_holder_key, viewHolder);
		viewHolder.pbLoading = (ProgressBar) convertView
				.findViewById(R.id.pbLoading);
		viewHolder.dateTime = (TextView) convertView
				.findViewById(R.id.tvPostDateTime);
		viewHolder.ivCoverPhoto.setTag(R.string.view_holder_key, viewHolder);

		/*viewHolder.ivDeleteStory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TGStory s = (TGStory) v.getTag(R.string.object_key);
				s.deleteStory(null);
			}
		});*/
		viewHolder.ivShareHikeIcon = (Button) convertView
				.findViewById(R.id.ivShareIcon);
		viewHolder.ivShareHikeIcon.setTag(R.string.cover_photo_key,
				viewHolder.ivCoverPhoto);
		viewHolder.ivShareHikeIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// shareWaitDialog(v);
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
					// PNG compress is always done in SW. JPEG is faster and
					// uses HW when available.
					// Right solution is to create bitmap when impage is loaded
					// and make it a part of story
					// will slow loading of pic's a bit, but sharing will be
					// much faster. Another option is
					// to do it async , but problem is that it may slowdown app
					// or if it is done with
					// low priority user may click on share before bitmap is
					// ready - need mechanism to track
					// if bitmap is ready and also add shareWaitDialog
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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

	private void shareWaitDialog(View v) {

		shareWaitDialog = new ProgressDialog(v.getContext());
		shareWaitDialog.setTitle("Sharing..."); //
		// shareWaitDialog.setMessage(subject);
		shareWaitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		shareWaitDialog.show();

	}

	/*public void setDeleteActionAllowed(Boolean flag) {
		deleteActionAllowed = flag;
	}*/
}
