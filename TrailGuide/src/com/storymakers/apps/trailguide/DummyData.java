package com.storymakers.apps.trailguide;

import java.util.ArrayList;

import com.storymakers.apps.trailguide.fragments.HikesListFragment.TGStory;
import com.storymakers.apps.trailguide.fragments.HikesListFragment.TGPost;

public class DummyData {
	public static ArrayList<TGStory> getHikes() {
		ArrayList<TGStory> stories = new ArrayList<TGStory>();
		stories.add(getLaguna());
		//stories.add(getUmanum());
		//stories.add(getAlmaden());
		return stories;
	}

	public static TGStory getLaguna() {
		TGStory story = new TGStory();
		story.posts = new ArrayList<TGPost>();
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0423209__-122.8579315-IQ7EFmQl.1413175473.PNG", 0, 38.0423209, -122.8579315));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0379302_-122.8564391-1uTm56fs.1413175462.PNG", 0, 38.0379302,-122.8564391));
		story.posts.add(new TGPost("This place is awesome", 1, 38.0362427,-122.8558596));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0351291_-122.856799-01LK5GYt.1413175442.PNG", 0, 38.0351291,-122.856799));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0351291_-122.856799__2_-sWT8EEzK.1413175403.PNG", 0, 38.0351291,-122.856799));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0283639_-122.8588262-AuCzfjCr.1413175389.PNG", 0, 38.0283639,-122.8588262));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0219532_-122.8579237-CHdJAIsQ.1413175371.PNG", 0, 38.0219532,-122.8579237));
		story.posts.add(new TGPost("Nearing the beach...", 1, 38.0191769,-122.8565391));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0180459_-122.8582825-snlC7NZt.1413175303.PNG", 0, 38.0180459,-122.8582825));
		story.posts.add(new TGPost("Beautiful beach...", 1, 38.0180459,-122.8582825));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.015647_-122.8583851__2_-uhy5Bkap.1413175252.PNG", 0, 38.015647,-122.8583851));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.015647_-122.8583851-KvO6JRNh.1413175227.PNG", 0, 38.015647,-122.8583851));
		story.posts.add(new TGPost("http://picpaste.com/pics/38.0185489_-122.8625654-KLYJwaB8.1413175318.PNG", 0, 38.0185489,-122.8625654));
		return story;
	}

	/*public static TGStory getUmanum() {
		return new TGStory();
	}

	public static TGStory getAlmaden() {
		return new TGStory();
	}*/
}
