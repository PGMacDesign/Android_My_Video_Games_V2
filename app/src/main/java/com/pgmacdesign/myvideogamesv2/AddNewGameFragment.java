package com.pgmacdesign.myvideogamesv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgmacdesign.myvideogamesv2.Database.ContentProviderClass;
import com.pgmacdesign.myvideogamesv2.Database.DbHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by PatrickSSD2 on 2/10/2015.
 */
public class AddNewGameFragment extends Fragment implements AdapterView.OnItemClickListener {

	//Button to add a new game
	Button add_new_game;

	//Allows list to be accessed in onItemClicked
	List<Integer> search_game_list_id = new ArrayList<>();

	//Edit Texts, where the user can enter the game names
	EditText edit_text_game_name;

	//List of fun facts
	String[] facts;

	//Some global strings
	String search_response0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//This Array will be used to show when the user presses the add game button
		facts = getActivity().getResources().getStringArray(R.array.video_game_facts);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate the add new game layout view
		View view = inflater.inflate(R.layout.add_new_game_layout, container, false);

		//Set the text views up
		this.edit_text_game_name = (EditText) view.findViewById(R.id.edit_text_game_name);

		//Check the saved instance state (in case they rotate it while typing)
		if (savedInstanceState == null){
		} else {
			//Set strings to the respective edit text fields saved in the bundle
			String game_name = savedInstanceState.getString("edit_text_game_name");

			//In case something goes wonky with the pulling of saved instance state
			try {
				edit_text_game_name.setText(game_name); //Set the edit text name chosen back
			} catch (NullPointerException e){
				e.printStackTrace();
			}
		}

