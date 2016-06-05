package com.app.liaotianr.xmpp;

import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DelayInformation;

import com.app.liaotianr.utilities.Utility;

import android.util.Log;

public class ChatMessageListener implements PacketListener{

	String TAG = "ChatMessageListener";
	
	public ChatMessageListener(){	
	}
	
	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		Message message = (Message) packet;
		String from = message.getFrom();
		Log.d(TAG, "Message received from : "+from+" ----> "+message);
		
		DelayInformation inf = null;
		try {
		    inf = (DelayInformation)packet.getExtension("x","jabber:x:delay");
		} catch (Exception e) {
		    Utility.Log(TAG,e.getMessage());
		}
		// get offline message timestamp
		if(inf!=null){
		    Date date = inf.getStamp();
		    Utility.Log(TAG,"got date yahooooo : "+date.toString());
		}else{
			Utility.Log(TAG, "got nullllllll");
		}
		//message received broadcast
		
//		if (mContext instanceof IMessageReceived) {
//			IMessageReceived mR = (IMessageReceived) mContext;
//			mR.onMessageReceived(from, message);
//			
//		}
		
	}

}
