package com.app.liaotianr.xmpp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.app.liaotianr.ApplicationSettings;
import com.app.liaotianr.utilities.Utility;

public class XMPPManager{
	
	public static final String TAG = "XManager";
	private static Context mContext ;
	
	static SmackAndroid smackAndroid;
	static ConnectionConfiguration mConfiguration = null;
	static XMPPConnection mConnection = null;
	//static Roster mRoster = null;
	static List<XMPPFriend> mFriends = new ArrayList<XMPPFriend>();
	static XMPPManager mManager = null;
	static Collection<RosterEntry> mEntries;
	
	static MyRosterManager mRosterManager;
	static XMPPChatManager mChatManager;
	static XMPPFileManager mFileManager;
	
	private static ChatMessageListener mChatListener;
	private static PresenceListener mPresenceListener;
	private static MyPacketListener mPacketListener;
	private static MyConnectionListener mConnectionListener;
	
	private static boolean isLogin = false;

	private XMPPManager(Context context){
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		smackAndroid = SmackAndroid.init(context);
		mConfiguration = new ConnectionConfiguration(ServerConfiguration.SERVER_ADDRESS, ServerConfiguration.SERVER_PORT);
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
		
		mConnectionListener = new MyConnectionListener();
		mChatListener = new ChatMessageListener();
		mPresenceListener = new PresenceListener(mContext);
		mPacketListener = new MyPacketListener();
		//mRosterListener = new MyRosterListener(mContext);
		
		keyStoreSolve();
		
		mConnection = new XMPPConnection(mConfiguration);
	}

