package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Home extends Activity	{
	
	private final String TAG = "Home" ; 
	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		Log.v(TAG, "-v onCreate called") ;
		Log.i(TAG, "-i onCreate called") ;
		super.onCreate(savedInstanceState) ;
		
		setContentView(R.layout.home) ;
		
		TabHost tabs=(TabHost)findViewById(R.id.tabhost);
		
		tabs.setup();
		
		TabHost.TabSpec spec=tabs.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Overview");
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("New Expense");
		tabs.addTab(spec);
		
		spec = tabs.newTabSpec("tag3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("History");
		tabs.addTab(spec);
		
	}
	

}
