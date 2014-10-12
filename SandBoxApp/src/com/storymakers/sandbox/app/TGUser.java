package com.storymakers.sandbox.app;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TGUser {
	private ParseUser _user;
	private ArrayList<TGStory> stories;

	/* Required for Parse SDK. Do not modify anything here */
	public TGUser(ParseUser u) {
		_user = u;
		getStories();
	}

	public String getUserEmail() {
		return _user.getString("email");
	}

	public ParseUser getUserIdentity() {
		return _user;
	}

	public void setUserEmail(String email) {
		_user.put("email", email);
	}

	public String getUserName() {
		return _user.getString("username");
	}

	public void setUserName(String userName) {
		_user.put("username", userName);
	}

	public void saveData() {
		_user.saveInBackground();
	}

	private void getStories() {
		stories = new ArrayList<TGStory>();
		ParseQuery<TGStory> query = ParseQuery.getQuery(TGStory.class);
		query.whereEqualTo("creator", _user);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<TGStory>() {

			@Override
			public void done(List<TGStory> arg0, ParseException arg1) {
				if (arg1 != null) {
					arg1.printStackTrace();
					return;
				}
				for (TGStory o : arg0) {
					stories.add(o);
					Log.i("INFO", o.toString());
				}

			}
		});
	}
	
	public ArrayList<TGStory> getAllStories() {
		return stories;
	}

}
