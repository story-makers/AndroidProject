package com.storymakers.apps.trailguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.widget.SearchView;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.fragments.HikesListFragment;
import com.storymakers.apps.trailguide.fragments.SearchFragmentDialog;
import com.storymakers.apps.trailguide.fragments.SearchHikesFragment;
import com.storymakers.apps.trailguide.model.TGFilter;
import com.storymakers.apps.trailguide.model.TGStory;

public class HomeActivity extends FragmentActivity implements
		HikesListFragment.OnListItemClickListener,
		SearchFragmentDialog.OnDataPass {
	private String searchQuery;
	private SearchView searchView;
	TGFilter searchFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		searchFilter = new TGFilter("", "", "");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miNewHike:
			createNewHike();
			return true;
		case R.id.miProfile:
			myProfile();
			return true;
		case R.id.miSearch:
			showSearchDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		/*
		 * ActionBar actionBar = getActionBar();
		 * actionBar.setDisplayShowHomeEnabled(false); // displaying custom
		 * ActionBar View mActionBarView = getLayoutInflater().inflate(
		 * R.layout.action_bar_custom, null);
		 * actionBar.setCustomView(mActionBarView);
		 * actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		 */
		// MenuItem searchItem = menu.findItem(R.id.miSearch);
		// searchView = (SearchView) searchItem.getActionView();
		// mySearch(searchItem);
		return true;
	}

	private void showSearchDialog() {
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		SearchFragmentDialog searchFragmentDialog = SearchFragmentDialog
				.newInstance(searchFilter);
		searchFragmentDialog.show(fm, "fragment_search");

	}

	public void onListItemClicked(View v) {
		SearchHikesFragment fragment = (SearchHikesFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentSearchHikes);
		fragment.onListItemClicked(v);
	}

	@Override
	public void onListItemClicked(TGStory story) {
		Intent i = HikeDetailsActivity.getIntentForStory(this, story);
		startActivity(i);
	}

	private void createNewHike() {
		Intent i = HikeCreateActivity.getIntentForCreateStory(this);
		startActivity(i);
	}

	private void myProfile() {
		Intent i = ProfileActivity.getIntentForUserProfile(this);
		startActivity(i);
	}

	private void mySearch(MenuItem searchItem) {
		searchView = (SearchView) searchItem.getActionView();
		searchQuery = "";
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				searchQuery = query;
				SearchHikesFragment fragment = (SearchHikesFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragmentSearchHikes);
				fragment.onSearchSubmit();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Do something when collapsed
				return true; // Return true to collapse action view
			}

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Do something when expanded
				// item.expandActionView();
				searchView.onActionViewExpanded();
				if (searchQuery.length() != 0) {
					searchView.setQuery(searchQuery, false);
				}
				// searchView.setQueryHint(searchQuery);
				return true; // Return true to expand action view
			}
		});

	}

	@Override
	public void onDataPass(TGFilter filter) {
		// Log.d("LOG","hello " + data);
		searchFilter = filter;
		SearchHikesFragment fragment = (SearchHikesFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentSearchHikes);
		fragment.setSearchFilter(searchFilter);
		fragment.onSearchSubmit();

	}
}
