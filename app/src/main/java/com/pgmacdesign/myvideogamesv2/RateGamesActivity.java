package com.pgmacdesign.myvideogamesv2;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by PatrickSSD2 on 2/10/2015.
 */
public class RateGamesActivity extends Activity implements FragmentManager.OnBackStackChangedListener {


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_fragment_holder);


		//Set it to work with the backstackchanged listener
		//manager.addOnBackStackChangedListener(this);
	}


	public void onBackStackChanged() {


	}

	//At some point use
	//manager.popBackStack();
}
