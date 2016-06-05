package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.XMPPManager;

public class ServerConnectedReceiver extends BroadcastReceiver{

	XMPPManager manager;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		Utility.Log("ServerConnectedService", "Connected....");
		Toast.makeText(arg0, "connetced", Toast.LENGTH_SHORT).show();
	}
}