package com.storymakers.sandbox.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationFragment extends Fragment {
	Button btnAddPhoto, btnUIPlay, btnShowMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_navigation, container);
		btnAddPhoto = (Button) v.findViewById(R.id.btnUploadPicture);
		btnAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), UploadPhotoActivity.class);
				startActivity(i);
			}
		});
		btnShowMap = (Button) v.findViewById(R.id.btnShowMap);
		btnShowMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), MapsPlaygroundActivity.class);
				startActivity(i);
			}
		});
		btnUIPlay = (Button) v.findViewById(R.id.btnUIPlay);
		btnUIPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), UIPlayGroundActivity.class);
				startActivity(i);
			}
		});
		return v;
	}
}
