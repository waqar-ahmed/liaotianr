package com.app.liaotianr.xmpp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class XMPPUtils {
	
	//check internet connection
		public static boolean IsInternetAvailable(Context context)
		{
			ConnectivityManager cm =
			        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			 
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null &&
			                      activeNetwork.isConnectedOrConnecting();
			return isConnected;
		}
		

		
		public static String getCurrentGMTDate(){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
	        return format.format(new Date()).toString();
		}
		
		public static String getUsernameByJid(String jid){
			return jid.split("@")[0];
		}
}