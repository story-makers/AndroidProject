package com.storymakers.apps.trailguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.storymakers.apps.trailguide.interfaces.ProgressNotificationHandler;
import com.storymakers.apps.trailguide.interfaces.UploadProgressHandler;
import com.storymakers.apps.trailguide.model.TGPost.PostListDownloadCallback;
import com.storymakers.apps.trailguide.model.TGPost.PostType;

@ParseClassName("TGStory")
public class TGStory extends ParseObject {
	public enum StoryType {
		DRAFT, COMPLETE
	};

	public final String KEY_BEGINDATE = "beginDate";
	public final String KEY_ENDDATE = "endDate";
	// VP: TODO need to follow this method for all fields

	private String title;
	private ParseUser creator;
	private long likes, refs;
	private ParseGeoPoint location;
	private ArrayList<TGPost> posts;
	private ArrayList<TGStory> referenced_stories;
	private Date beginDate, endDate;
	private String coverPhotoURL;
	private Boolean hasPreamble;

	// DO Not modify. required by Parse SDK
	public TGStory() {
		hasPreamble = false;
		posts = new ArrayList<TGPost>();
		referenced_stories = new ArrayList<TGStory>();
	}

	public static TGStory createNewStory(TGUser creator, String title) {
		final TGStory story = new TGStory();
		story.setState(StoryType.DRAFT);
		story.setTitle(title);
		story.setCreator(creator);
		story.setLikes(0);
		story.setRefs(0);
		/*
		 * LocationCallback cb = new LocationCallback() {
		 * 
		 * @Override public void done(ParseGeoPoint arg0, ParseException arg1) {
		 * if (arg0 != null){ story.setLocation(arg0); story.saveInBackground();
		 * } } };
		 */
		story.setBeginDate(new Date());
		// ParseGeoPoint.getCurrentLocationInBackground(10000, cb);

		return story;
	}

	private void setState(StoryType t) {
		switch (t) {
		case DRAFT:
			put("state", "DRAFT");
			break;
		case COMPLETE:
			put("state", "COMPLETE");
		default:
			break;
		}

	}

	public StoryType getState() {
		String s = getString("state");
		if (s.equals("COMPLETE"))
			return StoryType.COMPLETE;
		else
			return StoryType.DRAFT;
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		this.title = title;
		put("title", title);
	}

	public TGUser getCreator() {
		return (TGUser) get("creator");
	}

	public void setCreator(TGUser creator) {
		this.creator = creator.getUserIdentity();
		put("creator", this.creator);
	}

	public long getLikes() {
		return getLong("likes");
	}

	public void setLikes(long likes) {
		this.likes = likes;
		put("likes", likes);
	}

	public long getRefs() {
		return getLong("refs");
	}

	public void setRefs(long refs) {
		this.refs = refs;
		put("refs", refs);
	}

	public ParseGeoPoint getLocation() {
		return (ParseGeoPoint) get("location");
	}

	public void setLocation(ParseGeoPoint location) {
		this.location = location;
		put("location", location);
	}

	public void getPosts(final PostListDownloadCallback handle) {

		this.posts = new ArrayList<TGPost>();
		RemoteDBClient.getPostsForStory(this, new PostListDownloadCallback() {

			@Override
			public void fail(String reason) {
				Log.e("ERROR", reason);
				if (handle != null)
					handle.fail(reason);
			}

			@Override
			public void done(List<TGPost> objs) {
				TGStory.this.posts.addAll(objs);
				for (TGPost p : objs) {
					if (p.getType() == PostType.REFERENCEDSTORY) {
						TGStory.this.referenced_stories.add(p
								.getReferencedStory());
					}

					if (p.getType() == PostType.PREAMBLE) {
						hasPreamble = true;
					}
				}
				if (handle != null) {
					handle.done(objs);
				}
			}
		});

	}

	public ArrayList<TGPost> getPosts() {
		return this.posts;
	}

