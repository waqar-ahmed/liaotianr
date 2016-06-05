package com.app.liaotianr.xmpp;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;

public class XMPPChatManager{
	
	static HashMap<String, Chat> chatList;
	static Chat mChat ;
	static XMPPConnection mConnection;
	static XMPPChatManager mChatManager;
	static MyMessageListener mMessageListener;
	static MyChatManagerListener mChatManagerListener;
	
	private XMPPChatManager(XMPPConnection connection, Context context){
		mConnection = connection;
		chatList = new HashMap<String, Chat>();
		mMessageListener = new MyMessageListener(context);
		setChatManagerListener();
	}
	
	public static XMPPChatManager getInstance(XMPPConnection connection, Context context){
		if(mChatManager == null){
			mChatManager = new XMPPChatManager(connection, context);
		}
		return mChatManager;
	}
	
	static boolean sendMessage(String to, String message) throws XMPPException{
		mChat = getOrCreateChat(to);
		try {
			mChat.sendMessage(message);
			return true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	private static Chat getOrCreateChat(String jid){
		if(chatList.containsKey(jid)){
			mChat = chatList.get(jid);			
		}
		else{
			mChat = mConnection.getChatManager().createChat(jid, mMessageListener);	
			chatList.put(jid, mChat);
		}
		return mChat;
	}
	
	private void setChatManagerListener(){
		mChatManagerListener = new MyChatManagerListener();
		mConnection.getChatManager().addChatListener(mChatManagerListener);
	}
	
	public class MyChatManagerListener implements ChatManagerListener{

		@Override
		public void chatCreated(Chat arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
			if(!arg1)
				arg0.addMessageListener(mMessageListener);
		}
		
	}
	
	public void dispose(){
		
		if(mConnection !=null){
			mConnection.getChatManager().removeChatListener(mChatManagerListener);
		}
		
		chatList = null;
		mChat = null;
		mConnection = null;
		mChatManager = null;
		mMessageListener = null;
		mChatManagerListener = null;
	}
	
}
