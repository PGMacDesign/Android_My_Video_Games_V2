package com.pgmacdesign.myvideogamesv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.pgmacdesign.myvideogamesv2.Database.DbHelper;

import java.util.ArrayList;
import java.util.List;

/*
The Main activity, will allow users to choose between 2 options, going to the ListGamesActivity or
to the RateGamesActivity. There will be NO action bar on purpose.
 */
public class MainActivity extends Activity implements View.OnClickListener{

	//2 Image Buttons. When clicked, they will open up the respective activity
	ImageButton rank_my_games_button, list_my_games_button;

	SQLiteDatabase db;
	DbHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Initialize any and all variables / views / buttons needed
		Initialize();
		//Check if the database exists. If it does not, create it and populate it with some fictitious data for example.
		checkIfDBExists();

	}

	//Checks to see if the database has been created. If not, creates it
	private void checkIfDBExists() {

	}

	private void Initialize() {
		//Image Buttons
		rank_my_games_button = (ImageButton) findViewById(R.id.rank_my_games_button);
		list_my_games_button = (ImageButton) findViewById(R.id.list_my_games_button);

		//Set them to onClickListeners so they can be clicked for an action
		list_my_games_button.setOnClickListener(this);
		rank_my_games_button.setOnClickListener(this);
	}



	//Handles the onClick for the images. Opens the respective activities
	public void onClick(View v) {
		//Switch case to open the respective activity that the user chooses.
		switch (v.getId()) {

			//If they click the list_my_games option, open respective activity
			case R.id.list_my_games_button:
				try {
					Intent intent0 = new Intent(v.getContext(), ListGamesActivity.class);
					startActivity(intent0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			//If they click the rank_my_games option, open respective activity
			case R.id.rank_my_games_button:
				try {
					Intent intent1 = new Intent(v.getContext(), RateGamesActivity.class);
					startActivity(intent1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		}
	}

	/*
	This class pulls data from the database, adds each column to a list, and then adds those
	respective lists to a list of lists. When finished, it returns the list of lists
	 */
	public static List<List<String>> pullFromDatabase(Context context){

		DbHelper dbHelper = new DbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		//Create a list of list of Strings that will be eventually returned by this method
		List<List<String>> returned_list = new ArrayList<>();

		//Create a list of all the Row game IDs in the database and pull the data
		List<String> passed_list_rows = new ArrayList<>();
		String[] columns = {dbHelper.COLUMN_GAME_ID};
		Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);
		cursor.moveToFirst();

		//Loop through the cursor and add the row IDs to the list
		while(cursor.moveToNext()){
			if (cursor != null){
				String str = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_GAME_ID));
				passed_list_rows.add(str);
			}
		}

		Cursor cursor1 = null; //Initialize it so it can be closed after the for loop completes
		//ALL of the columns in the database
		String[] columns2 = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_ALIASES, dbHelper.COLUMN_DECK,
				dbHelper.COLUMN_ICON_URL, dbHelper.COLUMN_MEDIUM_URL, dbHelper.COLUMN_NAME,
				dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, dbHelper.COLUMN_PLATFORM_NAME,
				dbHelper.COLUMN_PLATFORM_ABBREVIATION, dbHelper.COLUMN_PLAYED_CHECKBOX, dbHelper.COLUMN_RATING};
		//Loop through the list and pull the respective data from each row into a list of its own
		for (int i = 0; i< passed_list_rows.size(); i++){
			List<String> temp_list = pullRowFromDatabase(context, passed_list_rows.get(i));
			returned_list.add(temp_list);
			Log.d("Line 121", " Ran and looped");
		}

		//To prevent memory leaks, close the respective objects
		db.close();
		cursor.close();
		if (cursor1 != null){
			cursor1.close();
		}

		return returned_list;
	}

	//Pulls a row of data from the database. Takes the game_id as the parameter to search for the row
	public static List<String> pullRowFromDatabase(Context context, String game_id){

		DbHelper dbHelper = new DbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<String> returned_list = new ArrayList<>();

		Cursor cursor1 = null; //Initialize it so it can be closed after the for loop completes

		String[] columns = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_ALIASES, dbHelper.COLUMN_DECK,
				dbHelper.COLUMN_ICON_URL, dbHelper.COLUMN_MEDIUM_URL, dbHelper.COLUMN_NAME,
				dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, dbHelper.COLUMN_PLATFORM_NAME,
				dbHelper.COLUMN_PLATFORM_ABBREVIATION, dbHelper.COLUMN_PLAYED_CHECKBOX, dbHelper.COLUMN_RATING};

		String[] whereArgs = {game_id};

		cursor1 = db.query(dbHelper.TABLE_NAME, columns, dbHelper.COLUMN_GAME_ID + " =?", whereArgs, null, null, null);

		cursor1.moveToFirst();

		//Loop through the cursor and add the row IDs to the list
		while(cursor1.moveToNext()){
			if (cursor1 != null){
				//Get the values at each of the respective columns
				String str0 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_GAME_ID));
				String str1 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ALIASES));
				String str2 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_DECK));
				String str3 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ICON_URL));
				String str4 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_MEDIUM_URL));
				String str5 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_NAME));
				String str6 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE));
				String str7 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLATFORM_NAME));
				String str8 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLATFORM_ABBREVIATION));
				String str9 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLAYED_CHECKBOX));
				String str10 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_RATING));

				//Add these values to the list
				returned_list.add(str0);
				returned_list.add(str1);
				returned_list.add(str2);
				returned_list.add(str3);
				returned_list.add(str4);
				returned_list.add(str5);
				returned_list.add(str6);
				returned_list.add(str7);
				returned_list.add(str8);
				returned_list.add(str9);
				returned_list.add(str10);

			}
		}

		//To prevent memory leaks, close the respective objects
		db.close();
		if (cursor1 != null){
			cursor1.close();
		}

		return returned_list;
	}
}
