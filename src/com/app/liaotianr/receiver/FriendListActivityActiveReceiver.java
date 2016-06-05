package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.liaotianr.service.XMPPService;
import com.app.liaotianr.xmpp.XMPPManager;

public class FriendListActivityActiveReceiver extends BroadcastReceiver{

	XMPPManager manager;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
			XMPPService.isFriendListActivityVisible = arg1.getBooleanExtra("state", false);
	}
}