		//Set the button up and initiate its onclicklistener
		this.add_new_game = (Button) view.findViewById(R.id.add_game_button);
		this.add_new_game.setOnClickListener(new View.OnClickListener() {
			//This will take the input data and search the web for the respective game
			public void onClick(View v) {
				//Disables the button in case they click it multiple times
				add_new_game.setEnabled(false);

				//Clear the list of game IDs so no residuals carry over
				search_game_list_id.clear();

				//These 4 lines of code hide the keyboard when the user presses the button
				InputMethodManager inputManager = (InputMethodManager)
						getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				Toast.makeText(getActivity(), "Loading your results now", Toast.LENGTH_LONG).show();

				String game_name = edit_text_game_name.getText().toString();

				if (game_name.equalsIgnoreCase("")){
					add_new_game.setEnabled(false);
				} else if (game_name == null){
					add_new_game.setEnabled(false);
				} else {

					int length = game_name.length();
					//If the String ends with a whitespace (IE the auto complete helped out), delete the whitespace
					if (Character.isWhitespace(game_name.charAt(length - 1))) {
						game_name = game_name.substring(0, game_name.length() - 1);
					}
					//Replace all remaining whitespace (IE space in a name, Final Fantasy) with an underscore
					game_name = game_name.replaceAll(" ", "_").toLowerCase();

					try {
						//Choose a random fact from the string array
						String randomStr = facts[new Random().nextInt(facts.length)];

						//Setup a Dialog popup to entertain the user for the seconds until the server response comes
						/*
						(Side note, people's attention spans have gotten bad as even I need something
						to entertain me for 10 seconds these days... Squirrel!)
						 */
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
								}
							}
						};
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("This may take up to 10 seconds, so here is a random fact:");
						builder.setMessage(randomStr).setNegativeButton("Close", dialogClickListener).show();

					} catch (NullPointerException e){
						Toast.makeText(getActivity(), "Oops! Something went wrong! Please check your internet connection and try again",Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}

					//New async task to run in the background. Pass in the 2 strings
					try {
						String web_url_to_send = "http://www.giantbomb.com/api/search/?api_key=9be0ead91d814eeb64cc5fb0da481ce726a22400&query=";
						String web_url_to_send_2 = "&field_list=name,id,platforms&format=json";
						search_response0 = new AsyncBackgroundClass(web_url_to_send, game_name, web_url_to_send_2).execute().get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

					//VideoGames object to hold the response of the Deserialized code
					VideoGames search_videogames = new VideoGames();
					/*
					Now that the search results from the server are available,
					deserialize the string and add the data to the object
					 */
					search_videogames = deserializeTheJSON(search_videogames, search_response0);

					add_new_game.setEnabled(true);

					//Pass the video games object into the class that handles the listview
					userMakesChoice(search_videogames);
				}
			}
		});

		return view;
	}

	//This takes the searched data and implements it into the list view. It also sets up the list view click listener
	private void userMakesChoice(VideoGames search_videogames) {
		//Fill the List with data to add to the listview
		List<String> search_game_list = new ArrayList<>();

		//First, clear the list in case there were other ones previously:
		search_game_list.clear();

		//Fill the search game list with items from the video game object
		for(int i = 0; i < search_videogames.results.length; i++){
			//Add the game name
			String the_game_name0 = search_videogames.results[i].game_name;
			String the_game_name1 = null;
			String the_game_name2 = null;

			try {
				the_game_name1 = " Platform: " + search_videogames.results[i].platforms[0].system_name;
			} catch (NullPointerException e){
				e.printStackTrace();
			}
			try {
				the_game_name2 = " AKA: " + search_videogames.results[i].platforms[0].abbreviation;
			} catch (NullPointerException e){
				e.printStackTrace();
			}

			//Some results may return null on the platform or the abbreviation. If so, add those details here
			if (the_game_name1 != null){
				the_game_name0 += the_game_name1;
			}
			if (the_game_name2 != null){
				the_game_name0 += the_game_name2;
			}

			//Add it to the list
			search_game_list.add(the_game_name0);

			//Add the game ID
			int game_id = search_videogames.results[i].game_id;
			search_game_list_id.add(game_id);
		}

		//Just in case no results are returned
		if (search_game_list.size() == 0){
			Toast.makeText(getActivity(), "Oops! No results turned up!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getActivity(), "Try entering in the console name differently (IE NES instead of Nintendo or PS2 instead of Playstation 2)", Toast.LENGTH_LONG).show();
		}

		//Create the arrayAdapter that uses the list created above as the data
		ArrayAdapter<String> arrayAdapter =
				new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, search_game_list);
		//Create the listview
		ListView listView = (ListView) getActivity().findViewById(R.id.add_game_listview);

		//Set the datapter
		listView.setAdapter(arrayAdapter);
		listView.setClickable(true);

		/*
		This item click listener manages the list view. When the user chooses a video game from
		the list, it is added to their database of video games
		*/
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//Position chosen will match the position of the lists created above.
		int gameID = search_game_list_id.get(position);

		//For debugging purposes
		Log.d("ID Chosen", Integer.toString(gameID));

		//If ID is -1, then they clicked on the "oops" error, skip it
		if (gameID == -1){
			Toast.makeText(getActivity(), "That game may not be correct, try a different one perhaps?", Toast.LENGTH_LONG).show();
		} else {
			//Run the async background task to query the server for the response data
			try {
				String web_url_to_send = "http://www.giantbomb.com/api/games/?api_key=9be0ead91d814eeb64cc5fb0da481ce726a22400&filter=id:";
				String web_url_to_send_2 = "&limit=5&format=json";
				String response = new AsyncBackgroundClass(web_url_to_send, Integer.toString(gameID), web_url_to_send_2).execute().get();

				Toast.makeText(getActivity(), "Your game is being added to the list, please wait a moment", Toast.LENGTH_SHORT).show();

				VideoGames videoGames = new VideoGames();
				videoGames = deserializeTheJSON(videoGames, response);

				//Passes the video games object that was just deserialized to the task that will place it into the db and then pop the fragment
				new AddToDatabaseTask(videoGames).execute();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}


		}
	}

	//This async class sends data to the server and returns a JSON string
	private class AsyncBackgroundClass extends AsyncTask<Void, Void, String>{

		private String starting_url, game_details, ending_url;
		//Constructor class to allow for 3 different passed in items

		public AsyncBackgroundClass(String str1, String str2, String str3){
			this.starting_url = str1;
			this.game_details = str2;
			this.ending_url = str3;
		}

		//Pass in String params for the URL pass values.
		protected String doInBackground(Void... params) {
			String async_server_response;

			//Makes a web url of the passed 3 parameters
			String web_url = starting_url + game_details + ending_url;

			//New server request object
			ServerRequest sr = new ServerRequest();
			/*
			Send a server request with the web url.
			The second parameter is the method. can either be get or post
			 */
			async_server_response = sr.makeServiceCall(web_url, ServerRequest.GET);
			//Return the String of the response. This make take anywhere from 0-15 seconds depending on the server load
			return async_server_response;
		}
	}

	//This async class manages adding items to the database and then once finished, popping the backstack
	private class AddToDatabaseTask extends AsyncTask<Void, Void, Void>{
		//Video games object for passing through/ to
		private VideoGames db_video_games;

		//Constructor
		public AddToDatabaseTask(VideoGames vg){
			this.db_video_games = vg;
		}

		protected Void doInBackground(Void... params) {
			//Put the data into the database
			putDataInDatabase(db_video_games);
			return null;
		}

		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			//Pop back to the previous fragment
			getFragmentManager().popBackStack();
		}
	}

	//This class will put the data into the database. Returns the row number of the row put into
	private void putDataInDatabase(VideoGames videogames_database_info){

		//Create a list of all the data to input into the database from the VideoGames Object
		String db_game_id = Integer.toString(videogames_database_info.results[0].game_id);

		String db_aliases = videogames_database_info.results[0].aliases;
		db_aliases = fixNullString(db_aliases);

		String db_deck = videogames_database_info.results[0].deck;
		db_deck = fixNullString(db_deck);

		String db_icon_url = videogames_database_info.results[0].game_image.icon_url;
		db_icon_url = formatTheBadURL(db_icon_url);

		String db_medium_url = videogames_database_info.results[0].game_image.medium_url;
		db_medium_url = formatTheBadURL(db_medium_url);

		String db_name = videogames_database_info.results[0].game_name;
		db_name = fixNullString(db_name);

		//Shortening this one as it adds seconds afterwards (semi-useless here)
		String str = videogames_database_info.results[0].original_release_date;
		String db_original_release_date;
		if (str != null){
			db_original_release_date = str.substring(0, Math.min(str.length(), 10)); //First 10 Characters
		} else {
			db_original_release_date = "N/A";
		}

		String db_platform_name = videogames_database_info.results[0].platforms[0].system_name;
		db_platform_name = fixNullString(db_platform_name);

		String db_platform_abbreviation = videogames_database_info.results[0].platforms[0].abbreviation;
		db_platform_abbreviation = fixNullString(db_platform_abbreviation);

		//Now that the strings have been setup, pass the values into the database via the content Provider
		DbHelper dbHelper = new DbHelper(getActivity());
		//Database to pass values into
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		//Content Values
		ContentValues cv = new ContentValues();

		//Pass the values into the Content Values
		cv.put(dbHelper.COLUMN_GAME_ID, db_game_id);
		cv.put(dbHelper.COLUMN_ALIASES, db_aliases);
		cv.put(dbHelper.COLUMN_DECK, db_deck);
		cv.put(dbHelper.COLUMN_ICON_URL, db_icon_url);
		cv.put(dbHelper.COLUMN_MEDIUM_URL, db_medium_url);
		cv.put(dbHelper.COLUMN_NAME, db_name);
		cv.put(dbHelper.COLUMN_ORIGINAL_RELEASE_DATE, db_original_release_date);
		cv.put(dbHelper.COLUMN_PLATFORM_NAME, db_platform_name);
		cv.put(dbHelper.COLUMN_PLATFORM_ABBREVIATION, db_platform_abbreviation);
		cv.put(dbHelper.COLUMN_PLAYED_CHECKBOX, "false");
		cv.put(dbHelper.COLUMN_RATING, "0");

		//Content Resolver to handle the content provider data being passed
		ContentResolver contentResolver = getActivity().getContentResolver();
		//Insert data into the db
		contentResolver.insert(ContentProviderClass.LOC_URI, cv);
	}

	//This formats the badly formed URL links. Not sure why the site owners returned it that way
	private String formatTheBadURL(String db_icon_url) {
		String return_url;
		return_url = fixNullString(db_icon_url);
		return_url = return_url.replaceAll("\\\\", "");
		Log.d("Formatted String", return_url);
		return return_url;
	}

	//Just a simple method to fix any null values passed in by the server (IE Console name)
	private String fixNullString(String str){
		if (str != null){
			return str;
		} else {
			return "N/A";
		}
	}

	/**
	This class manages the server requests sent out to the web server (That was about as helpful of a
	description as the redundancy department of redundancy eh?)
	*/
	public class ServerRequest {

		String server_response = null;
		public final static int GET = 1;
		public final static int POST = 2;

		public ServerRequest() {
		}

		/*
		Making service call
		@url - url to make request
		@method - http request method
		*/
		public String makeServiceCall(String url, int method) {
			return this.makeServiceCall(url, method, null);
		}

		/*
		Making service call
		@url - url to make request
		@method - http request method
		@params - http request params
		*/
		public String makeServiceCall(String url, int method,
		                              List<NameValuePair> params) {
			try {
				// http client
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpEntity httpEntity = null;
				HttpResponse httpResponse = null;

				// Checking http request method type
				if (method == POST) {
					HttpPost httpPost = new HttpPost(url);
					// adding post params
					if (params != null) {
						httpPost.setEntity(new UrlEncodedFormEntity(params));
					}

					httpResponse = httpClient.execute(httpPost);

				} else if (method == GET) {

					// appending params to url
					if (params != null) {
						String paramString = URLEncodedUtils
								.format(params, "utf-8");
						url += "?" + paramString;
					}

					HttpGet httpGet = new HttpGet(url);

					httpResponse = httpClient.execute(httpGet);

				}
				httpEntity = httpResponse.getEntity();
				server_response = EntityUtils.toString(httpEntity);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return server_response;

		}
	}

	//Returns a VideoGames object after deserializing the data. @Params, VideoGames Object to return, The JSON String to be deserialized
	private VideoGames deserializeTheJSON(VideoGames videoGames, String JSONString){

		//GSON object to assist with deserialization
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();

		try {
			//Deserialize the data and put the data into the videogames object
			//(This has got to be the most useful thing ever btw)
			videoGames = gson.fromJson(JSONString, VideoGames.class);
		} catch (Exception e){
			e.printStackTrace();
		}

		//Return the object
		return videoGames;
	}

	/*
	To save data using the saved instance state bundle. Mostly used for if a user rotates a screen,
	Which causes the screen to lose the saved data. This way, they won't lose what they were typing.
	*/
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Get the string in the edit text fields
		String game_name = edit_text_game_name.getText().toString();

		//If the text is not null, then set it to the saved instance state
		if (game_name != null){
			outState.putString("edit_text_game_name", game_name);
		}
	}
}
