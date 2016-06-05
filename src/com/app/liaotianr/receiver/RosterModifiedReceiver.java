package com.app.liaotianr.receiver;

import com.app.liaotianr.service.XMPPService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RosterModifiedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		XMPPService.presenceHandler.sendEmptyMessage(0);
	}

}
