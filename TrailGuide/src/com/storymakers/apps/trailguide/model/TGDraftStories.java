package com.storymakers.apps.trailguide.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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
		for (TGStory o : objs){
			draftStories.add(o);
			o.saveData();
		}
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

	public void getDraftStories(final FindCallback<TGStory> callback) {
		if (draftStories.size() > 0){
			callback.done(draftStories, null);
			return;
		}
		RemoteDBClient.getDraftStoriesByUser(new FindCallback<TGStory>() {

			@Override
			public void done(List<TGStory> arg0, ParseException arg1) {
				if (arg1 == null) {
					addStories(arg0);
				}
				
				callback.done(arg0, arg1);

			}
		});
	}

	public void completeDraftStory(TGStory s) {
		for (TGStory i : draftStories) {
			if (i.getObjectId() == s.getObjectId()) {
				Log.d("DRAFTSTORY",
						"Removing draft story with object id: "
								+ s.getObjectId());
				draftStories.remove(i);
			}
		}
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
