package com.storymakers.sandbox.app;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TGUser")
public class TGUser extends ParseObject {
	private ParseUser user;

	/* Required for Parse SDK. Do not modify anything here */
	public TGUser() {
	}

	public static TGUser createNewUser(ParseUser u, String name, String email) {
		TGUser t = new TGUser();
		t.setUserObj(u);
		u.put("username", name);
		t.setUserName(name);
		u.put("email", email);
		t.setUserEmail(email);

		return t;
	}

	public void setUserObj(ParseUser u) {
		put("user_obj", u);
	}

	public ParseUser getUserObj() {
		return (ParseUser) get("user_obj");
	}

	public String getUserEmail() {
		return getString("email");
	}

	public void setUserEmail(String email) {
		put("email", email);
	}

	public String getUserName() {
		return getString("user_name");
	}

	public void setUserName(String userName) {
		put("user_name", userName);
	}

}
