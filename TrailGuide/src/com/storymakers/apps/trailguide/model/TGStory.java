package com.storymakers.apps.trailguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
		DRAFT, COMPLETE, DELETED
	};

	public final String KEY_BEGINDATE = "beginDate";
	public final String KEY_ENDDATE = "endDate";
	public final String KEY_POST_SEQUENCE_NUM = "postSequenceGenerationId";
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
		story.setPostSequenceGeneration(5);
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
			break;
		case DELETED:
			put("state", "DELETED");
			break;
		default:
			break;
		}

	}

	public StoryType getState() {
		String s = getString("state");
		if (s.equals("COMPLETE"))
			return StoryType.COMPLETE;
		else if (s.equals("DRAFT"))
			return StoryType.DRAFT;
		else
			return StoryType.DELETED;
	}

	public String getTitle() {
		return TGUtils.capitalizeSentence(getString("title"));
	}

	public void setTitle(String title) {
		title = TGUtils.capitalizeSentence(title);
		this.title = title;
		put("title", title);
	}

	public ParseUser getCreator() {
		return (ParseUser) get("creator");
	}

	public void setCreator(TGUser creator) {
		this.creator = creator.getUserIdentity();
		put("creator", this.creator);
	}

	public long getLikes() {
		if (!has("likes"))
			return 0;
		return getLong("likes");
	}

	private long getPostSequenceGeneration() {
		increment(KEY_POST_SEQUENCE_NUM);
		return getLong(KEY_POST_SEQUENCE_NUM);
	}

	private void setPostSequenceGeneration(long num) {
		put(KEY_POST_SEQUENCE_NUM, num);
	}

	public void setLikes(long likes) {
		this.likes = likes;
		put("likes", likes);
	}

	public long getRefs() {
		if (!has("refs"))
			return 0;
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
		RemoteDBClient.getPostsForTGStory(this, new PostListDownloadCallback() {

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

	public static List<TGPost> filterPosts(List<TGPost> posts,
			Set<TGPost.PostType> filterTypes) {
		ArrayList<TGPost> filteredPosts = new ArrayList<TGPost>();
		for (TGPost post : posts) {
			if (!filterTypes.contains(post.getType())) {
				filteredPosts.add(post);
			}
		}
		return filteredPosts;
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

	public void addPost(TGPost p, ProgressNotificationHandler progress) {
		if (hasPreamble && p.getType() == PostType.PREAMBLE) {
			Log.e("FATAL", "Die. You cannot have multiple preambles");
			return;
		}
		if (p.getType() == PostType.REFERENCEDSTORY) {
			this.referenced_stories.add(p.getReferencedStory());
			/* TO BE FIXED: Add logic for de duplication of referenced story */
		}

		this.posts.add(p);
		p.setStory(this);

		if (p.getType() == PostType.PREAMBLE) {
			p.setSequenceId(1);
			hasPreamble = true;
		} else
			p.setSequenceId(getPostSequenceGeneration());

		p.saveData(progress);
		if (p.getType() == PostType.PHOTO && getCoverPhotoURL() == null) {
			setCoverPhotoURL(p.getPhoto_url());
		}
		saveData();
	}

	/* returns number of items to save. */
	public int completeStory(final UploadProgressHandler uploadProgressHandler) {
		/*
		 * Add a preamble post when the story is complete to render the cover
		 * photo and details of story.
		 */
		
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
		TGDraftStories.getInstance().completeDraftStory(this);
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


	private void updateDistance() {
		// distance in miles. Need to save somewhere
		double distance = 0;
		ParseGeoPoint prevGp = null;
		Iterator<TGPost> it = posts.iterator();
		while (it.hasNext()) {
			TGPost post = it.next();
			ParseGeoPoint gp = post.getLocation();
			if (gp != null) {
				// is location a start point?
				if (prevGp != null) {
					distance += gp.distanceInMilesTo(prevGp);
				}
				prevGp = gp;
			}
		}
	}

	private void computeDuration() {
		if (endDate != null && beginDate != null) {
			long diff = endDate.getTime() - beginDate.getTime();

			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			// long diffDays = diff / (24 * 60 * 60 * 1000);
			String hikeDuration = "";
			if (diffHours != 0) {
				hikeDuration += diffHours + " hrs";
			}
			if (diffMinutes != 0) {
				hikeDuration += " " + diffMinutes + "mins";
			}
		}
	}

	private void verifyCoverPhoto() {
		if (getCoverPhotoURL() != null && getCoverPhotoURL().length() > 0)
			return;
		
		for (TGPost p : this.posts) {
			if (p.getType() == PostType.PHOTO) {
				if (p.getPhoto_url() != null & p.getPhoto_url().length() > 0) {
					this.setCoverPhotoURL(p.getPhoto_url());
				}
			}
		}
		if (getCoverPhotoURL() == null) {
			setCoverPhotoURL("http://codepath.com/images/bg/footer-bg.png");
			return;
		}
	}

	public void saveData() {
		saveData(null);
	}

	public void saveData(final ProgressNotificationHandler handler) {
		if (handler != null)
			handler.beginAction();
		if (getState() == StoryType.DRAFT) {
			pinAllInBackground(this.posts);
			pinInBackground();
			return;
		}
		saveAllInBackground(this.posts);
		saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {
				// TODO Auto-generated method stub
				if (handler != null)
					handler.endAction();
			}
		});
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
		if (!has("coverPhotoURL"))
			return null;
		return getString("coverPhotoURL");
	}

	public void setCoverPhotoURL(String coverPhotoURL) {
		if (coverPhotoURL == null)
			coverPhotoURL = "https://farm4.staticflickr.com/3852/15149838402_a0878021dc_z.jpg";
		put("coverPhotoURL", coverPhotoURL);
	}

	public boolean isCompleted() {
		return (getState() == StoryType.COMPLETE);
	}

	public boolean isDraft() {
		return (getState() == StoryType.DRAFT);
	}

	public String getDisplayDate() {
		// Date to display on hike story list
		Date d = getEndDate();
		if (d == null){
			d = getBeginDate();
		}
		return TGUtils.DATE_FORMATTER.format(d);
	}

	public void deleteStory(ProgressNotificationHandler progress) {
		setState(StoryType.DELETED);
		saveData(progress);
	}

}
