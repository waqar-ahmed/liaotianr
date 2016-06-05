package com.app.liaotianr.xmpp;

import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.util.Log;

public class XMPPOfflineMessage {

	public static String TAG = "XMPPOfflineMessage";
	
    @SuppressWarnings("unchecked")
	public static List<Message> handleOfflineMessages(XMPPConnection connection)
            throws Exception {
    	
    	List<Message> msgs = null;
    	
            Log.i(TAG, "Retrieving offline messages...");
            OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);

            if (!offlineMessageManager.supportsFlexibleRetrieval()) {
        Log.d(TAG, "Offline messages not supported");
        return null;
    }

            if (offlineMessageManager.getMessageCount() == 0) {
                    Log.d(TAG, "No offline messages found on server");
            } else {
        msgs = (List<Message>) offlineMessageManager.getMessages();
        for (Message msg : msgs) {
            String fullJid = msg.getFrom();
            String messageBody = msg.getBody();
            if (messageBody != null) {
                Log.d(TAG, "Retrieved offline message from " + fullJid + " with content: " + messageBody.substring(0, Math.min(40, messageBody.length())));
            }
        }
        
        offlineMessageManager.deleteMessages();
    }
            Log.i(TAG, "End of retrieval of offline messages from server");
            
            return msgs;
    }
}
