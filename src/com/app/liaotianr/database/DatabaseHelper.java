package com.app.liaotianr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	    private static final String DATABASE_NAME = "XinXin.db";
	    private static int DATABASE_VERSION = 1;
		Context context;
		private static final String TAG = "Database_Helper";
		
		
	    public DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
	    }
	    
	    @Override
		public void onCreate(SQLiteDatabase db) {
	    	db.execSQL(DatabaseTable.TABLE_QUERY.CREATE_FRIENDS_TABLE);
	    	db.execSQL(DatabaseTable.TABLE_QUERY.CREATE_MESSAGES_TABLE);
	    	db.execSQL(DatabaseTable.TABLE_QUERY.CREATE_FRIEND_REQUEST_SENT_TABLE);
	    	db.execSQL(DatabaseTable.TABLE_QUERY.CREATE_FRIEND_REQUEST_RECEIVED_TABLE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DatabaseTable.TABLE_FRIENDS);
			db.execSQL("DROP TABLE IF EXISTS " + DatabaseTable.TABLE_MESSAGES);
			db.execSQL("DROP TABLE IF EXISTS " + DatabaseTable.TABLE_FRIEND_REQUEST_SENT);
			db.execSQL("DROP TABLE IF EXISTS " + DatabaseTable.TABLE_FRIEND_REQUEST_RECEIVED);
		    onCreate(db);
		}
}

