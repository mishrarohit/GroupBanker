package me.rohitmishra.groupbanker;

import java.text.DecimalFormat;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class GroupBankerApplication extends Application {
	
	private static String TAG = "GroupBankerApplication";
	private DbAdapter mainDbHelper;
	private FriendsDbAdapter friendsDbHelper ;
	private TransactionDbAdapter transDbHelper;
	private SharedPreferences mPrefs ;
	private String userFBID ;
	private String userName ;
	
	@Override
	public void onCreate()	{
		mainDbHelper = new DbAdapter(this);
		mainDbHelper.open();
		
		friendsDbHelper = new FriendsDbAdapter(this);
		friendsDbHelper.open() ;
		
		transDbHelper = new TransactionDbAdapter(this);
		transDbHelper.open();
		
		mPrefs = getSharedPreferences(Constants.preferences, 0);
	}

	public FriendsDbAdapter getFriendsDbAdapter()	{
		return this.friendsDbHelper ;
	}
	
	public TransactionDbAdapter getTransDbAdapter()	{
		return this.transDbHelper ;
	}

	/* We were earlier retrieving UserFBID and UserName from SharedPreferences in onCreate. 
	 * The bug that was coming was because GroupBankerApplication may (or always will) get initialized
	 * before other classes. It's onCreate will then assign these two variables null value.
	 */
	
	public String getUserFBID() {
		this.userFBID = mPrefs.getString("userfbid", null);
		return this.userFBID;
	}

	public String getUserName() {
		this.userName = mPrefs.getString("username", null);
		Log.v(TAG, "Username Getting returned:" + this.userName);
		return this.userName;
	}
	
	/*
	 * For truncating amount to 2 digits after decimal point.
	 */
	public double roundTwoDecimals(double d)
	{
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
	
}
