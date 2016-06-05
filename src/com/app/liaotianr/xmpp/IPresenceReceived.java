package com.app.liaotianr.xmpp;


public interface IPresenceReceived {
	
	void onPresenceReceived(String from, int presenceStatus);
}
