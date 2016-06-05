package com.app.liaotianr.xmpp;

import android.graphics.Bitmap;


public class XMPPFriend {

	String mUsername;
    String mName;
    Bitmap mUserImage;
    int mPresenceStatus;
    
    static String TAG = "XMPPFriend";
    
    public XMPPFriend(){}
    
    public XMPPFriend(String username, String name, int presenceStatus) {
        mName = name;
        mUsername = username;
        mUserImage = null;
        mPresenceStatus = presenceStatus;
    }
    
    
    public XMPPFriend(String username, String name, int presenceStatus, Bitmap userImage) {
        mName = name;
        mUsername = username;
        mUserImage = userImage;
        mPresenceStatus = presenceStatus;
    }
   
    //getters
    
	public String getUsername() {
		return mUsername;
	}

	public String getName() {
		return mName;
	}
	
	public Bitmap getImage(){
		return mUserImage;
	}
	
	//setters
	public void setUsername(String username) {
		mUsername = username;
	}

	public void setName(String name) {
		mName = name;
	}
	
	public void setImage(Bitmap image){
		mUserImage = image;
	}
	
	public int getPresenceStatus(){
		return mPresenceStatus;
	}
	
	public void setPresenceStatus(int presenceStatus){
		mPresenceStatus = presenceStatus;
	}
}
