package me.rohitmishra.groupbanker;

import android.app.Application;

public class GroupBankerApplication extends Application {
	
	private FriendsDbAdapter friendsDbHelper ;
	private String userFBID ;
	private String userName ;
	
	@Override
	public void onCreate()	{
		friendsDbHelper = new FriendsDbAdapter(this);
		friendsDbHelper.open() ;
	}

	public FriendsDbAdapter getFriendsDbAdapter()	{
		return friendsDbHelper ;
	}

	public void setUserFBID(String userFBID) {
		this.userFBID = userFBID;
	}

	public String getUserFBID() {
		return userFBID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
