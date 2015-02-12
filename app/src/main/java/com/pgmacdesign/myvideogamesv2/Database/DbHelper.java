package com.pgmacdesign.myvideogamesv2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 The database class. It will hold all of the data retrieved from the website that the user chose from
 the list.
 */
public class DbHelper extends SQLiteOpenHelper {

	//Standard variables for the database functions
	public static final String DATABASE_NAME = "videogamesdb2";
	public static final String TABLE_NAME = "videogamestable";
	public static final int DATABASE_VERSION = 4;
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

	public static final String C_ID = "_id";
	// Column with the game_id, IE 13053
	public static final String COLUMN_GAME_ID = "game_id";
	// Stored as an String, aliases for the game, IE FF7, FFVII
	public static final String COLUMN_ALIASES = "aliases";
	// Stored as a string, a short description of the game
	public static final String COLUMN_DECK = "deck";
	// Stored as an string, one of the image URLs
	public static final String COLUMN_ICON_URL = "icon_url";
	// Stored as an string, one of the image URLs
	public static final String COLUMN_MEDIUM_URL = "medium_url";
	// Stored as an string, the name of the game (IE Final Fantasy VII)
	public static final String COLUMN_NAME = "name";
	// Stored as an string, the original release date (IE 1997-01-31 00:00:00)
	public static final String COLUMN_ORIGINAL_RELEASE_DATE = "original_release_date";
	// Stored as a String, the platform name (IE Playstation)
	public static final String COLUMN_PLATFORM_NAME = "platform_name";
	// Stored as a String, the platform name abbreviation (IE PS1)
	public static final String COLUMN_PLATFORM_ABBREVIATION = "platform_abbreviation";
	// Stored as a boolean, checkbox for whether or not they have played it
	public static final String COLUMN_PLAYED_CHECKBOX = "played_checkbox";
	// Stored as a String, rating system using the android 'star' picker
	public static final String COLUMN_RATING = "rating";

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
			+ C_ID + " integer primary key autoincrement, "
			+ COLUMN_GAME_ID + " text,"
			+ COLUMN_ALIASES + " text,"
			+ COLUMN_DECK + " text,"
			+ COLUMN_ICON_URL + " text,"
			+ COLUMN_MEDIUM_URL + " text,"
			+ COLUMN_NAME + " text,"
			+ COLUMN_ORIGINAL_RELEASE_DATE + " text,"
			+ COLUMN_PLATFORM_NAME + " text,"
			+ COLUMN_PLATFORM_ABBREVIATION + " text,"
			+ COLUMN_PLAYED_CHECKBOX + " text,"
			+ COLUMN_RATING + " text"
			+");";

	public DbHelper(Context context){
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
}
