package com.app.liaotianr.xmpp;

public class XMPPIntentFilter {
	
	static String PACKAGE_NAME = "com.app.liaotianr.xmpp";
	
	//Connection related Intents
	public static String ACTION_XMPP_CONNECTED = PACKAGE_NAME + ".connected";
	public static String ACTION_XMPP_CONNECTING = PACKAGE_NAME + ".connecting";
	public static String ACTION_XMPP_DISCONNECTING = PACKAGE_NAME + ".disconnecting";
	public static String ACTION_XMPP_DISCONNECTED = PACKAGE_NAME + ".disconnected";
	
	//Message and Presence related iIntents
	public static String ACTION_XMPP_MESSAGE_RECEIVED = PACKAGE_NAME + ".message_received";
	public static String ACTION_XMPP_PRESENCE_RECEIVED = PACKAGE_NAME + ".presence_received";
	
	//File download related Intents
	public static String ACTION_XMPP_FILE_SEND_REQUEST = PACKAGE_NAME + ".file_send_request"; 
	public static String ACTION_XMPP_FILE_DOWNLOAD_PROGRESS = PACKAGE_NAME + ".file_progress";
	public static String ACTION_XMPP_FILE_DOWNLOAD_COMPLETE = PACKAGE_NAME + ".file_download_complete"; 
	
	//Account creation Intent
	public static String ACTION_XMPP_ACCOUNT_CREATION = PACKAGE_NAME + ".account_creation";
	
	//Subscription Intents
	public static String ACTION_XMPP_PRESENCE_SUBSCRIBE = PACKAGE_NAME + ".presence_subscribe";
	public static String ACTION_XMPP_PRESENCE_SUBSCRIBED = PACKAGE_NAME + ".presence_subscribed";
	public static String ACTION_XMPP_PRESENCE_UNSUBSCRIBE = PACKAGE_NAME + ".presence_unsubscribe";
	public static String ACTION_XMPP_PRESENCE_UNSUBSCRIBED = PACKAGE_NAME + ".presence_unsubscribed";
	
	//Roster related Intent
	public static String ACTION_XMPP_ROSTER_MODIFIED = PACKAGE_NAME + ".roster_modified";

}
