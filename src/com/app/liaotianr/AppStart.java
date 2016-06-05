package com.app.liaotianr;

import android.app.Application;
import android.content.Intent;

import com.app.liaotianr.service.XMPPService;

public class AppStart extends Application{

	Intent intent;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		startService(new Intent(this, XMPPService.class));
	
	}
	
	
	
}
