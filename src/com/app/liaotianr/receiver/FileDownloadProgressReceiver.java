package com.app.liaotianr.receiver;

import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.XMPPConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FileDownloadProgressReceiver extends BroadcastReceiver{

	String status = "";
	double progress = 0.0;
	static String TAG = "FileDownloadProgressReciever";
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		status = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_STATUS);
		progress = arg1.getDoubleExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_PROGRESS, 0.0);
		
		//Utility.Log(TAG, status + " : " + progress);
		
		if(status.equals(XMPPConstants.FILE_ALREADY_EXIST)){
			Utility.Log(TAG, "Unable to receive file!, File already exist");
		}
		
		if(status.equals(XMPPConstants.FILE_DOWNLOAD_ERROR)){
			Utility.Log(TAG, "Error Occured!, User cancelled the file sending or error downloading");
		}
		else{
			// show to user file progress
			Utility.Log(TAG, status + " : " + progress);
		}
	}

}
