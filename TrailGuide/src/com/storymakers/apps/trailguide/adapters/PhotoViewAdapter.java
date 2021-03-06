package com.storymakers.apps.trailguide.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGPost;

public class PhotoViewAdapter extends PagerAdapter {
	private List<TGPost> posts;
	private int count;
	private LayoutInflater inflater;
	private Activity activity;

	public PhotoViewAdapter(Activity act, List<TGPost> postlist){
		activity = act;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		posts = postlist;
		count = postlist.size();
		
	
	}
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == (RelativeLayout)arg1);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imgDisplay;
		TextView tvPhotoNote;
		TGPost p = posts.get(position);
		View v = inflater.inflate(R.layout.layout_photo_viewer, container, false);
		imgDisplay = (ImageView) v.findViewById(R.id.imgDisplay);
		final ProgressBar pbloading = (ProgressBar) v.findViewById(R.id.pbLoading);
		pbloading.setVisibility(View.VISIBLE);
		imgDisplay.setImageResource(R.drawable.transparent_shape);
		
		ImageLoader.getInstance().displayImage(p.getPhoto_url(), imgDisplay, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				pbloading.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				pbloading.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				pbloading.setVisibility(View.INVISIBLE);
				
			}
		});
		tvPhotoNote = (TextView) v.findViewById(R.id.tvPhotoNote);
		tvPhotoNote.setText(p.getNote());
		((ViewPager)container).addView(v);
		return v;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout)object);
	}
}
