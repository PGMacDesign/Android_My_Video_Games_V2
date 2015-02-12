package com.pgmacdesign.myvideogamesv2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.pgmacdesign.myvideogamesv2.Database.DBFunctions;

/*
The Main activity, will allow users to choose between 2 options, going to the ListGamesActivity or
to the RateGamesActivity. There will be NO action bar on purpose.
 */
public class MainActivity extends Activity implements View.OnClickListener{

	//2 Image Buttons. When clicked, they will open up the respective activity
	ImageButton rank_my_games_button, list_my_games_button;

	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Initialize any and all variables / views / buttons needed
		Initialize();

		//Check if the database exists. If it does not, create it and populate it with some fictitious data for example.
		final String PREFS_NAME = "MyPrefsFile";
		settings = this.getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean("my_first_time0", true)) {
			//The app is being launched for first time, do something
			DBFunctions.setupDBWithInitialValues(this);
		}

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


}
