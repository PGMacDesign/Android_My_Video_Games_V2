package com.pgmacdesign.myvideogamesv2;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.pgmacdesign.myvideogamesv2.Database.ContentProviderClass;
import com.pgmacdesign.myvideogamesv2.Database.DbHelper;


public class MainActivity11 extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	SQLiteDatabase db;
	DbHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity11);


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
