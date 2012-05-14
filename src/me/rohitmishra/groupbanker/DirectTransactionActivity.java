package me.rohitmishra.groupbanker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class DirectTransactionActivity extends Activity implements OnClickListener {
	private static final String TAG = "DirectTransactionActivity";
	private GroupBankerApplication mApplication ;
	private InfoDbAdapter mInfoHelper;
	private overviewDbAdapter mOverviewHelper ;
	private TransactionDbAdapter mTransactionHelper ;
	
	private String mUserID1 ;
	private String mUserID2 ;
	private Context mContext ;
	private Bundle bundle ;
	private int mFlag = -1 ;
	
	private TextView mFriendName ;
	private ToggleButton mToggle ;
	private EditText mDesc ;
	private EditText mAmount ;
	private Button mDone ;
	
	@Override 
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directtransaction);
		
		mApplication = (GroupBankerApplication) getApplication();
		mContext = this.getApplicationContext() ;
		
		mInfoHelper = new InfoDbAdapter(this);
		mInfoHelper.open();
		
		mOverviewHelper = new overviewDbAdapter(this);
		mOverviewHelper.open();
		
		mTransactionHelper = new TransactionDbAdapter(this);
		mTransactionHelper.open();
		
		mFriendName = (TextView) findViewById(R.id.friend);
		mToggle = (ToggleButton) findViewById(R.id.toggleButton);
		mDesc = (EditText) findViewById(R.id.desc);
		mAmount = (EditText) findViewById(R.id.amount);
		mDone = (Button) findViewById(R.id.buttondone);
		
		Intent intent = getIntent() ;
		bundle = intent.getExtras();
		
		if(bundle != null)	{
			mUserID1 = bundle.getString("userID1");
			mUserID2 = bundle.getString("userID2");			
		}
		
		//fetching the name of the friend extracted from the overview table in the friends table
		String friendNameString = ((GroupBankerApplication) mContext.getApplicationContext())
        .getFriendsDbAdapter().fetchFriendName(mUserID2);
		
		mFriendName.setText(friendNameString);
		
		mDone.setOnClickListener(this);
		
		
		
		
	}
	
	public void onToggleClicked(View v) {
	    // Perform action on clicks
	    if (((ToggleButton) v).isChecked()) {
	    	Log.v(TAG, "Setting mFlag to 1");
	        mFlag = 1 ;
	    } else {
	        mFlag = -1 ;
	    }
	}

	@Override
	public void onClick(View v) {
		int flag = 1 ;
		String desc = mDesc.getText().toString();
		String amount = mAmount.getText().toString();
		Float fAmount = Float.valueOf(amount);
		
		
		if (desc == null || desc.equalsIgnoreCase("")) {
			mDesc.setError("This field cannot be blank.") ;
			flag = 0;
		}
		
		if (amount == null || amount.equalsIgnoreCase("")) {
			mAmount.setError("This field cannot be blank.") ;
			flag = 0;
		}
		
		if (flag == 1) {
			//Date d = new Date();
			//String formatted = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(d);
			Long timestamp = System.currentTimeMillis();
			String formatted = timestamp.toString();
			
			int transID =(int)mTransactionHelper.createTransaction(fAmount, desc, formatted);

			if (mFlag == -1){
				fAmount = -(fAmount);
			}
			
			else
			{
				fAmount = java.lang.Math.abs(fAmount);
			}
				
			
			int user1 = Integer.valueOf(mUserID1);
			int user2 = Integer.valueOf(mUserID2);
		
			// To ensure that user1 is always less than user2 in info table 
			String temp ;
			if(user1 > user2)	{
				temp = mUserID1;
				mUserID1 = mUserID2;
				mUserID2 = temp;
				fAmount = -(fAmount);
			}
			
			mInfoHelper.createInfo(transID, mUserID1, mUserID2, fAmount);
			
			mOverviewHelper.updateAmount(mUserID1, mUserID2, fAmount) ;
			
			mInfoHelper.close();
			mOverviewHelper.close();
			mTransactionHelper.close();
			
			Toast.makeText(getApplicationContext(), "Transaction successfully saved",
	                 Toast.LENGTH_LONG).show();
			
			Intent intent = new Intent(getApplicationContext(), FinalOverviewActivity.class);
			TabGroupActivity parentActivity1 = (TabGroupActivity)getParent();
			parentActivity1.startChildActivity("FinalOveriewActivity", intent);
			//startActivity(intent);
		}
		
	}
	
		

}
