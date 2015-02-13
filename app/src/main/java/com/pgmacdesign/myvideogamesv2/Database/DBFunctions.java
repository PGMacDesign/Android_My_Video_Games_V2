package com.pgmacdesign.myvideogamesv2.Database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
This is a grouping of database related methods. They are placed here for reusability purposes
 */
public class DBFunctions {

	//Pulls all of the gameIDs from the database.
	public static List<String> pullGameIDFromDatabase(Context context, List<String> passed_list_rows){
		DbHelper dbHelper = new DbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String[] columns = {dbHelper.COLUMN_GAME_ID};
		Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, null);

		//Loop through the cursor and add the row IDs to the list
		while(cursor.moveToNext()){
			if (cursor != null){
				String str = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_GAME_ID));
				passed_list_rows.add(str);
			}
		}
		//To prevent memory leaks, close the respective objects
		db.close();
		cursor.close();
		return passed_list_rows;
	}

	//Pulls a row of data from the database. Takes the game_id as the parameter to search for the row
	public static List<String> pullRowFromDatabase(Context context, String game_id){

		DbHelper dbHelper = new DbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<String> returned_list = new ArrayList<>();

		String[] columns = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_ALIASES, dbHelper.COLUMN_DECK,
				dbHelper.COLUMN_ICON_URL, dbHelper.COLUMN_MEDIUM_URL, dbHelper.COLUMN_NAME,
				dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, dbHelper.COLUMN_PLATFORM_NAME,
				dbHelper.COLUMN_PLATFORM_ABBREVIATION, dbHelper.COLUMN_PLAYED_CHECKBOX, dbHelper.COLUMN_RATING};

		String[] whereArgs = {game_id};

		Cursor cursor1 = db.query(dbHelper.TABLE_NAME, columns, dbHelper.COLUMN_GAME_ID + " =?", whereArgs, null, null, null);

		//Loop through the cursor and add the row IDs to the list
		while(cursor1.moveToNext()){
			if (cursor1 != null){
				//Get the values at each of the respective columns
				String str0 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_GAME_ID));
				String str1 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ALIASES));
				String str2 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_DECK));
				String str3 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ICON_URL));
				String str4 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_MEDIUM_URL));
				Log.d("Loop through games ", str4);
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

	//Sets up the db with some initial values for an example
	public static void setupDBWithInitialValues(Context context){
		//Add items to the database as it is empty (Having being run for the first time)
		try {
			SharedPreferences settings;
			final String PREFS_NAME = "MyPrefsFile";
			settings = context.getSharedPreferences(PREFS_NAME, 0);

			//Put the data into the database
			//Database helper class
			DbHelper dbHelper = new DbHelper(context);

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
			ContentResolver contentResolver = context.getContentResolver();
			contentResolver.insert(ContentProviderClass.LOC_URI, cv1); //Insert data into the db
			contentResolver.insert(ContentProviderClass.LOC_URI, cv2); //Insert data into the db
			contentResolver.insert(ContentProviderClass.LOC_URI, cv3); //Insert data into the db
			settings.edit().putBoolean("my_first_time0", false).commit();
			Log.d("First time0", "Completed");

		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
