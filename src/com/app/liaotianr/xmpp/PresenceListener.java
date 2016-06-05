package com.app.liaotianr.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.content.Intent;

public class PresenceListener implements PacketListener{

	String TAG = "PresenceListener";
	static Context mContext;
	
	public PresenceListener(Context context){
		mContext = context;
	}
	
	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		Presence presence = (Presence) packet;
		String from = presence.getFrom();
		
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.putExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_FROM, from);
        
		//Utility.Log(TAG, "Presence received from : "+from + " --> "+(presence));
		
		if(Presence.Type.subscribe.equals(presence.getType())){
			//Utility.Log(TAG, "subscribe request");
			intent.setAction(XMPPIntentFilter.ACTION_XMPP_PRESENCE_SUBSCRIBE);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_TYPE, XMPPConstants.Subscription.XMPP_PRESENCE_SUBSCRIBE);

		}
		
		else if(Presence.Type.subscribed.equals(presence.getType())){
			//Utility.Log(TAG, "some one subscribed");
			intent.setAction(XMPPIntentFilter.ACTION_XMPP_PRESENCE_SUBSCRIBED);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_TYPE, XMPPConstants.Subscription.XMPP_PRESENCE_SUBSCRIBED);

		}
		
		else if(presence.getType().equals(Presence.Type.unsubscribe)){
			//Utility.Log(TAG, "unsubscribe request");
			intent.setAction(XMPPIntentFilter.ACTION_XMPP_PRESENCE_UNSUBSCRIBE);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_TYPE, XMPPConstants.Subscription.XMPP_PRESENCE_UNSUBSCRIBE);

		}
		
		else if(presence.getType().equals(Presence.Type.unsubscribed)){
			//Utility.Log(TAG, "someone unscribed you");
			intent.setAction(XMPPIntentFilter.ACTION_XMPP_PRESENCE_UNSUBSCRIBED);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_TYPE, XMPPConstants.Subscription.XMPP_PRESENCE_UNSUBSCRIBED);

		}
		
		else {
			intent = null;
		}
	
		if(intent != null)
			mContext.sendBroadcast(intent);
	}

}
