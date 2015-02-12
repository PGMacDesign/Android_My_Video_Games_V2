package com.pgmacdesign.myvideogamesv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pgmacdesign.myvideogamesv2.Database.ContentProviderClass;
import com.pgmacdesign.myvideogamesv2.Database.DbHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by PatrickSSD2 on 2/10/2015.
 */
public class ListGamesActivity extends Activity {

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

		/*
		Using Shared Preferences to determine if the app has been opened before or if this is
		the first time. If not, do a dialog popup explaining how to delete records. If it is not,
		simply ignore it and continue to inflate the screen.
		 */
		final String PREFS_NAME = "MyPrefsFile";
		settings = this.getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean("my_first_time", true)) {
			//The app is being launched for first time, do something
			initializeTheApp();
			Log.d("Comments", "First time");
		}

		//Fragment manager
		manager = getFragmentManager();

		//Manages the backstack changes, useful for if they accidentally click back
		getFragmentManager().addOnBackStackChangedListener(getListener());

		fragment = (ListFragment) manager.findFragmentById(R.id.the_list_fragment);

		DbHelper dbHelper = new DbHelper(this);
		ContentResolver contentResolver = this.getContentResolver();
		List<List<String>> passed_data = MainActivity.pullFromDatabase(this);
		fragment.setupTheList(passed_data, "List", dbHelper, contentResolver);

		/*
		ListFragment list_fragment = new ListFragment();
		FragmentTransaction transaction = manager.beginTransaction();
		//Adding the bundle in to determine which items will be listed at the end of the listview
		Bundle bundle = new Bundle();
		bundle.putString("last_field_option", "List");
		list_fragment.setArguments(bundle);
		transaction.add(R.id.empty_view, list_fragment, "list");
		transaction.addToBackStack("backstack");
		transaction.commit();
		*/

		/*


		FragmentTransaction transaction = manager.beginTransaction();
		//Adding the bundle in to determine which items will be listed at the end of the listview
		Bundle bundle = new Bundle();
		bundle.putString("last_field_option", "List");
		fragment.setArguments(bundle);
		transaction.add(R.id.empty_view, fragment, "list");
		transaction.addToBackStack("backstack");
		transaction.commit();

        */


	}

	private void initializeTheApp() {
		//First time task
		//Dialog popup, telling them how to delete a record
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					//If they hit close, it will dismiss this dialog box
					case DialogInterface.BUTTON_NEGATIVE:
						try {
							dialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Did you know?");
		builder.
				setMessage("To Delete a game from your library, simply long-press on the game platform / console").
				setNegativeButton("Ok", dialogClickListener).
				show();

		//Add items to the database as it is empty (Having being run for the first time)
		try {
			int x = new createDefaultValuesInDatabase().execute().get(); //Wait until it has completed before moving on
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// record the fact that the app has been started at least once
		settings.edit().putBoolean("my_first_time", false).commit();
	}

	//This async class manages adding items to the database if it is empty
	private class createDefaultValuesInDatabase extends AsyncTask<Void, Void, Integer> {
		protected Integer doInBackground(Void... params) {
			//Put the data into the database
			//Database helper class
			DbHelper dbHelper = new DbHelper(ListGamesActivity.this);
			ContentValues cv1 = new ContentValues();
			cv1.put(dbHelper.COLUMN_GAME_ID, "10276");
			cv1.put(dbHelper.COLUMN_ALIASES, "Zelda 3");
			cv1.put(dbHelper.COLUMN_DECK, "The third installment in the Zelda series makes a return to the top-down 2D " +
					"gameplay found in the first game. This time around, Link needs to travel between the light and dark " +
					"world in order to set things straight in the kingdom of Hyrule.");
			cv1.put(dbHelper.COLUMN_ICON_URL, "http://static.giantbomb.com/uploads/square_avatar/9/93770/2363926-snes_thelegendofzeldaalinktothepast.jpg");
			cv1.put(dbHelper.COLUMN_MEDIUM_URL, "http://static.giantbomb.com/uploads/scale_medium/9/93770/2363926-snes_thelegendofzeldaalinktothepast.jpg");
			cv1.put(dbHelper.COLUMN_NAME, "The Legend of Zelda: A Link to the Past");
			cv1.put(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, "1991-11-21");
			cv1.put(dbHelper.COLUMN_PLATFORM_NAME, "Super Nintendo Entertainment System");
			cv1.put(dbHelper.COLUMN_PLATFORM_ABBREVIATION, "SNES");
			cv1.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "false");
			cv1.put(dbHelper.COLUMN_RATING, "3");

			ContentValues cv2 = new ContentValues();
			cv2.put(dbHelper.COLUMN_GAME_ID, "10299");
			cv2.put(dbHelper.COLUMN_ALIASES, "SMB3");
			cv2.put(dbHelper.COLUMN_DECK, "Super Mario Bros. 3 sends Mario on a whole new adventure across diverse worlds and sporting strange new suits and abilities.");
			cv2.put(dbHelper.COLUMN_ICON_URL, "http://static.giantbomb.com/uploads/square_avatar/9/93770/2362272-nes_supermariobros3_4.jpg");
			cv2.put(dbHelper.COLUMN_MEDIUM_URL, "http://static.giantbomb.com/uploads/scale_medium/9/93770/2362272-nes_supermariobros3_4.jpg");
			cv2.put(dbHelper.COLUMN_NAME, "Super Mario Bros. 3");
			cv2.put(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, "1988-10-23");
			cv2.put(dbHelper.COLUMN_PLATFORM_NAME, "Nintendo Entertainment System");
			cv2.put(dbHelper.COLUMN_PLATFORM_ABBREVIATION, "NES");
			cv2.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "true");
			cv2.put(dbHelper.COLUMN_RATING, "1");

			ContentValues cv3 = new ContentValues();
			cv3.put(dbHelper.COLUMN_GAME_ID, "13053");
			cv3.put(dbHelper.COLUMN_ALIASES, "FFVII, FF7");
			cv3.put(dbHelper.COLUMN_DECK, "A young man's quest to defeat a corrupt corporation he once served and exact revenge upon the man who wronged him, " +
					"uncovering dark secrets about his past along the way, in the most celebrated console RPG of all time. " +
					"It popularized the RPG genre and was a killer app for the PlayStation console.");
			cv3.put(dbHelper.COLUMN_ICON_URL, "http://static.giantbomb.com/uploads/square_avatar/8/87790/1814630-box_ff7.png");
			cv3.put(dbHelper.COLUMN_MEDIUM_URL, "http://static.giantbomb.com/uploads/scale_medium/8/87790/1814630-box_ff7.png");
			cv3.put(dbHelper.COLUMN_NAME, "Final Fantasy VII");
			cv3.put(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, "1997-01-31");
			cv3.put(dbHelper.COLUMN_PLATFORM_NAME, "Playstation");
			cv3.put(dbHelper.COLUMN_PLATFORM_ABBREVIATION, "PS1");
			cv3.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "true");
			cv3.put(dbHelper.COLUMN_RATING, "3");

			//Utilize the content resolver to add the values into the database
			ContentResolver contentResolver = ListGamesActivity.this.getContentResolver();
			contentResolver.insert(ContentProviderClass.LOC_URI, cv1); //Insert data into the db
			contentResolver.insert(ContentProviderClass.LOC_URI, cv2); //Insert data into the db
			contentResolver.insert(ContentProviderClass.LOC_URI, cv3); //Insert data into the db
			return 0;
		}
	}

	//This class is called whenever the backstack is changed. primarily used to handle the back button being hit
	private FragmentManager.OnBackStackChangedListener getListener(){
		FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
			public void onBackStackChanged() {
				ListFragment list_fragment = new ListFragment();
				//ddd - Need to set it here so that it calls a method to update something in the fragment
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.the_list_fragment, list_fragment, "list");
				transaction.commit();
			}
		};
		return result;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_plus_button, menu);
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
			transaction.add(R.id.the_list_fragment, addNewGameFragment);
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


