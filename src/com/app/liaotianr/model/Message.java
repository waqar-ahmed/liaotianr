package com.app.liaotianr.model;

import java.util.Date;

public class Message {
	int messageId;
	String chatWith;
	String messageFrom;
	String messageTo;
	String messageBody;
	int alreadyRead;
	String timeStamp;
	
	public Message(int id, String chatWith, String from, String to, String body, int read, String timestamp){
		messageId = id;
		this.chatWith = chatWith;
		messageFrom = from;
		messageTo= to;
		messageBody = body;
		alreadyRead = read;
		timeStamp = timestamp;
	}
	
	public Message(String chatWith, String from, String to, String body, int read, String timestamp){
		this.chatWith = chatWith;
		messageFrom = from;
		messageTo= to;
		messageBody = body;
		alreadyRead = read;
		timeStamp = timestamp;
	}
	
	
	
	public int getMessageId() {
		return messageId;
	}

	public String getChatWith() {
		return chatWith;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public String getMessageTo() {
		return messageTo;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public int isAlreadyRead() {
		return alreadyRead;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public void setChatWith(String chatWith) {
		this.chatWith = chatWith;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public void setAlreadyRead(int alreadyRead) {
		this.alreadyRead = alreadyRead;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}
