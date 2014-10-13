package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.HikeArrayAdapter;

public class HikesListFragment extends Fragment {
	private ArrayList<TGStory> stories;
	private ListView lvHikesList;
	private HikeArrayAdapter hikesListAdapter;
	//private SwipeRefreshLayout swipeContainer;

	private OnListItemClickListener listener;

	public interface OnListItemClickListener {
		public void onListItemClicked(TGStory story);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_hikes_list, container,
				false /* don't attach to container yet */);
		// Assign view preferences
		lvHikesList = (ListView) v.findViewById(R.id.lvHikesList);
		lvHikesList.setAdapter(hikesListAdapter);
		//setupScrollListener();
		//setupSwipeListener(v);
		//setUpListViewListeners();
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		stories = new ArrayList<TGStory>();
		hikesListAdapter = new HikeArrayAdapter(getActivity(), stories);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnListItemClickListener) {
			listener = (OnListItemClickListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement TweetsListFragment.OnImageClickListener");
		}
	}

	public void addAll(ArrayList<TGStory> stories) {
		hikesListAdapter.addAll(stories);
	}

	// Dummy classes until we have the model
	public static class TGStory {
		public ArrayList<TGPost> posts;
		public int numLikes;
		public int numPins;
		public String title;
		public String coverPhoto;
		public TGStory(){}
	}

	public static class TGPost {
		String content; //url or note
		int type; //0 - url, 1 - note
		double lat;
		double lng;
		public TGPost(String s, int t, double la, double lo) {
			content = s;
			type = t;
			lat = la;
			lng = lo;
		}
	}
}
