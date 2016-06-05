package com.app.liaotianr.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.liaotianr.database.DatabaseTable.TABLE_FRIEND_REQUEST_SENT_COLUMN;
import com.app.liaotianr.model.Friend;
import com.app.liaotianr.model.Message;
import com.app.liaotianr.xmpp.XMPPUtils;

public class FriendDbHandler {
	private static final String TAG = "FriendDbHandler";
	Context context;
	DatabaseHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;
	
	public FriendDbHandler(Context context){
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}
	
	public ArrayList<Friend> getAllFriends(){
		ArrayList<Friend> friends = new ArrayList<Friend>();
		database = dbHelper.getReadableDatabase();
		cursor = database.rawQuery("Select * from " + DatabaseTable.TABLE_FRIENDS + ";", null);
		if (cursor.moveToFirst()) {
            do {
            	friends.add(new Friend(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));    
            } while (cursor.moveToNext());
        }
		cursor.close();
		return friends;
	}
	
    public void addFriend(Friend friend)
    {
    	database = dbHelper.getWritableDatabase();
    	
    	ContentValues value = new ContentValues();
    	value.put(DatabaseTable.TABLE_FRIENDS_COLUMN.USERNAME, friend.getUsername());
    	value.put(DatabaseTable.TABLE_FRIENDS_COLUMN.NAME, friend.getName());
    	value.put(DatabaseTable.TABLE_FRIENDS_COLUMN.IMAGE, friend.getImage());
    	
    	database.insertWithOnConflict(DatabaseTable.TABLE_FRIENDS, null, value, SQLiteDatabase.CONFLICT_IGNORE);
    	database.close();
    }
    
    public void addFriendRequestSent(String username, String subscriptionType){
    	addFriendRequest(username, subscriptionType, DatabaseTable.TABLE_FRIEND_REQUEST_SENT);
    }
    
    public void addFriendRequestReceived(String username, String subscriptionType){
    	addFriendRequest(username, subscriptionType, DatabaseTable.TABLE_FRIEND_REQUEST_RECEIVED);
    }

	private void addFriendRequest(String username, String subscriptionType, String tableName) {
		database = dbHelper.getWritableDatabase();
    	
    	ContentValues value = new ContentValues();
    	value.put(TABLE_FRIEND_REQUEST_SENT_COLUMN.USERNAME, username);
    	value.put(TABLE_FRIEND_REQUEST_SENT_COLUMN.SUBSCRIPTION_TYPE, subscriptionType);
    	value.put(TABLE_FRIEND_REQUEST_SENT_COLUMN.TIME_STAMP, XMPPUtils.getCurrentGMTDate());
    	
    	database.insert(tableName, null, value);
    	database.close();
	}
    
    public boolean checkSentRequestExist(String username){
    	return checkRequestExist(username, DatabaseTable.TABLE_FRIEND_REQUEST_SENT);
    }
    
    public boolean checkReceivedRequestExist(String username){
    	return checkRequestExist(username, DatabaseTable.TABLE_FRIEND_REQUEST_RECEIVED);
    }

	private boolean checkRequestExist(String username, String tableName) {
		database=dbHelper.getReadableDatabase();
    	cursor = database.rawQuery(
    			"Select * from " + tableName
    			+ " where " + DatabaseTable.TABLE_FRIEND_REQUEST_SENT_COLUMN.USERNAME + "='"+username+"';", null);
    	
    	if(cursor == null)
		{
		Log.d("asd","returning zero");
		return false;
		}
		cursor.close();
		return true;
	}
}
