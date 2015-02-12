package com.pgmacdesign.myvideogamesv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.pgmacdesign.myvideogamesv2.Database.ContentProviderClass;
import com.pgmacdesign.myvideogamesv2.Database.DbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 This class will serve as the fragment that works with the listview to hold/ show data
 */
public class ListFragment extends Fragment{
	//Custom adapter to be accessed via setter methods accessible to the activities.
	CustomAdapter adapter;

	//CustomAdapter.Holder holder2; //May need to add this back in
	public CustomAdapter.Holder holder;

	//The listview
	ListView listView;

	//When the view is created
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate the add new game layout view
		View view = inflater.inflate(R.layout.list_view_holder, container, false);

		listView = (ListView) view.findViewById(R.id.data_list_view);

		//Set the background color
		listView.setBackgroundColor(Color.GRAY);

		return view;
	}

	//This class manages the adapter setup for the listView
	public void setupTheList(List<List<String>> lists_of_records, String str, DbHelper dbHelper1, ContentResolver contentResolver1){
		//(ContentResolver contentResolver = ListGamesActivity.this.getContentResolver();)
		//Set the custom adapter to the listview
		adapter = new CustomAdapter(getActivity(), lists_of_records, str, dbHelper1, contentResolver1);
		listView.setAdapter(adapter);
	}



	///////////////////////////////////////////////////////////////////////////////////////
	//This custom adapter is used to fill the respective data into the listview
	public class CustomAdapter extends BaseAdapter {

		List<List<String>> all_passed_data = new ArrayList<>();
		Context context;
		private LayoutInflater inflater = null;
		private String last_field_option;
		private DbHelper dbHelper;
		private ContentResolver contentResolver;

		//Custom Adapter Constructor
		public CustomAdapter(Context context, List<List<String>> passed_list, String str, DbHelper dbHelper, ContentResolver contentResolver) {
			this.all_passed_data = passed_list;
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			last_field_option = str;
			this.dbHelper = dbHelper;
			this.contentResolver = contentResolver;
		}

		@Override
		public int getCount() {
			return all_passed_data.size();
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
			public TextView tv_game_name, tv_console_name, tv_bottom;
			public ImageView imageView;

			//These will change depending on which fragment is opened
			public LinearLayout bottom_left, bottom_right;

			//Checkbox and Ratingbar objects
			public RatingBar ratingBar;
			public CheckBox checkBox;

		}

		//Custom view
		public View getView(final int position, View convertView, ViewGroup parent) {

			//A holder object to reference the textviews and imageviews
			holder = new Holder();
			View rowView;
			rowView = inflater.inflate(R.layout.custom_list_view, null);

			//Display metrics are used to calculate the dp of the user's screen, which helps give a cleaner look when setting width
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics();
			display.getMetrics(outMetrics);
			float density = getResources().getDisplayMetrics().density;
			float dpWidth = outMetrics.widthPixels / density;

			//Setup all of the UI components by referencing the holder and then the respective UI component
			holder.imageView = (ImageView) rowView.findViewById(R.id.custom_list_view_image);
			holder.imageView.setMaxWidth((int) (dpWidth) * (30 / 100));
			holder.imageView.setPadding(15, 15, 15, 15);

			holder.tv_game_name = (TextView) rowView.findViewById(R.id.custom_list_view_game_name);
			holder.tv_game_name.setMaxWidth((int) (dpWidth) * (30 / 100));
			holder.tv_game_name.setTextColor(Color.RED);
			holder.tv_game_name.setPadding(5, 0, 5, 0);

			holder.tv_console_name = (TextView) rowView.findViewById(R.id.custom_list_view_console);
			holder.tv_console_name.setMaxWidth((int) (dpWidth) * (35 / 100));
			holder.tv_console_name.setTextColor(Color.BLUE);
			holder.tv_console_name.setPadding(5, 0, 0, 0);

			holder.bottom_left = (LinearLayout) rowView.findViewById(R.id.left_layout);

			holder.bottom_right = (LinearLayout) rowView.findViewById(R.id.right_layout);

			holder.tv_bottom = new TextView(context);
			holder.ratingBar = new RatingBar(context); //On a side note, why is the rating bar ridiculously huge? I mean, enormous and in the way huge...
			holder.ratingBar.setStepSize(1);
			holder.ratingBar.setNumStars(4);
			holder.checkBox = new CheckBox(context);

			holder.tv_bottom.setPadding(5, 5, 5, 5); //Add some padding so they are not right up against each other

			//Loop through the list to see what is in it and set the different fields
			for (int i = 0; i < all_passed_data.size(); i++) {
				List<String> temp_list = all_passed_data.get(position);

				String game_id = temp_list.get(0);

				final String passed_game_id = game_id;

				holder.tv_game_name.setText(temp_list.get(5)); //Game Name
				holder.tv_console_name.setText(temp_list.get(7)); //Platform Name

				//Setting the image using the third party library picasso
				String icon_url = temp_list.get(3);

				//Set the image using a bitmap returned from the URL
				Picasso.with(context).load(icon_url).into(holder.imageView);

				//Check which fragment we are in
				if (last_field_option.equalsIgnoreCase("List")) {

					holder.bottom_left.addView(holder.checkBox);
					holder.bottom_right.addView(holder.tv_bottom);

					holder.tv_bottom.setGravity(Gravity.CENTER); //Set to center so it aligns better
					holder.checkBox.setGravity(Gravity.CENTER); //Set to center so it aligns better
					holder.bottom_left.setGravity(Gravity.CENTER); //Set to center so it aligns better
					holder.checkBox.setPadding(5, 5, 5, 5);

					holder.tv_bottom.setText("Have you finished playing the game?");

					holder.checkBox.setChecked(false);

					//Set the checkbox
					String played_game = temp_list.get(9);
					if (played_game.equalsIgnoreCase("true")) {
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

							if (isChecked) {

								try {
									//Update the database
									ContentValues cv1 = new ContentValues();
									cv1.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "true");

									//Utilize the content resolver to add the values into the database
									contentResolver.insert(ContentProviderClass.LOC_URI, cv1); //Insert data into the db

									Log.d("Database ", "Updated Successfully with boolean - true");

								} catch (Error e) {
									e.printStackTrace();
									Log.d("Database ", "Was NOT updated with boolean");
								}

							} else {

								try {
									//Update the database
									ContentValues cv1 = new ContentValues();
									cv1.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "false");

									//Utilize the content resolver to add the values into the database
									contentResolver.insert(ContentProviderClass.LOC_URI, cv1); //Insert data into the db

									Log.d("Database ", "Updated Successfully with boolean - false");
								} catch (Error e) {
									e.printStackTrace();
									Log.d("Database ", "Was NOT updated with boolean");
								}

							}
							//Update the adapter to notify it a change has been made
							adapter.notifyDataSetChanged();
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
												List<String> temp_list2 = all_passed_data.get(position);
												String game_id = temp_list2.get(0);

												//Update the database
												ContentValues cv1 = new ContentValues();
												cv1.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "true");
												String[] whereArgs = {game_id};
												//Utilize the content resolver to delete the values from the database
												contentResolver.delete(ContentProviderClass.LOC_URI, dbHelper.COLUMN_GAME_ID + " =?", whereArgs);

												//Update the adapter to notify it a change has been made
												adapter.notifyDataSetChanged();
												//Reset the fragment to update the deleted item
	////\/\/\/\/\/\/\/\/\/\/////////////////////////////////										//getFragmentManager().popBackStack();
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

				} else if (last_field_option.equalsIgnoreCase("Rate")) {

					holder.bottom_left.addView(holder.tv_bottom);
					holder.bottom_right.addView(holder.ratingBar);

					holder.tv_bottom.setGravity(Gravity.LEFT); //Set text to center so it aligns better
					holder.ratingBar.setPadding(5, 5, 5, 5);

					holder.tv_bottom.setText("Rate your game");

					//Get the rating on the game
					String rating1 = temp_list.get(10);
					//Just in case there are more significant figures, trim down to the first character (IE 1.0 vs 1)
					rating1 = rating1.substring(0, Math.min(rating1.length(), 1));
					//If the value is not a mistake, set the rating in the view window
					if (rating1.equalsIgnoreCase("-1.0")) {
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
							if (score > 4) {
								score = 4; //To prevent out of bounds issues
							}

							int score_to_add_to_db = score;

							String game_id = passed_game_id;

							try {
								//Update the database
								ContentValues cv1 = new ContentValues();
								cv1.put(dbHelper.COLUMN_RATING, Integer.toString(score_to_add_to_db));

								//Utilize the content resolver to add the values into the database
								contentResolver.insert(ContentProviderClass.LOC_URI, cv1); //Insert data into the db

								Log.d("Database ", "Updated Successfully with a rating of: " + score_to_add_to_db);

							} catch (Error e) {
								e.printStackTrace();
								Log.d("Database ", "Was NOT updated with rating of: " + score_to_add_to_db);
							}

							//Update the adapter to notify it a change has been made
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
			return rowView;
		}
	}
}