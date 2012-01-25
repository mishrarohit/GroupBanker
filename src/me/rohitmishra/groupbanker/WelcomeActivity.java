package me.rohitmishra.groupbanker;

import com.facebook.android.Facebook.*;
import com.facebook.android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class WelcomeActivity extends Activity {
	Facebook facebook = new Facebook("228939630494756");

	String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
    @Override
    public void onCreate(Bundle savedInstanceState)	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.welcome) ;
    }
}

