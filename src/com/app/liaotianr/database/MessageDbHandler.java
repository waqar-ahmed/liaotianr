package com.app.liaotianr.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.liaotianr.model.Message;

public class MessageDbHandler {
	
	private static final String TAG = "MessageDbHandler";
	Context context;
	DatabaseHelper dbHelper;
	SQLiteDatabase database;
	Cursor cursor;
	static int LIMIT = 15;
	
	public MessageDbHandler(Context context){
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}
	
	public ArrayList<Message> getAllMessages(String username, int page){
		ArrayList<Message> messages = new ArrayList<Message>();
		database = dbHelper.getReadableDatabase();
		cursor = database.rawQuery(
				"Select * from " + DatabaseTable.TABLE_MESSAGES +
				" where " + DatabaseTable.TABLE_MESSAGES_COLUMN.CHAT_WITH + " = '"+username+"'" +
//				" order by " + 	DatabaseTable.TABLE_MESSAGES_COLUMN.MESSAGE_ID + ", LIMIT " + LIMIT * page	+
				";", null);
		
		if (cursor.moveToFirst()) {
            do {
            	messages.add(new Message(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6)));    
            } while (cursor.moveToNext());
        }
		cursor.close();
		return messages;
	}
	
    public void addMessage(Message message)
    {
    	database = dbHelper.getWritableDatabase();
    	
    	ContentValues value = new ContentValues();
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.CHAT_WITH, message.getChatWith());
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.MESSAGE_FROM , message.getMessageFrom() );
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.MESSAGE_TO, message.getMessageTo());
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.MESSAGE_BODY, message.getMessageBody());
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.TIMESTAMP, message.getTimeStamp());
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.ALREADY_READ, message.isAlreadyRead());
    	
    	database.insert(DatabaseTable.TABLE_MESSAGES, null, value);
    	database.close();
    }
    
    public int countUnreadMessages(String username)
    {
    	database=dbHelper.getReadableDatabase();
    	cursor = database.rawQuery(
    			"Select * from " + DatabaseTable.TABLE_MESSAGES + 
    			" where " + DatabaseTable.TABLE_MESSAGES_COLUMN.MESSAGE_FROM + "='"+username+
    			"' and " + DatabaseTable.TABLE_MESSAGES_COLUMN.ALREADY_READ + " = 1"+";", null);
    	
    	//select * from (arbitrary UserSQL) limit PageSize, CurrentOffset
    	
    	if(cursor == null)
    		{
    		Log.d("asd","returning zero");
    		return 0;
    		}
    	
    	int count = cursor.getCount();
    	cursor.close();
    	Log.d("asd","returning " + count);
    	return count;
    }
	
    public void updateAlreadyReadMessage(String username){
    	database = dbHelper.getWritableDatabase();
//    	database.execSQL(
//    			"Update " + DatabaseTable.TABLE_MESSAGES 
//    			+ " set " + DatabaseTable.TABLE_MESSAGES_COLUMN.ALREADY_READ + " = 0 "
//    			+ "where " + DatabaseTable.TABLE_MESSAGES_COLUMN.MESSAGE_FROM + " = '" + username 
//    			+ "';" );
    	ContentValues value = new ContentValues();
    	value.put(DatabaseTable.TABLE_MESSAGES_COLUMN.ALREADY_READ, 0);
    	database.update(DatabaseTable.TABLE_MESSAGES, value, DatabaseTable.TABLE_MESSAGES_COLUMN.CHAT_WITH + "= ?", new String[] {username});
    	database.close();
    }

}
