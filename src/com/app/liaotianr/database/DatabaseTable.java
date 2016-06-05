package com.app.liaotianr.database;

//Store information about database tables
public class DatabaseTable {
	
	//Table names that in database
	public static final String TABLE_FRIENDS = "Friends";
	public static final String TABLE_MESSAGES = "Messages";
	public static final String TABLE_FRIEND_REQUEST_SENT = "Friend_Requests_Sent";
	public static final String TABLE_FRIEND_REQUEST_RECEIVED = "Friend_Requests_Received";
	
	//Friend Table columns name
	public static class TABLE_FRIENDS_COLUMN{
		public static final String FRIEND_ID = "_id";
		public static final String USERNAME = "Username";
		public static final String NAME = "Name";
		public static final String SUBSCRIPTION_TYPE = "Subscription_Type";
		public static final String IMAGE = "Image";
	}
	
	public static class TABLE_FRIEND_REQUEST_SENT_COLUMN{
		public static final String FRIEND_REQUEST_SENT_ID = "_id";
		public static final String USERNAME = "Username";
		public static final String SUBSCRIPTION_TYPE = "Subscription_Type";
		public static final String TIME_STAMP = "TimeStamp";
	}
	
	public static class TABLE_FRIEND_REQUEST_RECEIVED_COLUMN{
		public static final String FRIEND_REQUEST_RECEIVED_ID = "_id";
		public static final String USERNAME = "Username";
		public static final String SUBSCRIPTION_TYPE = "Subscription_Type";
		public static final String TIME_STAMP = "TimeStamp";
	}
	
	//Message Table columns name
	public static class TABLE_MESSAGES_COLUMN{
		public static final String MESSAGE_ID = "_id";
		public static final String CHAT_WITH = "ChatWith";
		public static final String MESSAGE_FROM = "MessageFrom";
		public static final String MESSAGE_TO = "MessageTo";
		public static final String MESSAGE_BODY = "MessageBody";
		public static final String ALREADY_READ = "AlreadyRead";
		public static final String TIMESTAMP = "TimeStamp";
	}
	
	public static class TABLE_QUERY{
		
		//create friends table query
		public static final String CREATE_FRIENDS_TABLE = "CREATE TABLE "
															+ TABLE_FRIENDS + " (" 
															+ TABLE_FRIENDS_COLUMN.FRIEND_ID + COLUMN_DATA_TYPE.INTEGER_PRIMARY_KEY_AUTO_INCREMENT + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_FRIENDS_COLUMN.USERNAME + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.UNIQUE + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_FRIENDS_COLUMN.NAME + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_FRIENDS_COLUMN.SUBSCRIPTION_TYPE + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_FRIENDS_COLUMN.IMAGE + COLUMN_DATA_TYPE.TEXT
															+ ");";
	
		//create message table query
		public static final String CREATE_MESSAGES_TABLE = "CREATE TABLE "
															+ TABLE_MESSAGES + " ("
															+ TABLE_MESSAGES_COLUMN.MESSAGE_ID + COLUMN_DATA_TYPE.INTEGER_PRIMARY_KEY_AUTO_INCREMENT + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_MESSAGES_COLUMN.CHAT_WITH + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_MESSAGES_COLUMN.MESSAGE_FROM + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_MESSAGES_COLUMN.MESSAGE_TO + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_MESSAGES_COLUMN.MESSAGE_BODY + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_MESSAGES_COLUMN.ALREADY_READ + COLUMN_DATA_TYPE.INTEGER + COLUMN_DATA_TYPE.COMMA_SEP
															+ TABLE_MESSAGES_COLUMN.TIMESTAMP + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL
															+ ");"; 
		
		public static final String CREATE_FRIEND_REQUEST_SENT_TABLE = "CREATE TABLE "
																		+ TABLE_FRIEND_REQUEST_SENT + " ("
																		+ TABLE_FRIEND_REQUEST_SENT_COLUMN.FRIEND_REQUEST_SENT_ID + COLUMN_DATA_TYPE.INTEGER_PRIMARY_KEY_AUTO_INCREMENT + COLUMN_DATA_TYPE.COMMA_SEP
																		+ TABLE_FRIEND_REQUEST_SENT_COLUMN.USERNAME + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
																		+ TABLE_FRIEND_REQUEST_SENT_COLUMN.SUBSCRIPTION_TYPE + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.COMMA_SEP
																		+ TABLE_FRIEND_REQUEST_SENT_COLUMN.TIME_STAMP + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL
																		+ ");";
		
		public static final String CREATE_FRIEND_REQUEST_RECEIVED_TABLE = "CREATE TABLE "
																		+ TABLE_FRIEND_REQUEST_RECEIVED + " ("
																		+ TABLE_FRIEND_REQUEST_RECEIVED_COLUMN.FRIEND_REQUEST_RECEIVED_ID + COLUMN_DATA_TYPE.INTEGER_PRIMARY_KEY_AUTO_INCREMENT + COLUMN_DATA_TYPE.COMMA_SEP
																		+ TABLE_FRIEND_REQUEST_RECEIVED_COLUMN.USERNAME + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL + COLUMN_DATA_TYPE.COMMA_SEP
																		+ TABLE_FRIEND_REQUEST_RECEIVED_COLUMN.SUBSCRIPTION_TYPE + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.COMMA_SEP
																		+ TABLE_FRIEND_REQUEST_RECEIVED_COLUMN.TIME_STAMP + COLUMN_DATA_TYPE.TEXT + COLUMN_DATA_TYPE.NOT_NULL
																		+ ");";
		
	}
	
	public static class COLUMN_DATA_TYPE{
		public static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";
		public static final String INTEGER_PRIMARY_KEY_AUTO_INCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
		public static final String TEXT = " TEXT";
		public static final String INTEGER = " INTEGER";
		public static final String NOT_NULL = " NOT NULL";
		public static final String UNIQUE = " UNIQUE";
		public static final String COMMA_SEP = ",";
		
	}
}
