package com.storymakers.sandbox.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storymakers.sandbox.app.TGPost.PostListDownloadCallback;

public class StoryDetail extends Activity {

	private ListView lvPosts;
	private TextView tvStorytitle, tvPostscount;
	TGStory story = null;
	ArrayAdapter<TGPost> itemsAdapter; 
	ArrayList<TGPost> posts;
		    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_detail);
		lvPosts = (ListView) findViewById(R.id.lvPosts);
		tvStorytitle = (TextView)findViewById(R.id.tvStoryTitle);
		tvPostscount = (TextView)findViewById(R.id.tvStoryPostcount);
		posts = new ArrayList<TGPost>();
		itemsAdapter = new ArrayAdapter<TGPost>(this, android.R.layout.simple_list_item_1, posts);
		lvPosts.setAdapter(itemsAdapter);
		getRandomStory();
		
	}
	private void getRandomStory() {
		ParseQuery<TGStory> qry = ParseQuery.getQuery(TGStory.class);
		qry.getFirstInBackground(new GetCallback<TGStory>() {
			
			@Override
			public void done(TGStory object, ParseException e) {
				// TODO Auto-generated method stub
				Toast.makeText(StoryDetail.this, object.getTitle(), Toast.LENGTH_SHORT).show();
			}
		});
		final ArrayList<TGStory> stories = new ArrayList<TGStory>();
		RemoteDBClient.getStories(new FindCallback<TGStory>() {
			
			@Override
			public void done(List<TGStory> objects, ParseException e) {
				if (e != null){
					e.printStackTrace();
					return;
				}
				stories.addAll(objects);
				if (stories.size() > 0){
					Random generator = new Random();
					int i = generator.nextInt(stories.size());
					story = stories.get(i);
					showStory();
				}
				
			}
		}, 0, 0);
		
	}
	private void showStory() {
		if (story == null) {
			Toast.makeText(this, "Failed to get any story", Toast.LENGTH_SHORT).show();
			return;
		}
		tvStorytitle.setText(story.getTitle());
		tvPostscount.setText(story.getCoverPhotoURL());
		RemoteDBClient.getPostsForStory(story, new PostListDownloadCallback() {
			
			
			public void fail(String reason) {
				Log.e("ERROR", reason);
				
			}
			
			
			public void done(List<TGPost> objs) {
				itemsAdapter.addAll(objs);
				
			}
		});
		
	}
}
