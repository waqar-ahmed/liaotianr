package com.app.liaotianr.activity;

import com.app.liaotianr.MyPreference;
import com.app.liaotianr.R;
import com.app.liaotianr.MyPreference.Keys;
import com.app.liaotianr.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SplashActivity extends ActionBarActivity {

	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		showSplash();
		
		if(!MyPreference.getBoolean(getApplicationContext(), MyPreference.Keys.IS_USER_REGISTERED)){
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{
			intent = new Intent(this, FriendListActivity.class);
			startActivity(intent);
			finish();
		}
	}

	void showSplash(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