	public ArrayList<TGStory> getReferencedStories() {
		return this.referenced_stories;
	}

	public void addLike(ProgressNotificationHandler progress) {
		if (progress != null)
			progress.beginAction();
		increment("likes");
		saveData();
		if (progress != null)
			progress.endAction();
	}

	public void addRefs(ProgressNotificationHandler progress) {
		if (progress != null)
			progress.beginAction();
		increment("refs");
		saveData();
		if (progress != null)
			progress.endAction();
	}

	private long getGeneratedSequenceId() {
		return (1 + this.posts.size());
	}

	public void addPost(TGPost p, ProgressNotificationHandler progress) {
		if (hasPreamble && p.getType() == PostType.PREAMBLE) {
			Log.e("FATAL", "Die. You cannot have multiple preambles");
			return;
		}

		this.posts.add(p);
		p.setStory(this);

		if (p.getType() == PostType.PREAMBLE) {
			p.setSequenceId(1);
			hasPreamble = true;
		} else
			p.setSequenceId(getGeneratedSequenceId());
		
		p.saveData(progress);
		if (p.getType() == PostType.PHOTO && getCoverPhotoURL() == null) {
			setCoverPhotoURL(p.getPhoto_url());
		}
		if (p.getType() == PostType.REFERENCEDSTORY)
			this.referenced_stories.add(p.getReferencedStory());
	}

	/* returns number of items to save. */
	public int completeStory(final UploadProgressHandler uploadProgressHandler) {
		/*
		 * Add a preamble post when the story is complete to render the cover
		 * photo and details of story.
		 */
		addPreamble();
		/* add logic to compute hike time */
		computeDuration();
		/* add loop over posts to find cover photos */
		verifyCoverPhoto();
		/* add logic for finding distance in hike */
		updateDistance();

		setState(StoryType.COMPLETE);
		setEndDate(new Date());
		final int total_items = 1 + this.posts.size(); // for the story itself
		SaveCallback cb = new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (uploadProgressHandler != null)
					uploadProgressHandler.progress(1);
			}
		};

		saveAllInBackground(this.posts, cb);
		saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {

				if (uploadProgressHandler != null) {
					uploadProgressHandler.complete();
				}
			}
		});
		return total_items;
	}

	private void addPreamble() {
		TGPost post = TGPost.createNewPost(this, TGPost.PostType.PREAMBLE);
		this.addPost(post, null);
	}

	private void updateDistance() {
		// TODO Auto-generated method stub

	}

	private void computeDuration() {
		// TODO Auto-generated method stub

	}

	private void verifyCoverPhoto() {
		if (getCoverPhotoURL().length() > 0)
			return;
		for (TGPost p : this.posts) {
			if (p.getType() == PostType.PHOTO) {
				if (p.getPhoto_url() != null & p.getPhoto_url().length() > 0) {
					this.setCoverPhotoURL(p.getPhoto_url());
				}
			}
		}
	}

	public void saveData() {
		if (getState() == StoryType.DRAFT) {
			pinAllInBackground(this.posts);
			pinInBackground();
			return;
		}
		saveAllInBackground(this.posts);
		saveInBackground();
	}

	public Date getBeginDate() {
		return getDate(KEY_BEGINDATE);
	}

	public void setBeginDate(Date beginDate) {
		put(KEY_BEGINDATE, beginDate);
	}

	public Date getEndDate() {
		return getDate(KEY_ENDDATE);
	}

	public void setEndDate(Date endDate) {
		put(KEY_ENDDATE, endDate);
	}

	public String getCoverPhotoURL() {
		return getString("coverPhotoURL");
	}

	public void setCoverPhotoURL(String coverPhotoURL) {
		if (coverPhotoURL == null)
			coverPhotoURL = "https://farm4.staticflickr.com/3852/15149838402_a0878021dc_z.jpg";
		put("coverPhotoURL", coverPhotoURL);
	}

}
