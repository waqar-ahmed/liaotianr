package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.app.liaotianr.utilities.Utility;

public class ServerConnectingReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		Utility.Log("ServerConnectingService", "Connecting....");
		Toast.makeText(arg0, "connecting...", Toast.LENGTH_SHORT).show();
	}

}
