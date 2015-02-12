package com.pgmacdesign.myvideogamesv2.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
This class serves as the liason to manage the database data.
 */
public class ContentProviderClass extends ContentProvider {

	DbHelper dbHelper;
	SQLiteDatabase db;

	private static final String AUTH = "com.pgmacdesign.myvideogames2.Database.ContentProviderClass";
	public static final Uri LOC_URI = Uri.parse("content://" + AUTH + "/" + DbHelper.TABLE_NAME);

	final static int MATCHING = 1;

	private final static UriMatcher uriMatcher;
	//Initiate uri matcher with static reference
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTH, DbHelper.TABLE_NAME, MATCHING);
	}


	public boolean onCreate() {
		dbHelper = new DbHelper(getContext());
		db = dbHelper.getWritableDatabase();

		return true;
	}


	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		dbHelper.getWritableDatabase();

		//If it matches, return 1
		if (uriMatcher.match(uri) == MATCHING){
			db.insert(dbHelper.TABLE_NAME, null, values);
		}

		//Insert data to database
		getContext().getContentResolver().notifyChange(uri, null);

		Log.d("Insert URI", "insert complete");
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		Cursor cursor;

		db = dbHelper.getReadableDatabase();

		cursor = db.query(DbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		Log.d("query ", "query complete");
		return cursor;

	}


	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
}
