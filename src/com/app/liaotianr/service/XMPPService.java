package com.app.liaotianr.service;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;

import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.app.liaotianr.ApplicationSettings;
import com.app.liaotianr.FriendMessage;
import com.app.liaotianr.MessageManageHelper;
import com.app.liaotianr.MyPreference;
import com.app.liaotianr.activity.ChatActivity;
import com.app.liaotianr.activity.FriendListActivity;
import com.app.liaotianr.database.FriendDbHandler;
import com.app.liaotianr.database.MessageDbHandler;
import com.app.liaotianr.model.Friend;
import com.app.liaotianr.model.Message;
import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.ConnectionStatus;
import com.app.liaotianr.xmpp.XMPPConstants;
import com.app.liaotianr.xmpp.XMPPFriend;
import com.app.liaotianr.xmpp.XMPPManager;


// Service is restarting when we remove app from recent list. The reason is http://stackoverflow.com/questions/18612880/android-service-crashes-after-app-is-swiped-out-of-the-recent-apps-list
public class XMPPService extends Service{
	
	private static XMPPManager manager;
	private static List<FriendMessage> list = new ArrayList<FriendMessage>();
	private static List<XMPPFriend> friends;
	static MessageManageHelper messageHelper;
	public static boolean isFriendListActivityVisible = false;
	public static boolean isChatActivityVisible = false;
	
	public static Handler presenceHandler;
	public static Handler messageHandler;
	public static Handler subscriptionHandler;
	
	static MessageDbHandler dbMessage;
	static FriendDbHandler dbFriend;
	
	static final String TAG = "XMPPService";
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		messageHelper = new MessageManageHelper();
		list = new ArrayList<FriendMessage>();
		
		dbMessage = new MessageDbHandler(getApplicationContext());
		dbFriend = new FriendDbHandler(getApplicationContext());
		
		/*
		 * Possible solution to remove warning from handler is :
		 * 
		 * Handler mIncomingHandler = new Handler(new Handler.Callback() {
			    @Override
			    public boolean handleMessage(Message msg) {
			    }
			});
		 * 
		 * */

		presenceHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(isFriendListActivityVisible){
					FriendListActivity.updateView.sendEmptyMessage(0);
				}
			};
		};
		
		
		messageHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				Bundle bundle = msg.getData();
				Intent i = new Intent();
				i.putExtras(bundle);
				String from = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_JID);
				String body = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_BODY);
				String dateTime = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_DATE_TIME);
				Message message = new Message(from.split("/")[0], from.split("/")[0], ApplicationSettings.MY_JID, body, 1, dateTime);
				dbMessage.addMessage(message);
				
				if(isFriendListActivityVisible){
					android.os.Message mMsg = android.os.Message.obtain();
					mMsg.copyFrom(msg);
					FriendListActivity.messageReceived.sendMessage(mMsg);
				}
				else if(isChatActivityVisible){
					dbMessage.updateAlreadyReadMessage(from.split("/")[0]);
					android.os.Message mMsg = android.os.Message.obtain();
					mMsg.copyFrom(msg);
					ChatActivity.messageHandler.sendMessage(mMsg);
				}
			};
		};

		subscriptionHandler = new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				Bundle bundle = msg.getData();
				Intent i = new Intent();
				i.putExtras(bundle);
				
				String from = i.getStringExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_FROM);
				String type = i.getStringExtra(XMPPConstants.IntentKeys.XmppSubsctiptionKey.XMPP_SUBSCRIPTION_TYPE);
				
				handleSubsciptionRequest(from, type);
			}
		};
	}
	
	public static List<FriendMessage> getFriendList(){
		try{
			friends = manager.getFriendsList();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utility.Log("Friends..", e.getMessage());
			return null;
		}
		try{
			return createListWithMessageCounter(friends);
		}
		catch(Exception e){
			Utility.Log(TAG, e.getMessage());
			Utility.Log(TAG, e.getCause().toString());
			return null;
		}
	}
	
	private static List<FriendMessage> createListWithMessageCounter(List<XMPPFriend> friends) {
		// TODO Auto-generated method stub
		list.clear();
		for(XMPPFriend friend : friends){
				dbFriend.addFriend(new Friend(friend.getUsername(), friend.getName(), ""));
				list.add(new FriendMessage(friend, dbMessage.countUnreadMessages(friend.getUsername())));
		}
		return list;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		manager = XMPPManager.getInstance(getApplicationContext());
		manager.connect();
		
		//when service gets restarted after clearing app from recent app list then we need to again login to server
		if(intent == null){
			while(ConnectionStatus.CURRENT_STATUS != ConnectionStatus.CONNECTED){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(MyPreference.getBoolean(this, MyPreference.Keys.IS_USER_REGISTERED)){				
				login(MyPreference.getString(getApplicationContext(), MyPreference.Keys.USERNAME), MyPreference.getString(getApplicationContext(), MyPreference.Keys.PASSWORD));
			}
		}
		return START_STICKY;
		//return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.closeConnection();
		Utility.Log(TAG, "Service onDestroy called");
	}
	
	public static void addFriend(String jid){
		try {
			manager.addFriend(jid);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleSubsciptionRequest(String from, String type) {
		if(type.equals(XMPPConstants.Subscription.XMPP_PRESENCE_SUBSCRIBE)){
			Utility.Log(TAG, "Receive Subscription request from : "+from);
			
			//if roster contain entry(in case when you sent request to someone and after accepting your request he also want to subscribe you) then just call addfriend else show dialog because its a new request
			if(manager.friendExist(from)){
				try {
					manager.addFriend(from);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				if(isFriendListActivityVisible){
					android.os.Message msg = new android.os.Message();
					
					Intent intent = new Intent();
					intent.putExtra("from", from);
					intent.putExtra("title", from.split("@")[0] + " wants to add you as friend");
					intent.putExtra("toast", "Friend added!");
					intent.putExtra("isFriendRequestReceived", true);
				
					msg.setData(intent.getExtras());
					
					FriendListActivity.showRequestDialog.sendMessage(msg);
				}
					//new FriendListActivity().showReceiveFriendRequestDialog(from, from.split("@")[0] + " wants to add you as friend", "Friend added!", true);
					//FriendListActivity.showRequestDialog.sendMessage(msg);
				else{
					//show pending intent 
				}
			}
		}
		else if(type.equals(XMPPConstants.Subscription.XMPP_PRESENCE_SUBSCRIBED)){
			Utility.Log(TAG, "You are now friend with : "+from);
			Toast.makeText(getApplicationContext(), "You are now friend with : "+from, Toast.LENGTH_SHORT).show();
			
			try {
				//after sending subscribe to someone and when he accepts request he will send me subscribed request, so just add him
				manager.addFriend(from);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type.equals(XMPPConstants.Subscription.XMPP_PRESENCE_UNSUBSCRIBE)){
			Utility.Log(TAG, "Unsubscribe request from : "+from);
			manager.deleteFriend(from);
		}
		else if(type.equals(XMPPConstants.Subscription.XMPP_PRESENCE_UNSUBSCRIBED)){
			Utility.Log(TAG, "Friend deleted : "+from);
		}
	}



	public static boolean login(String username, String password){
		try {
			if(!manager.isUserLogin())
				manager.loginMe(username, password);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utility.Log("Friends..", e.getMessage());
			return false;
		}
	}
	
	public static void sendMessage(String to, String message) throws NetworkErrorException, XMPPException{
		manager.sendMessage(to, message);
	} 
	
	public static void reConnect(){
		Utility.Log("service", "reconnecting to server...");
		manager.reconnect();
	}
	
	public static boolean isLogin(){
		return manager.isUserLogin();
	}
}
