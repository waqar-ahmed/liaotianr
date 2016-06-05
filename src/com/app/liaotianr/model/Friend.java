package com.app.liaotianr.model;

public class Friend {

	int userId;
	String username;
	String name;
	String image;
	String subscriptionType;
	
	public Friend(String username, String name, String image){
		this.username = username;
		this.name = name;
		this.image = image;
	}
	
	public Friend(String username, String name, String subscriptionType, String image){
		this.username = username;
		this.name = name;
		this.subscriptionType = subscriptionType;
		this.image = image;
	}
	
	public Friend(int id, String username, String name, String subscriptionType, String image){
		this.userId = id;
		this.username = username;
		this.name = name;
		this.subscriptionType = subscriptionType;
		this.image = image;
	}
	
	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}


}
