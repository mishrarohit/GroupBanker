package me.rohitmishra.groupbanker;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FinalOverviewActivity extends Activity{
	
	 GroupBankerApplication application ;
     private overviewDbAdapter overviewHelper;
     private FriendsDbAdapter friendsHelper;
     private static final String TAG = "FinalOverviewActivity";
     private String username;
     private int length;
     private TextView mDescription;
     private int mID = 78965 ;
     private String friendId;
     private String friendName;
     private double amount;
     private TextView[] rowView;
     
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (GroupBankerApplication) getApplication();
        username = application.getUserName();
                
        //initialising the database helper variables
        overviewHelper = new overviewDbAdapter(this);
        friendsHelper = new FriendsDbAdapter(this);
        
        //fetching the details of the user's transit
        
        overviewHelper.open();
        friendsHelper.open();
        
        Cursor c = overviewHelper.AppUserInfo();
        startManagingCursor(c);
        length = c.getCount();
        
        //dynamic layout
        
        ScrollView scrollView = new ScrollView(this);
		RelativeLayout relativeLayout = new RelativeLayout(this) ;
		scrollView.addView(relativeLayout);
		
		//first line
		mDescription = new TextView(this);
		mDescription.setText("Overview of " + username + "'s transactions:");
		mDescription.setId(mID);
		
		final RelativeLayout.LayoutParams paramsDescription = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsDescription.addRule(RelativeLayout.ALIGN_LEFT);
			
		relativeLayout.addView(mDescription, paramsDescription);
		
		rowView = new TextView[length];
		int i = 0;
		//fetching all other friends involved in the transaction
		c.moveToFirst();
		
		//iterate till the cursor is empty
		
	   do {
		  
		  friendId = c.getString(c.getColumnIndex(overviewDbAdapter.KEY_USERID2));
		  
		  //fetching the name of the friend extracted from the overview table in the friends table
		  friendName = friendsHelper.fetchFriendName(friendId);
		  amount = c.getDouble(c.getColumnIndex(overviewDbAdapter.KEY_AMOUNT));
		  
		  if(amount > 0){
			  rowView[i].setText(friendName + " +" + amount);
		  }
		  
		  else	{
			  
			  rowView[i].setText(friendName + " " +  amount);
		  }
		  
		  rowView[i].setId(getId());
		  rowView[i].setPadding(10, 30, 60, 0);
			
		  final RelativeLayout.LayoutParams paramsTextView = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		  
		  if ( i == 0)	{
				paramsTextView.addRule(RelativeLayout.BELOW, mDescription.getId());
			}
		  
		  else	{
			  paramsTextView.addRule(RelativeLayout.BELOW, rowView[i-1].getId());
		  }
			  
	   }while(c.moveToNext());
	   
	   friendsHelper.close();
	   overviewHelper.close();
		
       
    }
	
	public int getId()	{
		mID = mID + 1 ;
		return mID ;
	}
	
}
