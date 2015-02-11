package com.pgmacdesign.myvideogamesv2;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.pgmacdesign.myvideogamesv2.Database.ContentProviderClass;
import com.pgmacdesign.myvideogamesv2.Database.DbHelper;


public class MainActivity11 extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	SQLiteDatabase db;
	DbHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity11);

		Log.d("Line", "41");
		dbHelper = new DbHelper(this);
		Log.d("Line", "43");
		db = dbHelper.getWritableDatabase();
		Log.d("Line", "45");
		ContentValues cv = new ContentValues();
		Log.d("Line", "47");
		cv.put(dbHelper.COLUMN_GAME_ID, "13053");
		cv.put(dbHelper.COLUMN_ALIASES, "FF7");
		cv.put(dbHelper.COLUMN_DECK, "Awesome game");
		cv.put(dbHelper.COLUMN_ICON_URL, "https://i.chzbgr.com/maxW500/8442595072/h9B38CACC/");
		cv.put(dbHelper.COLUMN_MEDIUM_URL, "https://i.chzbgr.com/maxW500/8442595072/h9B38CACC/");
		cv.put(dbHelper.COLUMN_NAME, "Final Fantasy VII");
		cv.put(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, "1997-10-10");
		cv.put(dbHelper.COLUMN_PLATFORM_NAME, "Playstation");
		cv.put(dbHelper.COLUMN_PLATFORM_ABBREVIATION, "PS1");
		cv.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "true");
		cv.put(dbHelper.COLUMN_RATING, "4");
		Log.d("Line", "59");
		//db.insert(dbHelper2.TABLE_NAME, null, cv);
		ContentResolver contentResolver = this.getContentResolver();
		contentResolver.insert(ContentProviderClass.LOC_URI, cv); //Insert data into the db
		Log.d("Line", "63");

		//db.close();

		//SQLiteDatabase db1 = DBHelper2

		String[] columns = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_NAME};
		Log.d("Line", "67");
		//Read data
		String gameID = "13053";
		String[] whereArgs = {gameID};
		Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, dbHelper.COLUMN_GAME_ID + " =?", whereArgs, null, null, null);
		//Cursor cursor = db.query(dbHelper2.TABLE_NAME, columns, null, null, null, null, null);
		Log.d("Line", "72");
		cursor.moveToFirst();
		Log.d("Line", "74");

		while(cursor.moveToNext()){
			if (cursor != null){
				String str = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_GAME_ID));
				//String str1 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_ALIASES));
				//String str2 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_DECK));
				//String str3 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_ICON_URL));
				//String str4 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_MEDIUM_URL));
				String str5 = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_NAME));
				//String str6 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_ORIGINAL_RELEASE_DATE));
				//String str7 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_PLATFORM_NAME));
				//String str8 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_PLATFORM_ABBREVIATION));
				//String str9 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_PLAYED_CHECKBOX));
				//String str10 = cursor.getString(cursor.getColumnIndex(dbHelper2.COLUMN_RATING));
				Log.d("Line", "87");
				Log.d("Cur reading = ", str5);
			}

		}
		Log.d("Line", "90");
		cursor.close();
	}

	//Loader being setup in order to pull data, pass in the columns and URI
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] columns = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_ALIASES,
				dbHelper.COLUMN_DECK, dbHelper.COLUMN_ICON_URL, dbHelper.COLUMN_MEDIUM_URL,
				dbHelper.COLUMN_NAME, dbHelper.COLUMN_ORIGINAL_RELEASE_DATE,
				dbHelper.COLUMN_PLATFORM_NAME, dbHelper.COLUMN_PLATFORM_ABBREVIATION,
				dbHelper.COLUMN_PLAYED_CHECKBOX, dbHelper.COLUMN_RATING };

		CursorLoader cursorLoader = new CursorLoader(this, ContentProviderClass.LOC_URI,
				columns, null, null, null);

		return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		//adapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		//adapter.swapCursor(null);
	}


}
