package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

public class OverviewActivity extends Activity implements View.OnClickListener{
	
	 private Bundle bundle;
	 private GroupBankerApplication mApplication ;
	 private static String TAG = "OverviewActivity" ;
	 private FriendsDbAdapter friendsHelper;
	 private overviewDbAdapter overviewHelper;	
	 private int idLength;	
	 private long[] overviewIds;
	 private String[] userIds = new String[2];
	 private TextView mDescription;
	 private TextView mAmount;
	 private String description;
	 private float amount;
	 private String name1;
	 private String name2;
	 private int mID = 12457;
		
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mApplication = (GroupBankerApplication) getApplication();
		
		Intent intent = getIntent();
		bundle = intent.getExtras();
		Log.v("TAG", "bundle:" + bundle);
		
		if (bundle == null)
		{
			TextView textview = new TextView(this);
	        textview.setText("Consolidated overview of the current transaction");
	        setContentView(textview);
		}
		
		else
		{
		overviewHelper = new overviewDbAdapter(this);
		overviewHelper.open();
		
		friendsHelper = new FriendsDbAdapter(this);
		friendsHelper.open();
		
		idLength = bundle.getInt("idLength");
		overviewIds = new long[idLength];
		overviewIds = bundle.getLongArray("overviewIds");
		description = bundle.getString("description");
		amount = bundle.getFloat("amount");
		
        /*TextView textview = new TextView(this);
        textview.setText("Consolidated overview of the current transaction");
        setContentView(textview);*/
        
        ScrollView scrollView = new ScrollView(this);
		RelativeLayout relativeLayout = new RelativeLayout(this) ;
		scrollView.addView(relativeLayout);
		
		mDescription = new TextView(this);
		mAmount = new TextView(this);
		
		mDescription.setText(description);
		mDescription.setId(mID);	// Set a random id for alignment 
		
		mAmount.setText("Amount = Rs. " + amount);
		mAmount.setId(getId());
		
		final RelativeLayout.LayoutParams paramsDescription = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsDescription.addRule(RelativeLayout.ALIGN_LEFT);
		//paramsDescription.setMargins(0, 0, 200, 30) ;	
		
		final RelativeLayout.LayoutParams paramsAmount = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsAmount.addRule(RelativeLayout.ALIGN_LEFT);
		paramsAmount.addRule(RelativeLayout.BELOW, mDescription.getId());
		
		
		relativeLayout.addView(mDescription, paramsDescription);
		relativeLayout.addView(mAmount, paramsAmount);
		
		TextView rowTextView;
		TextView[] entries = new TextView[idLength];
		int i ;
		
		//iterating over the id array to set each row
		
		for(i=0; i<idLength; i++)	{
			
			rowTextView = new TextView(this) ;
			rowTextView.setId(getId());
			
			userIds = overviewHelper.getUserIds(overviewIds[i]);
			
			
			name1 = friendsHelper.fetchFriendName(userIds[0]);
			name2 = friendsHelper.fetchFriendName(userIds[1]);
			
			if (name1 == "0") {
				
				name1 = mApplication.getUserName();
			}
			
			if(name2 == "0")	{
				
				name2 = mApplication.getUserName();
			}
				
			Log.v("TAG", "name 1:" + name1 + "name 2:" + name2);
			
			float amt = overviewHelper.getAmount(overviewIds[i]);
			
			//two possible signs of amt (+ve or _ve)
			
			if(amt > 0)	{
				
				//user 2 has to pay user 1 amt
				rowTextView.setText(name2 + " has to pay Rs" + amt + " to " + name1);				
			}
			
			else if(amt < 0)	{
				
				//user 1 has to pay user 2
				rowTextView.setText(name1 + " has to pay Rs" + -(amt) + " to " + name2);
			}
			
			else  {
				
				//user 1 and user 2 are even
				
				rowTextView.setText(name1 + " and " +  name2 + " are even");
				
			}
			
			rowTextView.setPadding(10, 30, 60, 0);
			final RelativeLayout.LayoutParams paramsTextView = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			paramsTextView.addRule(RelativeLayout.ALIGN_LEFT);
			
			if(i == 0)	{
				paramsTextView.addRule(RelativeLayout.BELOW, mAmount.getId());
			}
			
			else	{
				
				paramsTextView.addRule(RelativeLayout.BELOW, entries[i-1].getId());
			}
			relativeLayout.addView(rowTextView, paramsTextView);
			entries[i] = rowTextView;
				
		}
		
		Button btn = new Button(this);
		btn.setText(R.string.newTransaction);
		btn.setId(getId());
		btn.setOnClickListener(this);
		final RelativeLayout.LayoutParams paramsButton = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsButton.addRule(RelativeLayout.BELOW, entries[idLength-1].getId());
		
		
		relativeLayout.addView(btn, paramsButton);
		
		this.setContentView(scrollView) ;
		friendsHelper.close();
		overviewHelper.close();
  }
}
	
	
	public int getId()	{
		mID = mID + 1 ;
		return mID ;
	}
	
	@Override
	public void onClick(View arg0)	{
		
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), Home.class);
		startActivity(intent);
		
	}
}
