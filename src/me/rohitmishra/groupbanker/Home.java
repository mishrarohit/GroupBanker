package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Home extends Activity	{
	
	private final String TAG = "Home" ; 
	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		Log.v(TAG, "-v onCreate called") ;
		Log.i(TAG, "-i onCreate called") ;
		super.onCreate(savedInstanceState) ;
		
		setContentView(R.layout.home) ;
		
	}
	

}
