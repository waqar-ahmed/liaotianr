package com.app.liaotianr;

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
	
	public XMPPFriend getFriend() {
		return mFriend;
	}

	public int getMessageCount() {
		return mMessageCount;
	}

	public void setFriend(XMPPFriend friend) {
		this.mFriend = friend;
	}

	public void setMessageCount(int messageCount) {
		this.mMessageCount = messageCount;
	}

}
