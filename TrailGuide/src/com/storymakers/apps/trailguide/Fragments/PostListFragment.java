package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.StoryPostAdapter;
import com.storymakers.apps.trailguide.model.TGPost;

public class PostListFragment extends Fragment {
	private ArrayList<TGPost> posts;
	private StoryPostAdapter storyPostAdapter;
	private ListView lvStoryPosts;

	// Use this after implementing parcelable.
	/* public static PostListFragment newInstance(ArrayList<TGPost> postsList) {
		PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("posts", postsList);
        fragment.setArguments(args);
        return fragment;
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		posts = new ArrayList<TGPost>();
		storyPostAdapter = new StoryPostAdapter(getActivity(), posts);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_post_list, container,
				false);
		
		lvStoryPosts = (ListView) v.findViewById(R.id.lvStoryPosts);
		lvStoryPosts.setAdapter(storyPostAdapter);

		// pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
		/*lvStoryPosts.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// pbLoading.setVisibility(ProgressBar.VISIBLE);
				// customLoadMoreDataFromApi(page);
				// or customLoadMoreDataFromApi(totalItemsCount);
			}
		});*/
		
		return v;
	}

	public void addAll(List<TGPost> postsList) {
		storyPostAdapter.addAll(postsList);
	}
	public void addPost(TGPost p) {
		storyPostAdapter.add(p);
	}
	/*public void populateTimeLine(long max_id) {
		StorySegment seg1 = new StorySegment("text1 one two three",
				StorySegment.SegmentTypes.TextNote);
		StorySegment seg2 = new StorySegment("text1 one two three",
				StorySegment.SegmentTypes.HomeSegment,
				"http://2.gravatar.com/avatar/858dfac47ab8176458c005414d3f0c36?s=256&d=&r=G");
		StorySegment seg3 = new StorySegment("text1 one two three",
				StorySegment.SegmentTypes.MapDescr,
				"http://2.gravatar.com/avatar/858dfac47ab8176458c005414d3f0c36?s=256&d=&r=G");
		List<StorySegment> segments = new ArrayList<StorySegment>();
		segments.add(seg1);
		segments.add(seg2);
		segments.add(seg3);
		aSegments.addAll(segments);
	}*/
}
