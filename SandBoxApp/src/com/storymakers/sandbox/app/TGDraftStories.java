package com.storymakers.sandbox.app;

import java.util.ArrayList;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class TGDraftStories {
	private ArrayList<TGStory> draftStories;
	private static TGDraftStories instance;
	private TGDraftStories (){
		draftStories = new ArrayList<TGStory>();
		//ParseQuery<TGStory> q = ParseQuery.
	}
	
	public static TGDraftStories getInstance() {
		if (instance == null)
		instance = new TGDraftStories();
		return instance;
	}
	
	public TGStory createNewDraft(TGUser u, String title){
		// Need to 
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		String draftStoryId = "ydsfkdsjfs";
		query.fromLocalDatastore();
		try {
			TGStory s = query.get(draftStoryId);
			if (s != null)
				return s;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TGStory s = TGStory.createNewStory(u, title);
		return s;
	}
}
