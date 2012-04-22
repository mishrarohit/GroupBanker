package me.rohitmishra.groupbanker;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Home extends TabActivity	{
	
	private final String TAG = "Home" ; 
	
	TabHost tabHost1; 
	
		@Override
	public void onCreate(Bundle savedInstanceState)	{
			
		Log.i(TAG, "-i onCreate called") ;
		super.onCreate(savedInstanceState) ;
		setContentView(R.layout.home) ;
		Resources res = getResources() ; 
		TabHost tabHost = getTabHost() ; 
		TabHost.TabSpec spec; 
		
		Log.v("TAG", "in onCreate of Home.java. var res=" + res + "var tabHost = " + tabHost);
		
		Intent intent ;
		
		// Create an Intent to launch an ActivityGroup for the tab (to be reused)
	    intent = new Intent().setClass(this, FinalOverviewActivity.class);
		
		// Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("overview").setIndicator("overview",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, TabGroup1Activity.class);
	    spec = tabHost.newTabSpec("newTransaction").setIndicator("New",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, HistoryActivity.class);
	    spec = tabHost.newTabSpec("history").setIndicator("History",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    
	    // To set add transaction as default tab 
	    tabHost.setCurrentTab(1) ;
	    tabHost1 = tabHost;
	    
	}
		
	/*public void switchTab(int tab, Intent intent){
	
		tabHost1.setCurrentTab(tab);
		
	}*/
	
	

}
