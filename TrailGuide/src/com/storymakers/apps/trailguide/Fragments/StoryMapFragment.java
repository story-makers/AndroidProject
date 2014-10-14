package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.MapInfoWindowItemAdapter;
import com.storymakers.apps.trailguide.model.TGPost;

public class StoryMapFragment extends Fragment {
	private SupportMapFragment mapFragment;
	private GoogleMap map;
	private ArrayList<TGPost> posts;
	private Map<Marker, List<TGPost>> markerData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		markerData = new HashMap<Marker, List<TGPost>>();
		getPosts();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story_map, container,
				false /* don't attach to container yet */);
		mapFragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
		if (mapFragment != null) {
			map = mapFragment.getMap();
			if (map != null) {
				Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
				map.setMyLocationEnabled(true);
				zoomToHikeStartPoint();
				addPostsToMap();
				map.setInfoWindowAdapter(new MapInfoWindowAdapter(this));
				// TODO: if needed set onInfoWindowClickListener to navigate to the specific item in timeline.
			} else {
				Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
		}
		return v;
	}



	private void getPosts() {
		// replace logic with data from intent / bundle args
		posts = new ArrayList<TGPost>();
		posts.add(new TGPost(38.0423209, -122.8579315));
		posts.add(new TGPost(38.0379302,-122.8564391));
		posts.add(new TGPost(38.0362427,-122.8558596));
		posts.add(new TGPost(38.0351291,-122.856799));
		posts.add(new TGPost(38.0283639,-122.8588262));
		posts.add(new TGPost(38.0219532,-122.8579237));
		posts.add(new TGPost(38.0191769,-122.8565391));
		posts.add(new TGPost(38.0180459,-122.8582825));
		posts.add(new TGPost(38.0180459,-122.8582825));
		posts.add(new TGPost(38.015647,-122.8583851));
		posts.add(new TGPost(38.015647,-122.8583851));
		posts.add(new TGPost(38.0185489,-122.8625654));
	}

	private void zoomToHikeStartPoint() {
		ParseGeoPoint point = posts.get(0).getLocation();
		if (point != null) {
			LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
			map.animateCamera(cameraUpdate);
		}
	}

	private void addPostsToMap() {
		ParseGeoPoint lastGeoPoint = null;
		Marker lastMarker = null;
		PolylineOptions path = new PolylineOptions().geodesic(true);
		for (int i = 0; i < posts.size(); i++) {
			ParseGeoPoint point = posts.get(i).getLocation();
			if (point == null) {
				continue;
			}
			if (i == 0) {
				lastMarker = map.addMarker(new MarkerOptions()
        				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_begin_end))
        				.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
        				.position(new LatLng(point.getLatitude(), point.getLongitude())));
				lastGeoPoint = point;
				ArrayList<TGPost> list = new ArrayList<TGPost>();
				list.add(posts.get(i));
				markerData.put(lastMarker, list);
				path.add(new LatLng(point.getLatitude(), point.getLongitude()));
			} else if (i == posts.size() - 1) {
				if (lastGeoPoint.distanceInMilesTo(point) > 0.05) {
					lastMarker = map.addMarker(new MarkerOptions()
		    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_begin_end))
		    				.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		    				.position(new LatLng(point.getLatitude(), point.getLongitude())));
					lastGeoPoint = point;
					ArrayList<TGPost> list = new ArrayList<TGPost>();
					list.add(posts.get(i));
					markerData.put(lastMarker, list);
					path.add(new LatLng(point.getLatitude(), point.getLongitude()));
				} else {
					markerData.get(lastMarker).add(posts.get(i));
				}
			} else {
				if (lastGeoPoint.distanceInMilesTo(point) > 0.05) {
					lastMarker = map.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
							.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
							.position(new LatLng(point.getLatitude(), point.getLongitude())));
					lastGeoPoint = point;
					ArrayList<TGPost> list = new ArrayList<TGPost>();
					list.add(posts.get(i));
					markerData.put(lastMarker, list);
					path.add(new LatLng(point.getLatitude(), point.getLongitude()));
				} else {
					markerData.get(lastMarker).add(posts.get(i));
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
	        LayoutInflater inflater = (LayoutInflater) storyMapFragment.getActivity().getSystemService(
	        		Context.LAYOUT_INFLATER_SERVICE );
	        View v = (View)inflater.inflate(R.layout.infowindow, null);

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
