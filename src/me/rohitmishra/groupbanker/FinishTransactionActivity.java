package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

public class FinishTransactionActivity extends Activity { 
	private GroupBankerApplication mApplication ;
	private static String TAG = "FinishTransactionActivity" ;
	
	private TextView mDescription ;
	private TextView mAmount ;
	private String []selectedIds ;
	private TextView[] names ;
	private EditText[] paid ;
	private EditText userPaid ;
	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.finishtransaction);
		
		mApplication = (GroupBankerApplication) getApplication();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		String description = bundle.getString("description");
		String amount = bundle.getString("amount");
		selectedIds = bundle.getStringArray("selectedIds");
		
		ScrollView scrollView = new ScrollView(this);
		RelativeLayout relativeLayout = new RelativeLayout(this) ;
		// relativeLayout.setOrientation(relativeLayout.VERTICAL);
		scrollView.addView(relativeLayout);
		
		
		// ViewGroup relativeLayout = (ViewGroup) findViewById(R.id.paid_relativeLayout);
		// Log.d(TAG, "relativeLayout made");
		
		//mDescription = (TextView) findViewById(R.id.description) ;
		//mAmount = (TextView) findViewById(R.id.amount);
		
		mDescription = new TextView(this);
		mDescription.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mAmount = new TextView(this);
		mAmount.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mDescription.setText(description);
		mDescription.setId(1234567);	// Set a random id for alignment 
		
		mAmount.setText(amount);
		mAmount.setId(1234568);
		
		final RelativeLayout.LayoutParams paramsDescription = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsDescription.addRule(RelativeLayout.ALIGN_LEFT);
		//paramsDescription.setMargins(0, 0, 200, 30) ;	
		
		final RelativeLayout.LayoutParams paramsAmount = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsAmount.addRule(RelativeLayout.ALIGN_LEFT);
		//paramsAmount.addRule(RelativeLayout.RIGHT_OF, mDescription.getId());
		
		
		relativeLayout.addView(mDescription, paramsDescription);
		relativeLayout.addView(mAmount, paramsAmount);
		
		/* Set up a separate textview and edittext for the user 
		 * We are getting username from GroupBankerApplication
		 */
		
		/*
		TextView userName = new TextView(this) ;
		userName.setText(mApplication.getUserName());
		final RelativeLayout.LayoutParams paramsUserName = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsUserName.addRule(RelativeLayout.ALIGN_LEFT);
		paramsUserName.addRule(RelativeLayout.BELOW, mDescription.getId());
		userName.setLayoutParams(paramsUserName) ;
		relativeLayout.addView(userName);
		
		userPaid = new EditText(this);
		final RelativeLayout.LayoutParams paramsUserPaid = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsUserPaid.addRule(RelativeLayout.ALIGN_RIGHT);
		paramsUserPaid.addRule(RelativeLayout.RIGHT_OF, userName.getId());
		userPaid.setLayoutParams(paramsUserPaid) ;
		relativeLayout.addView(userPaid);
		*/
		
		
		
		
		
		names = new TextView[selectedIds.length] ;
		paid = new EditText[selectedIds.length] ;
		
		/*
	
		for (int i = 0; i <  selectedIds.length; i++)	{ 
			final TextView rowTextView = new TextView(this) ;
			final EditText rowEditText = new EditText(this) ;
			
			String name = mApplication.getFriendsDbAdapter().fetchFriendName(selectedIds[i]);
			rowTextView.setText(name);
			
		//	rowTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//	rowEditText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			rowTextView.setId(i);
			
		//	rowEditText.setMinWidth(200);
			
			final RelativeLayout.LayoutParams paramsTextView = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			// The first TextView will be below userName TextView. Rest below names[i-1]
			if ( i == 0)	{
				paramsTextView.addRule(RelativeLayout.BELOW, userName.getId());
			}
			else {
				paramsTextView.addRule(RelativeLayout.BELOW, names[i-1].getId());
			}
			paramsTextView.addRule(RelativeLayout.ALIGN_LEFT);
			rowEditText.setLayoutParams(paramsTextView) ;
			
			
			final RelativeLayout.LayoutParams paramsEditText = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsEditText.addRule(RelativeLayout.RIGHT_OF, i);
			//params.addRule(RelativeLayout.ALIGN_RIGHT);
			rowEditText.setLayoutParams(paramsEditText) ;
		
			relativeLayout.addView(rowTextView);
			relativeLayout.addView(rowEditText);
			
			names[i] = rowTextView ;
			paid[i] = rowEditText ;
		}
		*/
		this.setContentView(scrollView) ;
	}

}
