package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.app.liaotianr.service.XMPPService;
import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.XMPPConstants;

public class XMPPMessageReceiver extends BroadcastReceiver{

	String from;
	String body;
	private Message message;
	private Bundle bundle;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		from = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_JID);
		body = arg1.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_BODY);
		Utility.Log("MessageReciever", from + " : " + body);
		
		message = new Message();
		bundle = new Bundle();
		
		bundle = arg1.getExtras();
		message.setData(bundle);
		
		XMPPService.messageHandler.sendMessage(message);
	}

}
