package com.storymakers.apps.trailguide.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGFilter;

public class SearchFragmentDialog extends DialogFragment {
	private EditText fltSearchString;
	private EditText fltdistance;
	private EditText fltime;
	private Button btnFltSearch;
	private static TGFilter searchFilter;

	public static SearchFragmentDialog newInstance(TGFilter filter) {
		SearchFragmentDialog frag = new SearchFragmentDialog();
		Bundle args = new Bundle();
		args.putSerializable("filter", filter);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Search Hikes");
		View view = inflater.inflate(R.layout.fragment_filters, container);
		fltdistance = (EditText) view.findViewById(R.id.fltdistance);
		fltime = (EditText) view.findViewById(R.id.fltime);
		fltSearchString = (EditText) view.findViewById(R.id.fltSearchString);
		btnFltSearch = (Button) view.findViewById(R.id.btnFltSearch);
		btnFltSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TGFilter filter = new TGFilter("", "", "");
				filter.setDistance(fltdistance.getText().toString());
				filter.setDuration(fltime.getText().toString());
				filter.setSearchString(fltSearchString.getText().toString());
				dataPasser.onDataPass(filter);
				dismiss();
			}
		});
		searchFilter = (TGFilter) getArguments().getSerializable("filter");
		fltime.setText(searchFilter.getDuration());
		fltdistance.setText(searchFilter.getDistance());
		fltSearchString.setText(searchFilter.getSearchString());
		return view;
	}

	public interface OnDataPass {
		public void onDataPass(TGFilter filter);
	}

	OnDataPass dataPasser;

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		dataPasser = (OnDataPass) a;
	}
}
