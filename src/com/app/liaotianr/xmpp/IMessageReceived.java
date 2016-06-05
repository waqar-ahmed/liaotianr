package com.app.liaotianr.xmpp;

import org.jivesoftware.smack.packet.Message;

public interface IMessageReceived {
	
	void onMessageReceived(String from, Message message);
}
