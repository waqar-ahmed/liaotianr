package com.app.liaotianr.model;


import com.app.liaotianr.xmpp.XMPPFriend;

public class FriendMessage {
	XMPPFriend mFriend;
	int mMessageCount;

	public FriendMessage(XMPPFriend friend){
		mFriend = friend;
		mMessageCount = 0;
	}
	
	public FriendMessage(XMPPFriend friend, int messageCount){
		mFriend = friend;
		mMessageCount = messageCount;
	}
	
	public void setFriend(XMPPFriend friend){
		mFriend = friend;
	}
	
	public XMPPFriend getFriend(){
		return mFriend;
	}
	
	public void setMessageCount(int count){
		mMessageCount = count;
	}
	
	public int getMessagCount(){
		return mMessageCount;
	}
}
