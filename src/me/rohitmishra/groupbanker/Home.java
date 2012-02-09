package me.rohitmishra.groupbanker;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Home extends TabActivity	{
	
	private final String TAG = "Home" ; 
	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		Log.i(TAG, "-i onCreate called") ;
		super.onCreate(savedInstanceState) ;
		setContentView(R.layout.home) ;
		
		Resources res = getResources() ; // Resource object to get Drawables
		TabHost tabHost = getTabHost() ; // The activity TabHost
		TabHost.TabSpec spec; 
		Intent intent ;
		
		// Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, OverviewActivity.class);
		
		// Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("overview").setIndicator("Overview",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, AddTransactionActivity.class);
	    spec = tabHost.newTabSpec("newTransaction").setIndicator("New",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, HistoryActivity.class);
	    spec = tabHost.newTabSpec("history").setIndicator("History",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);
		
	    
		
		
	}
	

}
