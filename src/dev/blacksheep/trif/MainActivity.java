package dev.blacksheep.trif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

import dev.blacksheep.trif.adapters.ExpandableListAdapter;
import dev.blacksheep.trif.classes.Utils;
import dev.blacksheep.trif.fragment.EmergencyFragment;
import dev.blacksheep.trif.fragment.FirstWelcomeFragment;
import dev.blacksheep.trif.fragment.MRTFragment;
import dev.blacksheep.trif.fragment.MapsFragment;
import dev.blacksheep.trif.fragment.PlacesOfInterestFragment;
import dev.blacksheep.trif.fragment.WalletFragment;

public class MainActivity extends SherlockActivity {
	private DrawerLayout mDrawerLayout;
	private ExpandableListView listView;
	private ActionBarHelper mActionBar;
	private int lastExpandedPosition = -1;

	private SherlockActionBarDrawerToggle mDrawerToggle;
	ExpandableListAdapter listAdapter;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	SharedPreferences settings;
	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		new Utils(MainActivity.this).showLostNotification("I'M LOST!", "Click here if you're lost!", LostActivity.class, true, 1);
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		settings = MainActivity.this.getSharedPreferences(Consts.PREFS_NAME, Context.MODE_PRIVATE);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView = (ExpandableListView) findViewById(R.id.left_drawer);
		// lvMain = (ListView) findViewById(R.id.lvMain);
		mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		listView.setOnChildClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
					listView.collapseGroup(lastExpandedPosition);
				}
				lastExpandedPosition = groupPosition;
			}
		});
		mActionBar = createActionBarHelper();
		mActionBar.init();
		mDrawerToggle = new SherlockActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.drawable.ic_drawer_light, R.string.drawer_open, R.string.drawer_close);
		mDrawerToggle.syncState();
		prepareListData();
		if (!settings.getString("initial", "0").equals("1")) {
			// mDrawerLayout.openDrawer(listView);
			displayView(99, "");
			settings.edit().putString("initial", "1").commit();
		} else {
			displayView(2, "Nature & Wildlife");
		}

	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		listDataHeader.add("Profile");
		listDataHeader.add("Directions");
		listDataHeader.add("Places");
		listDataHeader.add("Misc");

		List<String> profileData = new ArrayList<String>();
		profileData.add("eWallet");
		profileData.add("Photo Contest");

		List<String> top250 = new ArrayList<String>();
		top250.add("Nearest Taxi Stand");
		top250.add("MRT Map");

		List<String> nowShowing = new ArrayList<String>();
		nowShowing.add("Nature & Wildlife");
		nowShowing.add("Leisure & Entertainment");
		nowShowing.add("Arts & Discovery");
		nowShowing.add("Shopping");
		nowShowing.add("Dining");
		nowShowing.add("Nightlife");

		List<String> comingSoon = new ArrayList<String>();
		comingSoon.add("Emergency Contacts");
		listDataChild.put(listDataHeader.get(0), profileData);
		listDataChild.put(listDataHeader.get(1), top250);
		listDataChild.put(listDataHeader.get(2), nowShowing);
		listDataChild.put(listDataHeader.get(3), comingSoon);
		listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);
		listView.setAdapter(listAdapter);
	}

	private void displayView(int position, String whichPlacesOfInterest) {
		// update the main content by replacing fragments
		Bundle bundl = new Bundle();
		if (!whichPlacesOfInterest.equals("")) {
			bundl.putString("type", whichPlacesOfInterest);
		}
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new MapsFragment();
			break;
		case 1:
			fragment = new MRTFragment();
			break;
		case 2:
			fragment = new PlacesOfInterestFragment();
			fragment.setArguments(bundl);
			break;
		case 3:
			fragment = new WalletFragment();
			break;
		case 4:
			fragment = new EmergencyFragment();
			break;
		case 99:
			fragment = new FirstWelcomeFragment();
			break;
		default:
			break;
		}
		try {
			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			} else {
				Log.e("MainActivity", "Error in creating fragment");
				Toast.makeText(MainActivity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class DrawerItemClickListener implements ExpandableListView.OnChildClickListener {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			mDrawerLayout.closeDrawer(listView);
			String category = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
			mActionBar.setTitle(category);
			Log.e("CATEGORY", category);
			if (category.equals("MRT Map")) {
				displayView(1, "");
			} else if (category.equals("Nearest Taxi Stand")) {
				displayView(0, "");
			} else if (category.equals("Nature & Wildlife")) {
				displayView(2, "Nature & Wildlife");
			} else if (category.equals("Leisure & Entertainment")) {
				displayView(2, "Leisure & Entertainment");
			} else if (category.equals("Shopping")) {
				displayView(2, "Shopping");
			} else if (category.equals("Dining")) {
				displayView(2, "Dining");
			} else if (category.equals("Arts & Discovery")) {
				displayView(2, "Arts & Discovery");
			} else if (category.equals("Nightlife")) {
				displayView(2, "Nightlife");
			} else if (category.equals("eWallet")) {
				displayView(3, "");
			} else if (category.equals("Photo Contest")) {
				startActivity(new Intent(MainActivity.this, PhotoContestActivity.class));
			} else if (category.equals("Emergency Contacts")) {
				displayView(4, "");
			}
			return false;
		}
	}

	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}

	private class ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		private ActionBarHelper() {
			mActionBar = MainActivity.this.getSupportActionBar();
		}

		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
			mTitle = mDrawerTitle = MainActivity.this.getSupportActionBar().getTitle();
		}

		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
		}

		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}
}
