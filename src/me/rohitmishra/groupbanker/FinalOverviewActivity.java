package me.rohitmishra.groupbanker;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
     private TextView rowView;
     TextView[] transactions;
     
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("TAG", "In onCreate of finalOverviewActivity");
  }
	
	public int getId()	{
		mID = mID + 1 ;
		return mID ;
	}
	
	public void onPause()	{
		super.onPause();
		Log.v("TAG", "In onPause() of FinalOverviewActivity");
	}
	
	public void onResume()	{
		super.onResume();
		Log.v("TAG", "In onResume() of FinalOverviewActivity");
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
        
        Log.v("TAG", "The number of rows in cursor in finalOverview = " + length);        
        //dynamic layout
        
        ScrollView scrollView = new ScrollView(this);
		RelativeLayout relativeLayout = new RelativeLayout(this) ;
		scrollView.addView(relativeLayout);
		
		//first line
		mDescription = new TextView(this);
		mDescription.setText("Overview of " + username + "'s transactions:");
		mDescription.setGravity(1);
		mDescription.setTextSize(15);
		mDescription.setTypeface(null, 1);
		mDescription.setId(mID);
		
		final RelativeLayout.LayoutParams paramsDescription = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsDescription.addRule(RelativeLayout.ALIGN_LEFT);
			
		relativeLayout.addView(mDescription, paramsDescription);
		
		transactions = new TextView[length];
		
		int i = 0;
		//fetching all other friends involved in the transaction
		if(!c.moveToFirst())	{
			
			Log.v("TAG", "cursor empty!");
			//it means that the cursor is empty. Setting the activity to default view
			TextView textview = new TextView(this);
	        textview.setText("This is the Overview tab");
	        setContentView(textview);
		}
		
		else	{
		//iterate till the cursor is empty
		
	   do {
		   
		   rowView = new TextView(this);
		   rowView.setId(getId());
		  
		  friendId = c.getString(c.getColumnIndex(overviewDbAdapter.KEY_USERID2));
		  
		  //fetching the name of the friend extracted from the overview table in the friends table
		  friendName = friendsHelper.fetchFriendName(friendId);
		  amount = c.getDouble(c.getColumnIndex(overviewDbAdapter.KEY_AMOUNT));
		  
		  if(amount > 0){
			  rowView.setText(friendName + " +" + amount);
		  }
		  
		  else	{
			  
			  rowView.setText(friendName + " " +  amount);
		  }
		  
		  rowView.setId(getId());
		  rowView.setPadding(10, 30, 60, 0);
		  
			
		  final RelativeLayout.LayoutParams paramsTextView = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		  
		  paramsTextView.addRule(RelativeLayout.ALIGN_LEFT);
		  
		  if ( i == 0)	{
				paramsTextView.addRule(RelativeLayout.BELOW, mDescription.getId());
			}
		  
		  else	{
			  paramsTextView.addRule(RelativeLayout.BELOW, transactions[i-1].getId());
		  }
		  
		 relativeLayout.addView(rowView, paramsTextView);
		 transactions[i] = rowView;
		 i++;
		 
	   }while(c.moveToNext());
	   
	   this.setContentView(scrollView) ;
	   
	   friendsHelper.close();
	   overviewHelper.close();
		
	}
	}
	
}
