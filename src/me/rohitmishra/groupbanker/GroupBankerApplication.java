package me.rohitmishra.groupbanker;

import android.app.Application;

public class GroupBankerApplication extends Application {
	
	private FriendsDbAdapter friendsDbHelper ;
	
	@Override
	public void onCreate()	{
		friendsDbHelper = new FriendsDbAdapter(this);
		friendsDbHelper.open() ;
	}

	public FriendsDbAdapter getFriendsDbAdapter()	{
		return friendsDbHelper ;
	}
}
