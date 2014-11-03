package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.MapInfoWindowItemAdapter;
import com.storymakers.apps.trailguide.fragments.CustomMapFragment.OnMapReadyListener;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;

public class StoryMapFragment extends Fragment implements OnMapReadyListener {
	private static final double DEFAULT_LAT = 37.3858058;
	private static final double DEFAULT_LNG = -122.0808706;

	private TGStory story;
	private CustomMapFragment mapFragment;
	private GoogleMap map;
	private ArrayList<TGPost> posts;
	private Map<Marker, List<TGPost>> markerData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		markerData = new HashMap<Marker, List<TGPost>>();
	}

	@Override
	public void onMapReady() {
		initializeMap();
		getStory();
		getPosts(story, Color.rgb(0, 0, 255));
		if (getArguments().containsKey(getString(R.string.map_context_key))
				&& getArguments()
						.getString(getString(R.string.map_context_key)) == getString(R.string.create_hike_context)) {
			ArrayList<TGStory> referencedStories = story.getReferencedStories();
			for (TGStory refStory : referencedStories) {
				getPosts(refStory, Color.rgb(255, 0, 0));
			}
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story_map, container,
				false /* don't attach to container yet */);
		setupMapFragment();
		return v;
	}

	private void setupMapFragment() {
		mapFragment = CustomMapFragment.newInstance();
		// Begin the transaction
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flMapContainer, mapFragment, "map_fragment");
		// Execute the changes specified
		ft.commit();
	}

	private void initializeMap() {
		map = mapFragment.getMap();
		if (map != null) {
			//Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
			map.setMyLocationEnabled(true);
		} else {
			Toast.makeText(getActivity(), "Error - Map was null!!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void setInfoWindowAdapter() {
		// TODO: if needed set onInfoWindowClickListener to navigate to
		// the specific item in timeline.
		map.setInfoWindowAdapter(new MapInfoWindowAdapter(StoryMapFragment.this));
	}

	public void getPosts(TGStory story, final int color) {
		// replace logic with data from intent / bundle args
		story.getPosts(new PostListDownloadCallback() {
			@Override
			public void fail(String reason) {
				Log.e("ERROR", reason);
			}

			@Override
			public void done(List<TGPost> objs) {
				// We would like to filter reference story, preamble and metadata posts
				// for the hike's map fragment
				posts = (ArrayList<TGPost>) TGStory.filterPosts(objs,
						new HashSet<PostType>(Arrays.asList(PostType.REFERENCEDSTORY,
								PostType.PREAMBLE, PostType.METADATA)));
				zoomToHikeStartPoint();
				addPostsToMap(color);
				setInfoWindowAdapter();
			}
		});
	}

	private void zoomToHikeStartPoint() {
		// Start with the default LATLNG.
		LatLng latLng = new LatLng(DEFAULT_LAT, DEFAULT_LNG);
		LatLngBounds.Builder bc = new LatLngBounds.Builder();
		if (story != null) {
			// replace with hike start point location.
			ParseGeoPoint point = story.getLocation();
			if (point == null) {
				// find the first post with a location set on it.
				for (TGPost p : posts){
					
					if (p.getLocation() != null && (int)p.getLocation().getLatitude() != 0){
						
						point = p.getLocation();
						bc.include(new LatLng(point.getLatitude(), point.getLongitude()));
						
					}
				}
			}
			
			
		}
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
		
	}

	private void getStory() {
		String storyId = getArguments().getString("hike");
		story = RemoteDBClient.getStoryById(storyId);
	}

	private void addPostsToMap(int color) {
		if (posts == null) {
			return;
		}
		ParseGeoPoint lastGeoPoint = null;
		Marker lastMarker = null;
		PolylineOptions path = new PolylineOptions().geodesic(true)
				.color(color);
		int firstPost = 0;
		boolean firstPostSet = false;
		for (int i = 0; i < posts.size(); i++) {
			ParseGeoPoint point = posts.get(i).getLocation();
			if (point == null
					|| (point.getLatitude() == 0.0 && point.getLongitude() == 0.0)) {
				continue;
			}
			if (!firstPostSet) {
				firstPost = i;
				firstPostSet = true;
			}
			if (i == firstPost) {
				ArrayList<TGPost> list = new ArrayList<TGPost>();
				list.add(posts.get(i));
				if (posts.get(i).shouldShowOnMap()) {
					lastMarker = map
							.addMarker(new MarkerOptions()
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.ic_map_marker_begin_end))
									.anchor(0.0f, 0.8f) // Anchors the marker on the
														// bottom left
									.position(
											new LatLng(point.getLatitude(), point
													.getLongitude())));
					

					lastGeoPoint = point;
					markerData.put(lastMarker, list);
				}
				path.add(new LatLng(point.getLatitude(), point.getLongitude()));
			} /*else if (i == posts.size() - 1) {
				if (lastGeoPoint.distanceInMilesTo(point) > 0.05) {
					lastMarker = map
							.addMarker(new MarkerOptions()
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.ic_map_marker_begin_end))
									.anchor(0.0f, 0.0f) // Anchors the marker on
														// the bottom left
									.position(
											new LatLng(point.getLatitude(),
													point.getLongitude())));
					lastGeoPoint = point;
					ArrayList<TGPost> list = new ArrayList<TGPost>();
					list.add(posts.get(i));
					markerData.put(lastMarker, list);
					path.add(new LatLng(point.getLatitude(), point
							.getLongitude()));
				} else {
					markerData.get(lastMarker).add(posts.get(i));
				}
			}*/ else {
				if (lastGeoPoint.distanceInMilesTo(point) > 0.005) {
					TGPost p = posts.get(i);
					
					ArrayList<TGPost> list = new ArrayList<TGPost>();
					
					list.add(p);
					if (p.shouldShowOnMap()){
						Marker cur_marker = map.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_map_marker))
						.anchor(0.5f, 1.0f) // Anchors the marker on the
											// bottom left
						.position(
								new LatLng(point.getLatitude(), point
										.getLongitude())));
				
						lastMarker = cur_marker;
						lastGeoPoint = point;
						markerData.put(lastMarker, list);
					}
					path.add(new LatLng(point.getLatitude(), point
							.getLongitude()));
				} else {
					if(posts.get(i).shouldShowOnMap()){
						markerData.get(lastMarker).add(posts.get(i));
					}
					path.add(new LatLng(point.getLatitude(), point
							.getLongitude()));
				}
			}
		}
		map.addPolyline(path);
	}

	public List<TGPost> getMarkerData(Marker marker) {
		return markerData.get(marker);
	}

	public class MapInfoWindowAdapter implements InfoWindowAdapter {
		// TODO: Fix the styling and first time not loading image issue.
		private StoryMapFragment storyMapFragment;

		public MapInfoWindowAdapter(StoryMapFragment fragment) {
			this.storyMapFragment = fragment;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// Getting view from the layout file info_window_layout
			LayoutInflater inflater = (LayoutInflater) storyMapFragment
					.getActivity().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			View v = (View) inflater.inflate(R.layout.map_info_window, null);
			ListView lvPosts = (ListView) v.findViewById(R.id.lvPostsInfo);
			MapInfoWindowItemAdapter customadapter = new MapInfoWindowItemAdapter(
					storyMapFragment.getActivity(),
					storyMapFragment.getMarkerData(marker));
			lvPosts.setAdapter(customadapter);
			// Returning the view containing InfoWindow contents
			return v;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}
}
