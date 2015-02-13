package com.pgmacdesign.myvideogamesv2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by PatrickSSD2 on 2/10/2015.
 */
public class RateGamesActivity extends ActionBarActivity {

	//Fragment manager manages the respective fragments being opened, hidden, closed, etc
	FragmentManager manager;

	SharedPreferences settings;

	ListFragment fragment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_fragment_holder);

		//Add the action bar and support the back option
		ActionBarActivity actionBar = new ActionBarActivity();
		actionBar.getSupportActionBar();

		//Fragment manager
		manager = getFragmentManager();

		//Manages the backstack changes, useful for if they accidentally click back
		getFragmentManager().addOnBackStackChangedListener(getListener());


		//Create a fragment and transaction and then commit them
		fragment = new ListFragment();
		FragmentTransaction transaction = manager.beginTransaction();
		//Adding the bundle in to determine which items will be listed at the end of the listview
		Bundle bundle = new Bundle();
		bundle.putString("last_field_option", "Rate");
		fragment.setArguments(bundle);
		transaction.add(R.id.empty_view, fragment);
		//transaction.addToBackStack("backstack");
		transaction.commit();


	}

	//This class is called whenever the backstack is changed. primarily used to handle the back button being hit
	private FragmentManager.OnBackStackChangedListener getListener(){
		FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
			public void onBackStackChanged() {
				ListFragment list_fragment = new ListFragment();
				//Adding the bundle in to determine which items will be listed at the end of the listview
				Bundle bundle = new Bundle();
				bundle.putString("last_field_option", "Rate");
				list_fragment.setArguments(bundle);
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.empty_view, list_fragment, "Rate");
				transaction.commit();
			}
		};
		return result;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	//Manages the Action bar menu. If a user clicks either back or add new game (icon) it gets called here)
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//Add new game fragment to the view
		if (id == R.id.add_new_game) {
			/*
			This creates a new transaction, opens up a new fragment, covering up the one on screen
			and opens up one where the user can add a new game to the list
			 */
			AddNewGameFragment addNewGameFragment = new AddNewGameFragment();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.add(R.id.empty_view, addNewGameFragment);
			transaction.addToBackStack("backstack");
			transaction.commit();
			return true;
		}

		if (id == R.id.home) {
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);


			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}


