package me.rohitmishra.groupbanker;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FinalOverviewActivity extends ListActivity{
	
	 GroupBankerApplication application ;
     private overviewDbAdapter mOverviewHelper;
    // private FriendsDbAdapter friendsHelper;
     private static final String TAG = "FinalOverviewActivity";
     private String username;
     private int length;
    // private TextView mDescription;
     private int mID = 78965 ;
   //  private String friendId;
   //  private String friendName;
   //  private double amount;
   //  private TextView rowView;
   //  TextView[] transactions;
     static ListView listView ;
     private TextView overviewHeader ;
     private Context context ;
     private FinalOverviewAdapter adapter ;
     
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaloverview) ;
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
		context = this.getApplicationContext() ;
        username = application.getUserName();
                
        //initialising the database helper variables
        mOverviewHelper = new overviewDbAdapter(this);
        //friendsHelper = new FriendsDbAdapter(this);
        
        //fetching the details of the user's transit
        
        mOverviewHelper.open();
        //friendsHelper.open();
        
        Cursor c = mOverviewHelper.AppUserInfo();
        startManagingCursor(c);
        length = c.getCount();
        
        Log.v("TAG", "The number of rows in cursor in finalOverview = " + length);
        
        overviewHeader = (TextView)findViewById(R.id.overviewheader);
        overviewHeader.setText("Overview of " + username + "\'s expenses") ;
        
        listView = getListView() ;
        
        String[] from = new String[] {mOverviewHelper.KEY_USERID2, mOverviewHelper.KEY_AMOUNT};
        int[] to = new int[] { R.id.friendName, R.id.amount } ;
        
        // Now initialize the  adapter and set it to display using our row
        adapter =
    	   new FinalOverviewAdapter(this, R.layout.finaloverviewrow, c, from, to);
        
        setListAdapter(adapter);
        
        listView.setItemsCanFocus(true);
        
        
        
        
        
       
        /*
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
	*/
       
	mOverviewHelper.close();
	}
	
	@Override	
	protected void onListItemClick(ListView parent, View v, int position, long id) {
		
		// Get the userID of the friend whose name has been clicked on 
		
		Cursor tempCursor = (Cursor)parent.getItemAtPosition(position);
		String userID = tempCursor.getString(tempCursor.getColumnIndex(mOverviewHelper.KEY_USERID2));
		Double doubleAmount = tempCursor.getDouble(tempCursor.getColumnIndex(mOverviewHelper.KEY_AMOUNT));
		Intent intent = new Intent(getApplicationContext(), FriendOverviewActivity.class);
		intent.putExtra("userID1", "0");
		intent.putExtra("userID2", userID);
		intent.putExtra("doubleAmount", doubleAmount);
		startActivity(intent);		
	}
	
	
}
