package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.content.SharedPreferences ;
import com.facebook.android.*;
import com.facebook.android.Facebook.*;
import com.facebook.*;

public class GroupBankerActivity extends Activity {
	
	private static final String TAG = "GroupBankerActivity";
	Facebook facebook = new Facebook("228939630494756");
	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate called") ;
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //  Get existing access_token if any
         
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        
        //  Launch the welcomeActivity if the access_token has expired.
         
       Log.v(TAG, "fbsessionvalid= " + facebook.isSessionValid()) ;
       Log.v(TAG, "fbaccesstoken= " + facebook.getAccessToken()) ;
       
        if(!facebook.isSessionValid()) {
        	Intent i = new Intent(this, WelcomeActivity.class);
        	startActivity(i);
         }
    }
}