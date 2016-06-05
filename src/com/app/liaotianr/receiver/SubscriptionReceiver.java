package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.app.liaotianr.service.XMPPService;
import com.app.liaotianr.xmpp.XMPPConstants;
import com.app.liaotianr.xmpp.XMPPManager;

public class SubscriptionReceiver extends BroadcastReceiver{

	String from;
	String type;
	static String TAG = "SubscriptionReceiver";
	XMPPManager manager;
	private Message message;
	private Bundle bundle;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		from = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_FROM);
		type = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_TYPE);
		
		message = new Message();
		bundle = new Bundle();
		
		bundle = arg1.getExtras();
		message.setData(bundle);
		
		XMPPService.subscriptionHandler.sendMessage(message);
		
	}

}
