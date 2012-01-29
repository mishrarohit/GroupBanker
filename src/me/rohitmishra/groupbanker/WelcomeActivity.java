package me.rohitmishra.groupbanker;

import com.facebook.android.Facebook.*;
import com.facebook.android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;
import android.view.View;

public class WelcomeActivity extends Activity implements View.OnClickListener{
	
	// For Log tag
	private static final String TAG = "WelcomeActivity";
	
	Facebook facebook = new Facebook("228939630494756");

	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
   
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate called") ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        mPrefs = getPreferences(MODE_PRIVATE);
    //    String access_token = mPrefs.getString("access_token", null);
    //    long expires = mPrefs.getLong("access_expires", 0);
        
        /*
         * implementing OnClickListener() for Connect with Facebook button
         */
	  
        Button btn=(Button)findViewById(R.id.FBconnect);
        btn.setOnClickListener(this);
        
	}
	
	public void onClick(View view) {
		
		Log.v(TAG, "fbconnect button has been clicked");
		facebook.authorize(this, new String[] { "email", "publish_checkins" }, 
            	
        		new DialogListener() {
            
			@Override
            public void onComplete(Bundle values) {
				Log.v(TAG, "At start of onclick DialogListener onComplete");
				
				try 
				{
				SharedPreferences.Editor editor = mPrefs.edit();
				Log.v(TAG, "Inside onclick DialogListener onComplete got editor ");
				
				editor.putString("access_token", facebook.getAccessToken());
                editor.putLong("access_expires", facebook.getAccessExpires());
                Log.v(TAG, "Inside onclick DialogListener onComplete before editor commit ");
                
                editor.commit();
			}
				catch (Exception e)	{
					Log.w(TAG, "The SharedPreference editor didn't work "+ Log.getStackTraceString(e));
				}
		
                Log.v(TAG, "At end of onclick DialogListener onComplete");
            }

            @Override
            public void onFacebookError(FacebookError error) {}

            @Override
            public void onError(DialogError e) {}

            @Override
            public void onCancel() {}
        });
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
        Log.v(TAG, "authorizeCallBack worked");
        setContentView(R.layout.fbdone);
        Log.v(TAG, "fbdone.xml set") ;
    }
    
    public void onResume() {    
    	Log.v(TAG, "onResume called") ;
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
    
}