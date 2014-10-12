package com.storymakers.sandbox.app;

import java.util.Date;

import android.content.Context;
import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("TGPost")
public class TGPost extends ParseObject {
	public enum PostType {
		METADATA, NOTE, LOCATION, PHOTO
	}

	PostType type;
	private String note;
	private Date create_time;
	private ParseGeoPoint location;
	private String photo_url;
	private ParseFile photo;
	private TGStory story;

	public TGPost() {
	}

	public static TGPost createNewPost(TGStory story, PostType type) {
		final TGPost post = new TGPost();
		post.type = type;
		post.setNote("");
		post.setPhoto_url("");

		post.setCreate_time(new Date(System.currentTimeMillis()));
		post.story = story;
		post.setStory(story);

		return post;
	}

	public String getNote() {
		return getString("note");
	}

	public void setNote(String note) {
		put("note", note);
	}

	public String getCreate_time() {
		return getString("create_time");
	}

	public void setCreate_time(Date create_time) {
		put("create_time", create_time);
	}

	public ParseGeoPoint getLocation() {
		return (ParseGeoPoint) get("location");
	}

	public void setLocation(float[] latlong) {
		if (latlong != null) {
			ParseGeoPoint gp = new ParseGeoPoint(latlong[0], latlong[1]);
			put("location", gp);
		}
	}

	public String getPhoto_url() {
		return getString("photo_url");
	}

	public void setPhoto_url(String photo_url) {
		put("photo_url", photo_url);
	}

	public ParseFile getPhoto() {
		return (ParseFile) get("photo_img");
	}

	public void setPhotoFromUri(Context ctx, Uri photouri) {
		setPhoto(TGUtils.getBytesFromUri(ctx, photouri));
		setLocation(TGUtils.getGeoLocationFromPhoto(photouri.getPath()));
	}
	public void setPhoto(byte[] imageData) {
		ParseFile ph = new ParseFile("ph" + Long.toString(System.currentTimeMillis()) + ".jpeg", imageData);
		put("photo_img", ph);
	}

	public void setStory(TGStory s) {
		put("story", s);
		this.story = s;
	}

	public TGStory getStory() {
		return (TGStory) get("story");
	}

	public void saveData() {
		ParseFile p = getPhoto();
		if (p != null) {
			p.saveInBackground();
		}
		saveInBackground();
	}
}
