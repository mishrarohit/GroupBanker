package me.rohitmishra.groupbanker;

import com.facebook.android.Facebook.*;
import com.facebook.android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class WelcomeActivity extends Activity implements View.OnClickListener{
	
	Facebook facebook = new Facebook("228939630494756");

	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        /*
         * implementing OnClickListener() for Connect with Facebook button
         */
	  
        Button btn=(Button)findViewById(R.id.FBconnect);
        btn.setOnClickListener(this);
        
	}
	
	public void onClick(View view) {
		
		facebook.authorize(this, new String[] { "email", "publish_checkins" }, 
            	
        		new DialogListener() {
            @Override
            public void onComplete(Bundle values) {
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("access_token", facebook.getAccessToken());
                editor.putLong("access_expires", facebook.getAccessExpires());
                editor.commit();
                
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
        setContentView(R.layout.main);
    }
    
    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
}