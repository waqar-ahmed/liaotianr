package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.app.liaotianr.activity.RegisterActivity;

public class AccountCreationReceiver extends BroadcastReceiver{

	int code = -1;
	static String TAG = "AccountCreationReceiver";
	Message message;
	Bundle bundle;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		message = new Message();
		bundle = new Bundle();
		
		bundle = arg1.getExtras();
		message.setData(bundle);
		
		RegisterActivity.handler.sendMessage(message);
		
//		code = arg1.getIntExtra(XMPPConstants.IntentKeys.XmppAccountCreationKey.XMPP_ACCOUNT_CREATION_CODE, -1);
//		if(code == XMPPConstants.XMPP_ACCOUNT_CREATION_CONFLICT){
//			Utility.Log(TAG, "Sorry!, User already exist with this username");
//		}
//		else if(code == XMPPConstants.XMPP_ACCOUNT_CREATION_SUCCESSFULL){
//			Utility.Log(TAG, "Account created Successfully");
//		}
//		else if(code == XMPPConstants.XMPP_ACCOUNT_CREATION_ERROR){
//			Utility.Log(TAG, "Sorry!, Error occured during creation of account");
//		}
//		else{
//			Utility.Log(TAG, "Some Internal error occured");
//		}	
		
	}
}
