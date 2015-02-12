package com.pgmacdesign.myvideogamesv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pgmacdesign.myvideogamesv2.Database.ContentProviderClass;
import com.pgmacdesign.myvideogamesv2.Database.DBFunctions;
import com.pgmacdesign.myvideogamesv2.Database.DbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 This class will serve as the fragment that works with the listview to have data
 */
public class ListFragment extends Fragment{

	//CustomAdapter.Holder holder2; //May need to add this back in
	CustomAdapter.Holder holder;

	//String defined below in the bundle passed in
	public String last_field_option;

	//The listview
	ListView listView;

	//onCreate
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//This retrieves the data passed by the bundle and sets the string last_field_option
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			last_field_option = bundle.getString("last_field_option", "List");
		}
	}

	//When the view is created
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate the add new game layout view
		View view = inflater.inflate(R.layout.list_view_holder, container, false);

		//Get the context to pass in
		Context context = getActivity();

		DbHelper dbHelper = new DbHelper(context);
		ContentResolver contentResolver = context.getContentResolver();

		//List of the row numbers (in string format)
		List<String> passed_list_rows = new ArrayList<>();

		//Get the row numbers by querying the database
		passed_list_rows = DBFunctions.pullGameIDFromDatabase(context, passed_list_rows);

		for (int i = 0; i<passed_list_rows.size(); i++){
			Log.d("List row "+i, " "+passed_list_rows.get(i));
		}

		//Setup a listview and reference it to the listview id
		listView = (ListView) view.findViewById(R.id.data_list_view);

		//Set the background color
		listView.setBackgroundColor(Color.GRAY);

		//Set the custom adapter to the listview
		listView.setAdapter(new CustomAdapter(context, passed_list_rows, last_field_option, dbHelper, contentResolver));

		return view;
	}

	//In case we want to utilize a saved instance state (IE rotate screen)
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	//This custom adapter is used to fill the respective data into the listview
	class CustomAdapter extends BaseAdapter{

		List<String> passed_game_ids = new ArrayList<>();
		Context context;
		private LayoutInflater inflater = null;
		private String last_field_option;
		private DbHelper dbHelper;
		private ContentResolver contentResolver;
		private SQLiteDatabase db;
		private String game_id;
		private String rating1;

		public CustomAdapter(Context context, List<String> passed_list, String str, DbHelper dbHelper, ContentResolver contentResolver) {
			this.passed_game_ids = passed_list;
			this.context=context;
			inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			last_field_option = str;
			this.dbHelper = dbHelper;
			this.contentResolver = contentResolver;
			db = dbHelper.getWritableDatabase();

		}

		@Override
		public int getCount() {
			return passed_game_ids.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		//Holds the respective fields
		public class Holder {
			//These text views and image views will remain the same for both activities
			TextView tv_game_name, tv_console_name, tv_bottom;
			ImageView imageView;

			//These will change depending on which fragment is opened
			LinearLayout bottom_left, bottom_right;

			RatingBar ratingBar;
			CheckBox checkBox;
		}

		//Custom view
		public View getView(final int position, View convertView, ViewGroup parent) {

			//A holder object to reference the textviews and imageviews
			holder = new Holder();
			View rowView;
			rowView = inflater.inflate(R.layout.custom_list_view, null);

			//Display metrics are used to calculate the dp of the user's screen, which helps give a cleaner look when setting width
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics ();
			display.getMetrics(outMetrics);
			float density  = getResources().getDisplayMetrics().density;
			float dpWidth  = outMetrics.widthPixels / density;

			holder.imageView = (ImageView) rowView.findViewById(R.id.custom_list_view_image);
			holder.imageView.setMaxWidth((int)(dpWidth)*(30/100));
			holder.imageView.setPadding(15, 15, 15, 15);

			holder.tv_game_name = (TextView) rowView.findViewById(R.id.custom_list_view_game_name);
			holder.tv_game_name.setMaxWidth((int)(dpWidth)*(30/100));
			holder.tv_game_name.setTextColor(Color.RED);
			holder.tv_game_name.setPadding(5,0,5,0);

			holder.tv_console_name = (TextView) rowView.findViewById(R.id.custom_list_view_console);
			holder.tv_console_name.setMaxWidth((int) (dpWidth)*(35/100));
			holder.tv_console_name.setTextColor(Color.BLUE);
			holder.tv_console_name.setPadding(5,0,0,0);

			holder.bottom_left = (LinearLayout) rowView.findViewById(R.id.left_layout);

			holder.bottom_right = (LinearLayout) rowView.findViewById(R.id.right_layout);

			holder.tv_bottom = new TextView(context);
			holder.ratingBar = new RatingBar(context); //On a side note, why is the rating bar ridiculously huge? I mean, enormous and in the way huge...
			holder.ratingBar.setStepSize(1);
			holder.ratingBar.setNumStars(4);
			holder.checkBox = new CheckBox(context);

			holder.tv_bottom.setPadding(5, 5, 5, 5); //Add some padding so they are not right up against each other

			//Check which fragment is being called
			if (last_field_option.equalsIgnoreCase("List")){ //Check in small (left), tv in large (right)
				holder.bottom_left.addView(holder.checkBox);
				holder.bottom_right.addView(holder.tv_bottom);

				holder.tv_bottom.setGravity(Gravity.CENTER); //Set to center so it aligns better
				holder.checkBox.setGravity(Gravity.CENTER); //Set to center so it aligns better
				holder.bottom_left.setGravity(Gravity.CENTER); //Set to center so it aligns better
				holder.checkBox.setPadding(5, 5, 5, 5);


				holder.tv_bottom.setText("Have you finished playing the game?");

			} else if (last_field_option.equalsIgnoreCase("Rate")){ //Rating in large (right), tv in small (left)
				holder.bottom_left.addView(holder.tv_bottom);
				holder.bottom_right.addView(holder.ratingBar);

				holder.tv_bottom.setGravity(Gravity.LEFT); //Set text to center so it aligns better
				holder.ratingBar.setPadding(5, 5, 5, 5);

				holder.tv_bottom.setText("Rate your game");
			}

			//Loop through the list to see what is in it and set the different fields
			for (int i = 0; i < passed_game_ids.size(); i++){

				String[] columns = {dbHelper.COLUMN_GAME_ID, dbHelper.COLUMN_ALIASES, dbHelper.COLUMN_DECK,
						dbHelper.COLUMN_ICON_URL, dbHelper.COLUMN_MEDIUM_URL, dbHelper.COLUMN_NAME,
						dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, dbHelper.COLUMN_PLATFORM_NAME,
						dbHelper.COLUMN_PLATFORM_ABBREVIATION, dbHelper.COLUMN_PLAYED_CHECKBOX, dbHelper.COLUMN_RATING};

				String[] whereArgs = {passed_game_ids.get(i)};

				Cursor cursor1 = db.query(dbHelper.TABLE_NAME, columns, dbHelper.COLUMN_GAME_ID + " =?", whereArgs, null, null, null);

				cursor1.moveToFirst();

				//String values to be used in this class:
				game_id = null;
				String game_name = null;
				String platform_name = null;
				String icon_url = "";
				String played_game = null;
				rating1 = null;

				//Loop through the cursor and add the row IDs to the list
				while(cursor1.moveToNext()){
					if (cursor1 != null){
						//Get the values at each of the respective columns
						game_id = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_GAME_ID));
						icon_url = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_ICON_URL));
						game_name = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_NAME));
						platform_name = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLATFORM_NAME));
						played_game = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_PLAYED_CHECKBOX));
						rating1 = cursor1.getString(cursor1.getColumnIndex(dbHelper.COLUMN_RATING));

					}
				}

				final String passed_game_id = game_id;

				holder.tv_game_name.setText(game_name); //Game Name
				holder.tv_console_name.setText(platform_name); //Platform Name

				//Setting the image using the third party library picasso
				Log.d("Photo URL is: ", icon_url);

				//Set the image using a bitmap returned from the URL
				Picasso.with(context).load(icon_url).into(holder.imageView);

				//Check which fragment we are in
				if (last_field_option.equalsIgnoreCase("List")){
					holder.checkBox.setChecked(false);

					Toast.makeText(getActivity(), "To delete a game, long-press on the console/ platform name", Toast.LENGTH_SHORT);
					//Set the checkbox
					if (played_game.equalsIgnoreCase("true")){
						holder.checkBox.setChecked(true);
					} else {
						holder.checkBox.setChecked(false);
					}

					/*
					Add an on item click listener to the picture and the title of the game so
					if they click on it it will open a new activity with more details
					 */
					holder.imageView.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Log.d("Game ID is :", passed_game_id);
							Intent intent = new Intent(getActivity(), DetailsActivity.class);
							intent.putExtra("game_id", passed_game_id);
							startActivity(intent);
						}
					});

					//Same for title/ name as for image view, allows for a larger click area
					holder.tv_game_name.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Log.d("Game ID is :", passed_game_id);
							Intent intent = new Intent(getActivity(), DetailsActivity.class);
							intent.putExtra("game_id", passed_game_id);
							startActivity(intent);
						}
					});

					//Manages the checkbox and whether or not it is checked. Updates the database when clicked
					holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							String game_id = passed_game_id;

							if (isChecked){

								try {

									//Update the database
									ContentValues cv1 = new ContentValues();
									cv1.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "true");

									String[] whereArgs = {game_id};

									//Utilize the content resolver to add the values into the database
									contentResolver.update(ContentProviderClass.LOC_URI, cv1, dbHelper.COLUMN_GAME_ID + " =?", whereArgs); //Update data into the db

									Log.d("Database ", "Updated Successfully with boolean - true");

								} catch (Error e){
									e.printStackTrace();
									Log.d("Database ", "Was NOT updated with boolean");
								}

							} else {

								try {
									//Update the database
									ContentValues cv1 = new ContentValues();
									cv1.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "false");

									String[] whereArgs = {game_id};

									//Utilize the content resolver to add the values into the database
									contentResolver.update(ContentProviderClass.LOC_URI, cv1, dbHelper.COLUMN_GAME_ID + " =?", whereArgs); //Update data into the db

									Log.d("Database ", "Updated Successfully with boolean - false");
								} catch (Error e){
									e.printStackTrace();
									Log.d("Database ", "Was NOT updated with boolean");
								}

							}
						}
					});

					//This handles long presses. If the user long presses an icon, it will ask if they want to delete the game
					holder.tv_console_name.setOnLongClickListener(new View.OnLongClickListener() {
						public boolean onLongClick(View v) {
							//Dialog popup asking if they want to delete the record
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
										case DialogInterface.BUTTON_POSITIVE:
											try {
												//Delete the item from the database

												String[] whereArgs = {game_id};

												//Utilize the content resolver to delete a row from the database
												contentResolver.delete(ContentProviderClass.LOC_URI, dbHelper.COLUMN_GAME_ID + " =?", whereArgs); //Update data into the db

												//Reset the fragment to update the deleted item
												getFragmentManager().popBackStack();
											} catch (Exception e) {
												e.printStackTrace();
											}
											break;
									}
								}
							};
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Delete Game");
							builder.
									setMessage("Are you sure you want to delete this game from your collection?").
									setNegativeButton("No", dialogClickListener).
									setPositiveButton("Yes", dialogClickListener).
									show();

							return false;
						}
					});

				} else if (last_field_option.equalsIgnoreCase("Rate")){

					//Just in case there are more significant figures, trim down to the first character (IE 1.0 vs 1)
					rating1 = rating1.substring(0, Math.min(rating1.length(), 1));
					//If the value is not a mistake, set the rating in the view window
					if (rating1.equalsIgnoreCase("-1.0")){
						Log.d("Rating negative on Game: ", passed_game_id);
					} else {
						holder.ratingBar.setRating(Integer.parseInt(rating1));
					}

					/*
					Set the rating bar to an on click listener. If the user chooses a rating, it
					passes the score into the database for the respective game
					 */
					holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
						public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

							int score = (int) rating;
							if (score > 4){
								score = 4; //To prevent out of bounds issues
							}

							int score_to_add_to_db = score;

							String game_id = passed_game_id;

							try {
								//Update the database
								ContentValues cv1 = new ContentValues();
								cv1.put(dbHelper.COLUMN_RATING, Integer.toString(score_to_add_to_db));

								String[] whereArgs = {game_id};

								//Utilize the content resolver to add the values into the database
								contentResolver.update(ContentProviderClass.LOC_URI, cv1, dbHelper.COLUMN_GAME_ID + " =?", whereArgs); //Update data into the db

								Log.d("Database ", "Updated Successfully with a rating of: " + score_to_add_to_db);

							} catch (Error e){
								e.printStackTrace();
								Log.d("Database ", "Was NOT updated with rating of: " + score_to_add_to_db);
							}

						}
					});
				}
			}

			return rowView;
		}

	}

}
