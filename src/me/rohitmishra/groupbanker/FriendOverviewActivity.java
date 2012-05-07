package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



public class FriendOverviewActivity extends Activity implements OnClickListener{
	
	private static final String TAG = "FriendOverview";
	private GroupBankerApplication mApplication ;
	private InfoDbAdapter mInfoHelper;
	private String mUserID1 ;
	private String mUserID2 ;
	private String mName1 ;
	private String nName2 ;
	private Context mContext ;
	private Bundle bundle ;
	private TextView friendName ;
	private TextView friendAmount ;
	private Button friendTransaction ;
	private ListView friendOverviewList ;
	private Double mDoubleAmount ;
	private FriendOverviewAdapter adapter ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendoverview);
		  
		mApplication = (GroupBankerApplication) getApplication();
		mContext = this.getApplicationContext() ;
		
		mInfoHelper = new InfoDbAdapter(this);
		mInfoHelper.open();
		
		friendName = (TextView) findViewById(R.id.friendName);
		friendAmount = (TextView) findViewById(R.id.friendAmount);
		friendTransaction = (Button) findViewById(R.id.friendTransaction);
		friendOverviewList = (ListView) findViewById(R.id.friendOverviewList);
		
		Intent intent = getIntent() ;
		bundle = intent.getExtras();
		
		if(bundle != null)	{
			mUserID1 = bundle.getString("userID1");
			mUserID2 = bundle.getString("userID2");
			mDoubleAmount = bundle.getDouble("doubleAmount");
			
		}
		
		//fetching the name of the friend extracted from the overview table in the friends table
		String friendNameString = ((GroupBankerApplication) mContext.getApplicationContext())
        .getFriendsDbAdapter().fetchFriendName(mUserID2);
		
		friendName.setText(friendNameString);
		
		// Convert double to appropriate format 
		
		mDoubleAmount = ((GroupBankerApplication) mContext.getApplicationContext())
		.roundTwoDecimals(mDoubleAmount) ;
	         
		friendAmount.setText(mDoubleAmount.toString()) ;
		
		Cursor c = mInfoHelper.fetchTransIDAmount(mUserID1, mUserID2);
		startManagingCursor(c);
		
		String[]from = new String[] {mInfoHelper.KEY_TRANSID, mInfoHelper.KEY_AMOUNT};
		int[] to = new int[]{R.id.transactionName, R.id.amount};
		
		// Now initialize the  adapter and set it to display using our row
        adapter =
    	   new FriendOverviewAdapter(this, R.layout.friendoverviewrow, c, from, to);
        
       
		friendOverviewList.setAdapter(adapter);
		
		friendTransaction.setOnClickListener(this);
	
		mInfoHelper.close();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getApplicationContext(), DirectTransactionActivity.class);
		intent.putExtra("userID1", mUserID1);
		intent.putExtra("userID2", mUserID2);
		startActivity(intent);
		
	}
	
	
}
