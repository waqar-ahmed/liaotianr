package com.app.liaotianr.receiver;

import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.XMPPConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FileDownloadCompletionReceiver extends BroadcastReceiver{

	String mFilePath = "";
	static String TAG = "FileDownloadCompletionReciever";
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		mFilePath = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_COMPLETE);
		
		Utility.Log(TAG, mFilePath);
		
	}

}
