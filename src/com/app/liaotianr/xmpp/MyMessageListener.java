package com.app.liaotianr.xmpp;

import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DelayInformation;

import android.content.Context;
import android.content.Intent;

import com.app.liaotianr.utilities.Utility;

public class MyMessageListener implements MessageListener{

	String TAG = "MyMessageListener";
	Context mContext;
	String dateTime ="";
	
	public MyMessageListener(Context context){
		mContext = context;
	}
	
	@Override
	public void processMessage(Chat arg0, Message arg1) {
		// TODO Auto-generated method stub
		Utility.Log(TAG, arg1.getFrom() +" : "+ arg1.getBody());
		
		
		DelayInformation inf = null;
		try {
		    inf = (DelayInformation)arg1.getExtension("x","jabber:x:delay");
		} catch (Exception e) {
		    Utility.Log(TAG,e.getMessage());
		}
		
		// get offline message timestamp
		if(inf!=null){
			Utility.Log(TAG, "received offline message");
		    Date date = inf.getStamp();
//		    Utility.Log(TAG,"got date yahooooo : "+date.toString());   // Mon Dec 29 14:45:54 CST (Taipei) 2014
//		    Utility.Log(TAG,"got date yahooooo : "+date.toGMTString());   //29 Dec 2014 07:37:31 GMT
//		    Utility.Log(TAG,"got date yahooooo : "+date.toLocaleString());  //Dec 29, 2014 3:37:31 PM
		    dateTime = date.toGMTString();
			Utility.Log(TAG, dateTime);
		    
		}
		else{
			Utility.Log(TAG, "Not an offline message");
			dateTime = XMPPUtils.getCurrentGMTDate();
			Utility.Log(TAG, dateTime);
		}
		
		Intent intent = new Intent();
        intent.setAction(XMPPIntentFilter.ACTION_XMPP_MESSAGE_RECEIVED);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_JID, arg1.getFrom());
        intent.putExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_BODY, arg1.getBody());
        intent.putExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_DATE_TIME, dateTime);
        mContext.sendBroadcast(intent);
		
	}
	

}
