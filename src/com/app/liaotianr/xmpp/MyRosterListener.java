package com.app.liaotianr.xmpp;

import java.util.Collection;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.content.Intent;

import com.app.liaotianr.utilities.Utility;

public class MyRosterListener implements RosterListener{

	String TAG = "RosterListener";
	Context mContext;
	
	public MyRosterListener(Context context){
		mContext = context;
	}
	
	@Override
	public void entriesAdded(Collection<String> arg0) {
		// TODO Auto-generated method stub
		for(String s : arg0){
		Utility.Log(TAG, "IN_ENTRIES_ADDED --> Request received from : "+s);
		}
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		// TODO Auto-generated method stub
		for(String s : arg0)
			Utility.Log(TAG, "IN_ENTRIES_DELETED --> Request delete from : "+s);
		
		Intent intent = new Intent();
        intent.setAction(XMPPIntentFilter.ACTION_XMPP_ROSTER_MODIFIED);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mContext.sendBroadcast(intent);
		
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		// TODO Auto-generated method stub
		for(String s : arg0)
			Utility.Log(TAG, "IN_ENTRIES_UPDATED --> Request updated from : "+s);
	}

	@Override
	public void presenceChanged(Presence arg0) {
		// TODO Auto-generated method stub
		Utility.Log(TAG, "Presence received from : "+arg0.getFrom() + " --> "+ arg0.getStatus()+" : "+arg0);
		
		Intent intent = new Intent();
        intent.setAction(XMPPIntentFilter.ACTION_XMPP_PRESENCE_RECEIVED);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(XMPPConstants.IntentKeys.XmppPresenceKey.XMPP_PRESENCE,arg0.getFrom());
        mContext.sendBroadcast(intent);
		
		//broadcast presence recieved intent
		
//		if (mContext instanceof IPresenceReceived) {
//			IPresenceReceived pR = (IPresenceReceived) mContext;
//			pR.onPresenceReceived(arg0.getFrom(), PresenceStatus.getPresenceStatus(arg0));
//		}
	}

}
