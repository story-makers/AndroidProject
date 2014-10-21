package com.storymakers.apps.trailguide.model;

import java.io.Serializable;

public class TGFilter implements Serializable {
	private static final long serialVersionUID = -7494484270278174609L;
	private String distance;
	private String duration;
	private String searchString;

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public TGFilter(String distance, String duration, String searchString) {
		super();
		this.distance = distance;
		this.duration = duration;
		this.searchString = searchString;
	}

}
