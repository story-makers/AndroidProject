package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.HikeArrayAdapter;
import com.storymakers.apps.trailguide.model.TGStory;

import eu.erikw.PullToRefreshListView;

public class HikesListFragment extends Fragment {
	private ArrayList<TGStory> stories;
	protected PullToRefreshListView lvHikesList;
	private HikeArrayAdapter hikesListAdapter;
	// private SwipeRefreshLayout swipeContainer;

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
		lvHikesList = (PullToRefreshListView) v.findViewById(R.id.lvHikesList);
		lvHikesList.setAdapter(hikesListAdapter);
		// setupScrollListener();
		// setupSwipeListener(v);
		// setUpListViewListeners();

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
			throw new ClassCastException(
					activity.toString()
							+ " must implement HikesListFragment.OnListItemClickListener");
		}
	}

	public void onListItemClicked(View v) {
		listener.onListItemClicked((TGStory) v.getTag(R.string.object_key));
	}

	public void addAll(List<TGStory> stories) {
		hikesListAdapter.addAll(stories);
	}

	public void clear() {
		hikesListAdapter.clear();
	}

	public void setDeleteActionAllowed(Boolean deleteActionAllowed) {
		hikesListAdapter.setDeleteActionAllowed(deleteActionAllowed);
	}

}
