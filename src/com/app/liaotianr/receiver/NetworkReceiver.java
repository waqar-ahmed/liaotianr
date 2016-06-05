package com.app.liaotianr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.ConnectionStatus;
import com.app.liaotianr.xmpp.XMPPManager;

public class NetworkReceiver extends BroadcastReceiver {
	
	public static final String TAG = "NetworkReceiver";
	XMPPManager manager;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	Utility.Log(TAG, "...In network Receiver...");
    	
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
       ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo info = cm.getActiveNetworkInfo();
       if (info != null) {
           if (info.isConnected()) {
        	   ConnectionStatus.NETWORK_AVAILABLE = true;
        	   Utility.Log(TAG, "Network Available");
        	   
        	   
        	   if(ConnectionStatus.CURRENT_STATUS != ConnectionStatus.CONNECTED){
        		   if(ConnectionStatus.CURRENT_STATUS != ConnectionStatus.CONNECTING){
        			   Utility.Log(TAG, "going to reconnect.........");
        			   //XMPPService.reConnect();
        		   }
        	   }
               //start service
               //Intent mIntent = new Intent(context, ConnectToServerService.class);
               //context.startService(intent);
           }
           else {
        	   ConnectionStatus.NETWORK_AVAILABLE = false;
        	   ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
        	   Utility.Log(TAG, "Network Unavailable");
               //stop service
               //Intent mIntent = new Intent(context, ConnectToServerService.class);
               //context.stopService(intent);
           }
       }
       else {
    	   ConnectionStatus.NETWORK_AVAILABLE = false;
    	   ConnectionStatus.CURRENT_STATUS = ConnectionStatus.DISCONNECTED;
    	   Utility.Log(TAG, "Network Unavailable");
       }
    }
}