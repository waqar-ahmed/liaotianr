package com.app.liaotianr;

import java.util.ArrayList;
import java.util.List;

import com.app.liaotianr.xmpp.XMPPFriend;

public class MessageManageHelper {
	
	private static MessageManageHelper mMessageManager;
	
	public void increaseMessageCounter(String username, List<FriendMessage> list){
		for(FriendMessage f : list){
			if(f.getFriend().getUsername().equals(username)){
				f.mMessageCount += 1;
				return;
			}
		}
	}
	
	public void updateFriendPresence(String username, int presence, List<FriendMessage> list){
		for(FriendMessage f : list){
			if(f.getFriend().getUsername().equals(username)){
				f.getFriend().setPresenceStatus(presence);
				break;
			}
		}
	}
	
	public void setMessageCounterToZero(String username, List<FriendMessage> list){
		for(FriendMessage f : list){
			if(f.getFriend().getUsername().equals(username)){
				f.mMessageCount = 0;
				return;
			}
		}
	}
}
