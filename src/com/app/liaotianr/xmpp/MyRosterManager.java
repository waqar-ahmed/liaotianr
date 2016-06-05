package com.app.liaotianr.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.app.liaotianr.utilities.Utility;

public class MyRosterManager{

	static String TAG = "MyRosterManager";
	static MyRosterManager mRosterManager;
	static XMPPConnection mConnection;
	static Roster mRoster;
	MyRosterListener mRosterListener;
	private Collection<RosterEntry> mEntries;
	static Context mContext;
	
	private MyRosterManager(XMPPConnection connection, Context context){
		mConnection = connection;
		mContext = context;
		mRosterListener = new MyRosterListener(context);
		mRoster = mConnection.getRoster();
		mRoster.addRosterListener(mRosterListener);
	}
	
	public static MyRosterManager getInstance(XMPPConnection connection, Context context){
		if(mRosterManager == null){
			mRosterManager = new MyRosterManager(connection, context);
		}
		return mRosterManager;
	}
	
	public List<XMPPFriend> getBuddyList() throws InterruptedException{
		
		mRoster = mConnection.getRoster();
		Thread.sleep(1000);
		
		List<XMPPFriend> friends= new ArrayList<XMPPFriend>();
		mEntries = mRoster.getEntries();
		
		for(RosterEntry entry : mEntries){
			Utility.Log(TAG, "JID : "+entry.getUser()+" --- "+entry.getName()+" --- ");
			XMPPFriend f = new XMPPFriend(entry.getUser(), entry.getName(), getPresence(entry.getUser())); 
			friends.add(f);
		}
		
		Utility.Log(TAG, "friend list count : "+friends.size());
		
		return friends;
	}
	
	public int getPresence(String jid){
		Presence pres = mRoster.getPresence(jid);
		
		Log.d("tag",pres.toString());
		
		if(pres.toString().contains("unavailable")) 
			return PresenceStatus.OFFLINE;
		else 
			return PresenceStatus.ONLINE;
	}

	public void addFriend(String userID) {
			//userID = userID + "@sol";
	        if (mRoster != null) {
	            if (!mRoster.contains(userID)) {
	                try {
	                	mRoster.createEntry(userID, StringUtils.parseBareAddress(userID), new String[]{"friends"});
	                	//getBuddyList();
	            		
	                	Intent intent = new Intent();
	                    intent.setAction(XMPPIntentFilter.ACTION_XMPP_ROSTER_MODIFIED);
	                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	                    mContext.sendBroadcast(intent);
	                
	                } catch (Exception e) {
	                    System.err.println("Error in adding friend " + e.getMessage());
	                }
	            } else {
	                RosterEntry rosterEntry = mRoster.getEntry(userID);
	                RosterPacket.ItemType type = rosterEntry.getType();
	                switch (type) {
	                    case from:
	                        requestSubscription(userID, mConnection);
	                        break;
	                    case to:
	                        grantSubscription(userID, mConnection);
	                        break;
	                    case none:
	                        grantSubscription(userID, mConnection);
	                        requestSubscription(userID, mConnection);
	                        break;
	                    case both:
	                    default:
	                        break;
	                }
	            }
	        }
	    }
	
	public boolean rosterEntryExist(String jid){
		return mRoster.contains(jid) ?  true :  false;
	}
	
	
//	public void acceptRequest(String jid){
//		grantSubscription(jid, mConnection);
//		addFriend(jid);
//	}
//	
//	public void sendRequest(String jid){
//		jid = jid + "@" + ServerConfiguration.DOMAIN;
//		addFriend(jid);
//		//requestSubscription(jid, mConnection);
//	}
	
	   
	public static void grantSubscription(String jid, XMPPConnection connection) {
	        Presence presence = new Presence(Presence.Type.subscribed);
	        sendPresenceTo(jid, presence, connection);
	        Utility.Log(TAG, "subscribed...");
	}
	
	private static void requestSubscription(String jid, XMPPConnection connection) {
	        Presence presence = new Presence(Presence.Type.subscribe);
	        sendPresenceTo(jid, presence, connection);
	        Utility.Log(TAG, "subscribe...");
	}
	    
	public void sendCancelRequest(String jid){
		 Presence presence = new Presence(Presence.Type.unsubscribe);
	        sendPresenceTo(jid, presence, mConnection);

	}
	
	private static void sendPresenceTo(String to, Presence presence, XMPPConnection connection) {
	        presence.setTo(to);
	        try {
	            connection.sendPacket(presence);
	        } catch (Exception e) {
	            Log.e("Tag", "Failed to send presence.", e);
	        }
	}
	    
	String retrieveStatusMessage(String userID) {
	        String userStatus; // default return value

	        try {
	            userStatus = mConnection.getRoster().getPresence(userID).getStatus();
	        } catch (NullPointerException e) {
	            Log.e("Tag", "Invalid connection or user in retrieveStatus() - NPE", e);
	            userStatus = "";
	        }
	        // Server may set their status to null; we want empty string
	        if (userStatus == null) {
	            userStatus = "";
	        }

	        return userStatus;
	}
	    
	public boolean removeFriend(String userID) {
	        if (mConnection != null && mConnection.isConnected()) {
	            Roster roster = mConnection.getRoster();
	            if (roster.contains(userID)) {
	                try {
	                    roster.removeEntry(roster.getEntry(userID));
	                    
	                    Intent intent = new Intent();
	                    intent.setAction(XMPPIntentFilter.ACTION_XMPP_ROSTER_MODIFIED);
	                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	                    mContext.sendBroadcast(intent);
	                    
	                    return true;
	                } catch (Exception e) {
	                    System.err.println("Error in removing friend " + e.getMessage());
	                }
	            }
	        }
	        return false;
	}
	    
	public boolean renameFriend(String userID, String name) {
	        if (mConnection != null && mConnection.isConnected()) {
	            Roster roster = mConnection.getRoster();
	            if (roster.contains(userID)) {
	                RosterEntry entry  = roster.getEntry(userID);
	                try {
	                    entry.setName(name);
	                } catch (Exception e) {
	                    return false;
	                }
	                return true;
	            }
	        }
	       return false;
	}
	    
	public boolean createAccount(String username, String password, Map<String, String> values) throws XMPPException{
		
		AccountManager accountManager = new AccountManager(mConnection);
		
		//email, password, username, name
		
		if(!accountManager.supportsAccountCreation()) {
			Utility.Log(TAG, "Server does not support account creation");
			throw new XMPPException("Server does not support account creation.");
        }
		
		accountManager.createAccount(username, password, values);
		return true;
	}
	
	public Bitmap getDisplayPicture(String username) 
	{
		String jid = username + "@" + ServerConfiguration.DOMAIN;
		ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
                new VCardProvider());
		VCard card = new VCard();
		Bitmap img;
		try {
			card.load(mConnection,jid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		byte[] imgs = card.getAvatar();
		
		 if (imgs != null) {
             int len = imgs.length;
              img = BitmapFactory.decodeByteArray(imgs, 0, len);
              return img;
         }
		 else
		return null;
	}

	
	public void dispose(){
		if(mConnection !=null){
			if(mRoster !=null){
				mRoster.removeRosterListener(mRosterListener);
			}
		}
		mRosterManager = null;
		mConnection = null;
		mRoster = null;
		mRosterListener = null;
		mEntries = null;
	}
}

