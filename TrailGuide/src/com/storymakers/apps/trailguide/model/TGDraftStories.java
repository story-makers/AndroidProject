package com.storymakers.apps.trailguide.model;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.storymakers.apps.trailguide.interfaces.ProgressNotificationHandler;

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

	public TGStory createNewDraft(TGUser u, String title,
			final ProgressNotificationHandler handler) {
		if (handler != null)
			handler.beginAction();
		final TGStory s = TGStory.createNewStory(u, title);
		draftStories.add(s);
		s.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {

				s.saveData();
				if (handler != null)
					handler.endAction();
			}
		});

		return s;
	}
}
