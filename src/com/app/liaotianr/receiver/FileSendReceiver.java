package com.app.liaotianr.receiver;

import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.XMPPConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FileSendReceiver extends BroadcastReceiver{

	String status = "";
	static String TAG = "FileSendReciever";
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		status = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_SEND);
		
		Utility.Log(TAG, status);
		
	}

}