	private void keyStoreSolve() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			mConfiguration.setTruststoreType("AndroidCAStore");
			mConfiguration.setTruststorePassword(null);
			mConfiguration.setTruststorePath(null);
		} else {
			mConfiguration.setTruststoreType("BKS");
		    String path = System.getProperty("javax.net.ssl.trustStore");
		    if (path == null)
		        path = System.getProperty("java.home") + File.separator + "etc"
		            + File.separator + "security" + File.separator
		            + "cacerts.bks";
		    mConfiguration.setTruststorePath(path);
		}
	}

	private static void initializeListeners() {
		//mConnection.addPacketListener(mPacketListener, null);
        
		//mConnection.addPacketListener(mChatListener, new PacketTypeFilter(Message.class));
        mConnection.addPacketListener(mPresenceListener,  new PacketTypeFilter(Presence.class));
        
		//listener for incoming messages
//		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
//		mConnection.addPacketListener(mChatListener, filter);
		
        mChatManager = XMPPChatManager.getInstance(mConnection, mContext);
        mRosterManager = MyRosterManager.getInstance(mConnection, mContext);
        mFileManager = XMPPFileManager.getInstance(mConnection, mContext, ApplicationSettings.SD_CARD_ROOT_FOLDER, ApplicationSettings.RECEIVED_FILES_FOLDER);
	}
	
	public static XMPPManager getInstance(Context context){
		if(mManager == null){
			mManager = new XMPPManager(context);
		}
		return mManager;
	}
	
	public void connect(){
		
		if(XMPPUtils.IsInternetAvailable(mContext)){
			ConnectionStatus.NETWORK_AVAILABLE = true;
		}
		
		AsynConnect conn = new AsynConnect(true);
		conn.execute();
	}
	
	public boolean loginMe(String username, String password) throws XMPPException{
		return performLogin(username, password);
	}
	
	private boolean performLogin(String username, String password) throws XMPPException{
		Utility.Log(TAG, "logging in...");
		
//		if(isUserLogin()) {
//			Utility.Log(TAG, "Already login to server");
//			return true;
//		}
		
		try {
			if(ConnectionStatus.NETWORK_AVAILABLE && ConnectionStatus.CURRENT_STATUS == ConnectionStatus.CONNECTED){
				mConnection.login(username, password);
				mConnection.sendPacket(new Presence(Presence.Type.available));
				
				isLogin = true;
				Utility.Log(TAG, "loggin successfully");
				try {
					XMPPOfflineMessage.handleOfflineMessages(mConnection);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isLogin = false;
			throw e;
		}
		
		return isLogin;
	}
	
	private static boolean connectToServer(){
		if(mConnection == null){Utility.Log(TAG, "Connection object null......"); return false;}
		if(!ConnectionStatus.NETWORK_AVAILABLE) {Utility.Log(TAG, "Network Unavailable"); return false; }
		if(mConnection.isConnected()) {Utility.Log(TAG, "Already connected to server"); return true; }
		
		
		try {
			
			Intent intent = new Intent();
	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_CONNECTING);
	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	        mContext.sendBroadcast(intent);
			
			ConnectionStatus.CURRENT_STATUS = ConnectionStatus.CONNECTING;
			Utility.Log(TAG, "Connecting...");
			mConnection.connect();
			Utility.Log(TAG,"connected....");
			ConnectionStatus.CURRENT_STATUS = ConnectionStatus.CONNECTED;
			
			
			intent = new Intent();
	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_CONNECTED);
	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	        mContext.sendBroadcast(intent);
			
			
			
			mConnection.addConnectionListener(mConnectionListener);
			
			Utility.Log(TAG, "Connected");
			
			initializeListeners();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Utility.Log(TAG, "Exception occured");
			ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
			e.printStackTrace();
		}
		
		if(ConnectionStatus.CURRENT_STATUS == ConnectionStatus.CONNECTED){
			return true;
		}
		
		return false;
	}
	
	public void createAccount(String username, String password, Map<String, String> values){
		
		Intent intent = new Intent();
        intent.setAction(XMPPIntentFilter.ACTION_XMPP_ACCOUNT_CREATION);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		
		try {
			mRosterManager.createAccount(username, password, values);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppAccountCreationKey.XMPP_ACCOUNT_CREATION_CODE, XMPPConstants.XMPP_ACCOUNT_CREATION_SUCCESSFULL);
	        mContext.sendBroadcast(intent);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if(e.getXMPPError().getCode() == 409){   //if conflict
				//Utility.Log(TAG, "Sorry!, User already exist with this username");
		        intent.putExtra(XMPPConstants.IntentKeys.XmppAccountCreationKey.XMPP_ACCOUNT_CREATION_CODE, XMPPConstants.XMPP_ACCOUNT_CREATION_CONFLICT);
			}
			else{
				//Utility.Log(TAG, "Sorry!, Error occured during creation of account");
		        intent.putExtra(XMPPConstants.IntentKeys.XmppAccountCreationKey.XMPP_ACCOUNT_CREATION_CODE, XMPPConstants.XMPP_ACCOUNT_CREATION_ERROR);
			}

	        mContext.sendBroadcast(intent);
		}
	}
	
	public void acceptRequest(String jid) throws XMPPException{
		if(ConnectionStatus.CURRENT_STATUS == ConnectionStatus.DISCONNECTED){
			Utility.Log(TAG, "no connection to server");
			throw new XMPPException("Not connected to server.");
		}
		
		if(!mConnection.isAuthenticated() || !isLogin){
			Utility.Log(TAG, "not login to server");
			throw new XMPPException("Not login to server.");
		}
		
		//mRosterManager.acceptRequest(jid);
	}
	
	
	
	public void addFriend(String jid) throws XMPPException{
		if(ConnectionStatus.CURRENT_STATUS == ConnectionStatus.DISCONNECTED){
			Utility.Log(TAG, "no connection to server");
			throw new XMPPException("Not connected to server.");
		}
		
		if(!mConnection.isAuthenticated() || !isLogin){
			Utility.Log(TAG, "not login to server");
			throw new XMPPException("Not login to server.");
		}
		
		mRosterManager.addFriend(jid);
		
		//mRosterManager.sendRequest(jid);
	}
	
	public List<XMPPFriend> getFriendsList() throws XMPPException, InterruptedException{
	
		Utility.Log(TAG, "retreiving friend list...");
		
		if(ConnectionStatus.CURRENT_STATUS == ConnectionStatus.DISCONNECTED){
			Utility.Log(TAG, "no connection to server");
			throw new XMPPException("Not connected to server.");
		}
		
		if(!mConnection.isAuthenticated()){
			Utility.Log(TAG, "not login to server");
				throw new XMPPException("Not login to server.");
		}
		
		return mRosterManager.getBuddyList();
	}
	
	public boolean friendExist(String jid){
		return mRosterManager.rosterEntryExist(jid);
	}
	
	public void sendCancelRequest(String jid){
		mRosterManager.sendCancelRequest(jid);
	}
	
	public boolean deleteFriend(String jid){
		return mRosterManager.removeFriend(jid);
	}
	
	public int getPresence(String username){
		String jid = username + "@" + ServerConfiguration.DOMAIN;
		return mRosterManager.getPresence(jid);
	}
	
	public boolean isUserLogin(){
		return mConnection.isAuthenticated();
	}
	
	public void sendFile(String path, String to){
		XMPPFileManager.sendFile(path, to);
	}
	
	public boolean sendMessage(String to, String message) throws XMPPException, NetworkErrorException{
		if(!(ConnectionStatus.CURRENT_STATUS == ConnectionStatus.CONNECTED) || !isUserLogin()){
			//check here if user is registered, if yes then connect again to server
			if(!XMPPUtils.IsInternetAvailable(mContext)){
				Utility.Log(TAG, "Internet not available");
				throw new NetworkErrorException("Not connected to internet.");
			}
		}
		return XMPPChatManager.sendMessage(to, message);
	} 
	
	public void closeConnection(){
		Utility.Log(TAG, "disconnecting...");
		
		Intent intent = new Intent();
        intent.setAction(XMPPIntentFilter.ACTION_XMPP_DISCONNECTING);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mContext.sendBroadcast(intent);
		
		smackAndroid.onDestroy();
		new Thread(){
			public void run(){
				cleanConnection();
			}
		}.start();
	}
	
	private void cleanConnection(){
		if(mRosterManager !=null)
			mRosterManager.dispose();
		
		if(mChatManager !=null)
			mChatManager.dispose();
		
		if(mConnection != null){
			if(mPacketListener !=null){
				mConnection.removePacketListener(mPacketListener);
				mPacketListener = null;
			}
			if(mChatListener !=null){
				mConnection.removePacketListener(mChatListener);
				mChatListener = null;
			}
			if(mPresenceListener !=null){
				mConnection.removePacketListener(mPresenceListener);
				mPresenceListener = null;
			}
			if(mConnectionListener !=null){
				mConnection.removeConnectionListener(mConnectionListener);
				mConnectionListener = null;
			}
//			if(mRoster != null){
//				mRoster.removeRosterListener(mRosterListener);
//				mRoster = null;
//			}
			if(mConnection.isConnected()){
				ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTING;
				mConnection.disconnect();
				ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
				isLogin = false;
			}
			
			mConnection = null;
			mManager = null;
			Utility.Log(TAG, "disconnected successfully");
			
			Intent intent = new Intent();
	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_DISCONNECTED);
	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	        mContext.sendBroadcast(intent);
		}
	}
	
	public void reconnect(){
		closeConnection();
		connect();
	}

	//Asyntask implementation to connect to server
	public class AsynConnect extends AsyncTask<Void, Void, Void>{

		boolean showProgressDialog = true;
		ProgressDialog dialog ;
		IConnectionEstablished delegate;
		
		public AsynConnect(boolean showDialog){
			showProgressDialog = showDialog;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
//			if(showProgressDialog){
//				dialog = new ProgressDialog(mContext);
//				this.dialog.setMessage("Progress start");
//				this.dialog.setCanceledOnTouchOutside(false);
//		        this.dialog.show();
//			}
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			while(ConnectionStatus.CURRENT_STATUS != ConnectionStatus.DISCONNECTED){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try{
				if(mConnection == null){
					init(mContext);
				}
				XMPPManager.connectToServer();
			}
			catch(Exception e){
				Utility.Log(TAG,"error: "+e);
//				if(showProgressDialog){
//					 if (dialog.isShowing()) {
//				            dialog.dismiss();
//				        }
//				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);


		}
	}

}
