package com.app.liaotianr.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class Utility {
	
	//check internet connection
		public boolean IsInternetAvailable(Context context)
		{
			ConnectivityManager cm =
			        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			 
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null &&
			                      activeNetwork.isConnectedOrConnecting();
			return isConnected;
		}
		
		public static void Log(String tag, String message){
			Log.d(tag,message);
			System.out.println(tag+" :: "+message);
		}
		
		public static String convertDateTime(String date){

			SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date parsed = null;
			try {
				parsed = sourceFormat.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // => Date is in UTC now

			TimeZone tz = Calendar.getInstance().getTimeZone();
			SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			destFormat.setTimeZone(tz);

			String result = destFormat.format(parsed);
			return result;
			
		}
 
		
		public static Date parseDate(String time){
			String inputPattern = "EEE MMM dd HH:mm:ss z yyyy";
			String outputPattern = "MMM d, yyyy HH:mm:ss";
			
			SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
			SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
			
			Date date = null;
		    String str = null;

		    try {
		        date = inputFormat.parse(time);
		        str = outputFormat.format(date);

		        Log.i("mini", "Converted Date Today:" + str);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    return date;
		}
    
}