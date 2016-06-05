package com.app.liaotianr.xmpp;

public class XMPPConstants {
	
	//File send and receive related constants
	public static final String FILE_DOWNLOAD_ERROR = "download_error";
	public static final String FILE_ALREADY_EXIST = "file_exist";
	public static final String FILE_NOT_EXIST = "file_not_exist";
	public static final String FILE_SENDING_ERROR = "sending_error";
	public static final String FILE_SUCCESSFULLY_SENT = "file_successfully_sent";
	
	//Account creation related Constants
	public static final int XMPP_ACCOUNT_CREATION_CONFLICT = 409;
	public static final int XMPP_ACCOUNT_CREATION_ERROR = 410;
	public static final int XMPP_ACCOUNT_CREATION_SUCCESSFULL = 411;
	
	public static class IntentKeys{
		
		public static class XmppFileTransferKey{
			public static final String XMPP_FILE_PROGRESS = "file_download_progress";
			public static final String XMPP_FILE_DOWNLOAD_STATUS = "file_download_status";
			public static final String XMPP_FILE_DOWNLOAD_COMPLETE = "file_download_complete_path";
			public static final String XMPP_FILE_SEND = "file_send";
		}
		
		public static class XmppMessageKey{
			public static final String XMPP_MESSAGE_JID = "jid";
			public static final String XMPP_MESSAGE_BODY = "body";
			public static final String XMPP_MESSAGE_DATE_TIME = "dateTime";
		}
		
		public static class XmppPresenceKey{			
			public static final String XMPP_PRESENCE = "presence";	
		}
		
		public static class XmppAccountCreationKey{
			public static final String XMPP_ACCOUNT_CREATION_CODE = "account_creation_code";			
		}
		
		public static class XmppSubsctiptionKey{
			public static final String XMPP_SUBSCRIPTION_FROM = "subscription_from";
			public static final String XMPP_SUBSCRIPTION_TYPE = "subscription_type";
		}
		
	}
	
	public static class Subscription{
		public static final String XMPP_PRESENCE_SUBSCRIBE = "subscribe";
		public static final String XMPP_PRESENCE_SUBSCRIBED = "subscribed";
		public static final String XMPP_PRESENCE_UNSUBSCRIBE = "unsubscribe";
		public static final String XMPP_PRESENCE_UNSUBSCRIBED = "unsubscribed";
	}

}
