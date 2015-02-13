package com.pgmacdesign.myvideogamesv2;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pgmacdesign.myvideogamesv2.Database.DbHelper;
import com.squareup.picasso.Picasso;

/**
 This class shows the user more information about their games
 */
public class DetailsActivity extends ActionBarActivity {
	String game_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);

		ActionBarActivity actionBar = new ActionBarActivity();
		actionBar.getSupportActionBar();

		Intent intent = getIntent();
		intent.getExtras();
		game_id = intent.getStringExtra("game_id");

		//All of the UI components
		TextView details_activity_game_name, details_activity_text_view_release_date,
				details_activity_description, details_activity_platform;
		ImageView imageView;
		RatingBar details_activity_ratingBar;
		CheckBox details_activity_checkbox;

		//Define the UI components
		details_activity_game_name = (TextView) findViewById(R.id.details_activity_game_name);
		details_activity_text_view_release_date = (TextView) findViewById(R.id.details_activity_text_view_release_date);
		details_activity_description = (TextView) findViewById(R.id.details_activity_description);
		details_activity_platform = (TextView) findViewById(R.id.details_activity_platform);

		//Photo, set via the photo url in the database using Picasso
		imageView = (ImageView) findViewById(R.id.imageView);

		//Checkbox. setting it to non-clickable
		details_activity_checkbox = (CheckBox) findViewById(R.id.details_activity_checkbox);
		details_activity_checkbox.setClickable(false);

		details_activity_ratingBar = (RatingBar) findViewById(R.id.details_activity_ratingBar);

		//Retrieve the data from the database
		DbHelper dbHelper = new DbHelper(this);
		ContentResolver contentResolver = this.getContentResolver();
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String[] columns = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_ALIASES, dbHelper.COLUMN_DECK,
				dbHelper.COLUMN_ICON_URL, dbHelper.COLUMN_MEDIUM_URL, dbHelper.COLUMN_NAME,
				dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, dbHelper.COLUMN_PLATFORM_NAME,
				dbHelper.COLUMN_PLATFORM_ABBREVIATION, dbHelper.COLUMN_PLAYED_CHECKBOX, dbHelper.COLUMN_RATING};

		String[] whereArgs = {game_id};

		Cursor cursor1 = db.query(dbHelper.TABLE_NAME, columns, dbHelper.COLUMN_GAME_ID + " =?", whereArgs, null, null, null);

		//String values to be used in this class:
		String game_name, platform_name, platform_abb, medium_url, played_game, rating1, release_date, deck;
		//Initialize them
		game_name = platform_name = platform_abb = medium_url = played_game = rating1 = release_date = deck = "";

		//Loop through the cursor and add the row IDs to the list
		while(cursor1.moveToNext()){
			if (cursor1 != null){
				//Get the values at each of the respective columns
				medium_url = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_MEDIUM_URL));
				game_name = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_NAME));
				platform_name = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLATFORM_NAME));
				played_game = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLAYED_CHECKBOX));
				rating1 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_RATING));
				platform_abb = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLATFORM_ABBREVIATION));
				release_date = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE));
				deck = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_DECK));
			}
		}

		//Set the UI Components with values from the list/ database

		details_activity_game_name.setText(game_name);
		details_activity_text_view_release_date.setText("Release Date: " + release_date);
		details_activity_description.setText(deck);
		details_activity_platform.setText("Platform: " + platform_name + " AKA: " + platform_abb);

		//Load the photo (higher quality link) into the image frame
		Picasso.with(this).load(medium_url).into(imageView);

		//Checkbox, see if database has it set to played or not
		if (played_game.equalsIgnoreCase("true")){
			details_activity_checkbox.setChecked(true);
		} else {
			details_activity_checkbox.setChecked(false);
		}

		//Just in case there are more significant figures, trim down to the first character (IE 1.0 vs 1)
		rating1 = rating1.substring(0, Math.min(rating1.length(), 1));
		//If the value is not a mistake, set the rating in the view window
		if (rating1.equalsIgnoreCase("-1.0")){
			Log.d("Rating negative on Game: ", game_id);
		} else {
			details_activity_ratingBar.setRating(Integer.parseInt(rating1));
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.home) {
			// app icon in action bar clicked; go home
			/*
			Intent intent = new Intent(this, ListGamesActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			*/
			getFragmentManager().popBackStack();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//getFragmentManager().popBackStack();

		Intent intent = new Intent(this, ListGamesActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
