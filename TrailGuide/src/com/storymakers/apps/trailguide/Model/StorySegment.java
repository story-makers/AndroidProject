package com.storymakers.apps.trailguide.Model;

public class StorySegment {
	private String imageUrl;
	private String text;
	private int numLikes;
	private int numPins;
	private SegmentTypes storySegmentType;

	public enum SegmentTypes {
		HomeSegment, TextNote, MapDescr
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public int getNumPins() {
		return numPins;
	}

	public void setNumPins(int numPins) {
		this.numPins = numPins;
	}

	public SegmentTypes getStorySegmentType() {
		return storySegmentType;
	}

	public void setStorySegmentType(SegmentTypes storySegmentType) {
		this.storySegmentType = storySegmentType;
	}

	public StorySegment(String imageUrl, String text, int numLikes,
			int numPins, SegmentTypes storySegmentType) {
		super();
		this.imageUrl = imageUrl;
		this.text = text;
		this.numLikes = numLikes;
		this.numPins = numPins;
		this.storySegmentType = storySegmentType;
	}

	public StorySegment(String text, SegmentTypes storySegmentType) {

		this.imageUrl = null;
		this.text = text;
		this.numLikes = 0;
		this.numPins = 0;
		this.storySegmentType = storySegmentType;
	}

	public StorySegment(String text, SegmentTypes storySegmentType,
			String imgUrl) {

		this.imageUrl = imgUrl;
		this.text = text;
		this.numLikes = 0;
		this.numPins = 0;
		this.storySegmentType = storySegmentType;
	}

}
