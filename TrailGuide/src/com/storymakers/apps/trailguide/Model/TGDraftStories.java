package com.storymakers.apps.trailguide.model;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.model.TGStory.StoryType;

public class TGDraftStories {
	private ArrayList<TGStory> draftStories;
	private static TGDraftStories instance;

	private TGDraftStories() {
		draftStories = new ArrayList<TGStory>();
	}
	public void addStories(List<TGStory> objs) {
		draftStories.addAll(objs);
	}
	public static TGDraftStories getInstance() {
		if (instance == null) {
			instance = new TGDraftStories();
			RemoteDBClient.getDraftStoriesByUser(new FindCallback<TGStory>() {
				
				@Override
				public void done(List<TGStory> objects, ParseException e) {
					if (e==null) {
						instance.addStories(objects);
					}
					
				}
			});
		}
		return instance;
	}

	public TGStory getDraftStory() {
		TGStory retval = null;
		if (draftStories.size() > 0) {
			retval = draftStories.get(0);
		}
		return retval;
	}

	public Boolean hasDraft() {
		return (draftStories.size() > 0);
	}

	public TGStory createNewDraft(TGUser u, String title) {
		// Need to check in existing array if there is one with same title
		for (TGStory so : draftStories) {
			if (so.getTitle().equals(title))
				return so;
		}
		final TGStory s = TGStory.createNewStory(u, title);
		draftStories.add(s);
		s.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				RemoteDBClient.saveDraftStoryRef(s, null);
				s.saveData();
			}
		});

		return s;
	}
}
