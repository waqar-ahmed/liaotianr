package com.app.liaotianr.xmpp;

import org.jivesoftware.smack.ConnectionListener;

import com.app.liaotianr.utilities.Utility;

public class MyConnectionListener implements ConnectionListener{

	String TAG = "MyConnectionListener";
	
	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
		Utility.Log(TAG, "connectionClosed");
	}

	@Override
	public void connectionClosedOnError(Exception arg0) {
		// TODO Auto-generated method stub
		ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
		Utility.Log(TAG, "connectionClosedOnError");
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		ConnectionStatus.CURRENT_STATUS = ConnectionStatus.CONNECTING;
		Utility.Log(TAG, "reconnectingIn");
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
		Utility.Log(TAG, "reconnectionFailed");
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		ConnectionStatus.CURRENT_STATUS = ConnectionStatus.CONNECTED;
		Utility.Log(TAG, "reconnectionSuccessful");
	}
	
}