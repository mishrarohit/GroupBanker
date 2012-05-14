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
	 private String[] selectedIds;
	 private int mID = 12457;
		
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("TAG", "In onCreate of OverviewActivity");
        mApplication = (GroupBankerApplication) getApplication();
        
        setContentView(R.layout.currentoverview);
        
		Intent intent = getIntent();
		bundle = intent.getExtras();
		Log.v("TAG", "bundle:" + bundle);
		
		overviewHelper = new overviewDbAdapter(this);
		overviewHelper.open();
		
		friendsHelper = new FriendsDbAdapter(this);
		friendsHelper.open();
		
		idLength = bundle.getInt("idLength");
		overviewIds = new long[idLength];
		overviewIds = bundle.getLongArray("overviewIds");
		description = bundle.getString("description");
		amount = bundle.getFloat("amount");
		selectedIds = bundle.getStringArray("selectedIds");	
 
		//generating dynamic view
		
        ScrollView scrollView = (ScrollView)findViewById(R.id.OuterScrollView);
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.innerLayout);
		
		
		mDescription = (TextView)findViewById(R.id.overviewDesc);
		mAmount = (TextView)findViewById(R.id.overviewAmt);
		
		mDescription.setText("Transaction Description:" + description);
		//mDescription.setId(mID);	// Set a random id for alignment 
		
		mAmount.setText("Amount: Rs. " + amount);
		//mAmount.setId(getId());
		
		/*final RelativeLayout.LayoutParams paramsDescription = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsDescription.addRule(RelativeLayout.ALIGN_LEFT);
				
		final RelativeLayout.LayoutParams paramsAmount = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsAmount.addRule(RelativeLayout.ALIGN_LEFT);
		paramsAmount.addRule(RelativeLayout.BELOW, mDescription.getId());
		
		
		relativeLayout.addView(mDescription, paramsDescription);
		relativeLayout.addView(mAmount, paramsAmount);*/
		
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
		
		Button btn = (Button)findViewById(R.id.refreshTransaction);
		btn.setOnClickListener(this);
		
		//this.setContentView(scrollView) ;
		friendsHelper.close();
		overviewHelper.close();
  }

	
	
	public int getId()	{
		mID = mID + 1 ;
		return mID ;
	}
	
	@Override
	public void onClick(View arg0)	{
		
		overviewIds = null;
		selectedIds = null;
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), Home.class);
		startActivity(intent);
		
	}
}
