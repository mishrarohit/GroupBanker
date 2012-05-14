package me.rohitmishra.groupbanker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class FinishTransactionActivity extends Activity implements View.OnClickListener { 
	private GroupBankerApplication mApplication ;
	private static String TAG = "FinishTransactionActivity" ;
	
	private TextView mDescription ;
	private TextView mAmount ;
	private String []selectedIds ;
	private TextView[] names ;
	private EditText[] paid ;
	private EditText userPaid ;
	private TransactionDbAdapter mTransactionDbHelper;
	private DetailsDbAdapter detailsHelper;
	private overviewDbAdapter overviewHelper;
	private InfoDbAdapter mInfoDbAdapter ;
	private String description;
	private String amount;
	private float amount1;
	private Bundle overviewBundle = new Bundle();
	private RelativeLayout mrelativeLayout ;
	
	
	private int mID = 1234567 ;
	
	@Override
	public void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
				
		Log.v("TAG", "in onCreate of FinishTransactionActivity");
		
		setContentView(R.layout.finishtransaction);
		
		//getting the window display parameters
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		try {
		display.getSize(size);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			size.x = display.getWidth();
	        size.y = display.getHeight();
		}
		int width = size.x;
		int height = size.y;
		
		mApplication = (GroupBankerApplication) getApplication();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		mTransactionDbHelper = new TransactionDbAdapter(this);
		mTransactionDbHelper.open();
		
		detailsHelper = new DetailsDbAdapter(this);
		detailsHelper.open();
		
		overviewHelper = new overviewDbAdapter(this);
		overviewHelper.open();
		
		mInfoDbAdapter = new InfoDbAdapter(this);
		mInfoDbAdapter.open();
		
		description = bundle.getString("description");
		overviewBundle.putString("description", description);
		
		amount = bundle.getString("amount");
		amount1 = Float.valueOf(amount);
		overviewBundle.putFloat("amount", amount1);
		
		selectedIds = bundle.getStringArray("selectedIds");
		
		ScrollView scrollView = (ScrollView)findViewById(R.id.OverAllScrollView);
		RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.innerRelativeLayout);
				
		mDescription = (TextView)findViewById(R.id.description);
		mAmount = (TextView)findViewById(R.id.amount);
		
		mDescription.setText("Transaction Description:" + description);
		mAmount.setText("Amount: Rs. " + amount);
		
		
		/* Set up a separate textview and edittext for the user 
		 * We are getting username from GroupBankerApplication
		 */
		
		
		TextView userName = new TextView(this) ;
		userName.setId(getId());
		userName.setText(mApplication.getUserName());
		Log.v(TAG, "username value:" + mApplication.getUserName()) ;
		userName.setPadding(10, 10, 10, 30);
		userName.setWidth(width/2);
		userName.setTextSize(17);
		
		final RelativeLayout.LayoutParams paramsUserName = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsUserName.addRule(RelativeLayout.ALIGN_LEFT);
		paramsUserName.addRule(RelativeLayout.BELOW, mAmount.getId());
		relativeLayout.addView(userName, paramsUserName);
		
		
		userPaid = new EditText(this);
		userPaid.setId(getId());
		userPaid.setInputType(InputType.TYPE_CLASS_NUMBER); //takes only number input
		userPaid.setPadding(10, 10, 10, 10);
		userPaid.setWidth(width/2);
		userPaid.setHint(R.string.paidHint);
		
		
		
		//TODO Will be good if we can control the width without hardcoding
		
		final RelativeLayout.LayoutParams paramsUserPaid = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsUserPaid.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsUserPaid.addRule(RelativeLayout.RIGHT_OF, userName.getId());
		paramsUserPaid.addRule(RelativeLayout.ALIGN_TOP, userName.getId());
		relativeLayout.addView(userPaid, paramsUserPaid);
		
		// Now lets set the textViews and edittexts for all who have paid 
		
		names = new TextView[selectedIds.length] ;
		paid = new EditText[selectedIds.length] ;
		TextView rowTextView;
		EditText rowEditText ;
		int i ;
	
		for (i = 0; i <  selectedIds.length; i++)	{ 
			rowTextView = new TextView(this) ;
			rowTextView.setId(getId());
			//rowTextView.setMaxWidth(70);
			rowTextView.setPadding(10, 10, 10, 30);
			rowTextView.setTextSize(17);
			rowTextView.setWidth(width/2);
			
			rowEditText = new EditText(this) ;
			rowEditText.setId(getId());
			rowEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
			rowEditText.setHint(R.string.paidHint);
			rowEditText.setWidth(width/2);
			rowEditText.setPadding(10, 10, 10, 10);
			
			
			String name = mApplication.getFriendsDbAdapter().fetchFriendName(selectedIds[i]);
			rowTextView.setText(name);
			
						
			final RelativeLayout.LayoutParams paramsTextView = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			
			final RelativeLayout.LayoutParams paramsEditText = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsEditText.addRule(RelativeLayout.RIGHT_OF, rowTextView.getId());
				paramsEditText.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				paramsEditText.addRule(RelativeLayout.ALIGN_TOP, rowTextView.getId());
								
			
			// The first TextView will be below userName TextView. Rest below names[i-1]
			if ( i == 0)	{
				paramsTextView.addRule(RelativeLayout.BELOW, userName.getId());
			}
			else {
				paramsTextView.addRule(RelativeLayout.BELOW, names[i-1].getId());
				//paramsEditText.addRule(RelativeLayout.BELOW, paid[i-1].getId());
			}
			paramsTextView.addRule(RelativeLayout.ALIGN_LEFT);
			
			
			relativeLayout.addView(rowTextView, paramsTextView);
			relativeLayout.addView(rowEditText, paramsEditText);
			
			names[i] = rowTextView ;
			paid[i] = rowEditText ;
		}
		
		Button btn = (Button)findViewById(R.id.finishTransaction);
		btn.setOnClickListener(this);
		
		mrelativeLayout = relativeLayout;
		
			
	}
	
	public int getId()	{
		mID = mID + 1 ;
		return mID ;
	}
	
	

	@Override
	public void onClick(View arg0) {
		
		String userAmt = userPaid.getText().toString();
		float floatVal =0, sum =0;
		int flag = 0;
		
		if (userAmt == null || userAmt.equalsIgnoreCase("")) {
			userPaid.setError("This field cannot be blank.") ;
			flag = 1;
		}
		
		
		int i=0;
		//iterating among the edit texts for each friends to check for empty fields
		for(i=0; i<selectedIds.length; i++)	{
				
				if(paid[i] == null || paid[i].getText().toString().equalsIgnoreCase("")){
					
					flag =1;
					paid[i].setError("This field cannot be blank.") ;
				}
			}
			
		
		if (flag == 0) {
			
		screenLayout();
		sum = 0;
		
		floatVal = Float.valueOf(userPaid.getText().toString());
		sum += floatVal;
		
		for(i=0; i<selectedIds.length; i++){
					
					floatVal = Float.valueOf(paid[i].getText().toString());
					sum += floatVal;
				}
				
		if (sum != amount1)
		{
			Toast.makeText(getApplicationContext(), "The sum of the individual amounts should be equal to the total amount entered",
			                 Toast.LENGTH_LONG).show();
			
		}
		
		else {
		int lastId;
		float paid1, difference;
		String[] userId = new String[selectedIds.length+1];
		float[] diff = new float[selectedIds.length+1];
		long[] finalIds;
		Long timestamp = System.currentTimeMillis();
		String formatted = timestamp.toString();
		//Date d = new Date();
		//String formatted = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(d);
		Log.v(TAG, "values going in the transaction table are:" + amount1 + "description" + description);
		
		//entering details in transaction database
		lastId =(int)mTransactionDbHelper.createTransaction(amount1, description, formatted);
		Log.v(TAG, "The ID of the last inserted row:" + lastId);
		
		//entering details in Details table
		
		//amount equally divided among people involved
		float toPay = amount1/(selectedIds.length+1);
		
		// inserting the app user details in the details table
		
		String paidVal1 = userPaid.getText().toString();
		paid1 = Float.valueOf(paidVal1);
		detailsHelper.createDetails(lastId, "0", toPay, paid1);
		
		difference = paid1 - toPay;
		userId[0] = "0"; 
		diff[0] = difference;
		
		//entering the details for rest of the people involved in transaction
		for (i=0; i<selectedIds.length; i++)	{
			
			difference = 0;
		    String paidVal = paid[i].getText().toString();
			paid1 = Float.valueOf(paidVal);
			difference = paid1-toPay;
			
			//inserting the values in corresponding arrays
			userId[i+1] = selectedIds[i];
			diff[i+1] = difference;
						
			Log.v(TAG, "values going in the details table are: transID = " + lastId + "fbid=" + selectedIds[i] + "paid = " + paid1 + "topay = " + toPay);
			detailsHelper.createDetails(lastId,selectedIds[i], toPay, paid1);
		}
		
		//a bit of housekeeping i want to know the arrays are created well
		
		Log.v(TAG, "the userID array: ");
		for(i=0; i<= selectedIds.length; i++)	{
			
			Log.v(TAG, userId[i] + ",");
		}
		
		Log.v(TAG, "the difference array: " );
		for(i=0; i<= selectedIds.length; i++)	{
					
			Log.v(TAG, diff[i] + ",");
		}
		
		//sorting the arrays
		sort(userId, diff);
		Log.v(TAG, "Sorted Arrays are");
		
		Log.v(TAG, "the userID array: ");
		for(i=0; i<= selectedIds.length; i++)	{
			
			Log.v(TAG, userId[i] + ",");
		}
		
		Log.v(TAG, "the difference array: " );
		for(i=0; i<= selectedIds.length; i++)	{
					
			Log.v(TAG, diff	[i] + ",");
		}
		
		 //sending the sorted array to update the overview table
		updateOverview(userId, diff, lastId);	 
		 				
		 Toast.makeText(getApplicationContext(), "Transaction successfully saved",
                 Toast.LENGTH_LONG).show();
		mTransactionDbHelper.close();
		detailsHelper.close();
		mInfoDbAdapter.close();
		
		Log.v("TAG", "starting activity overview");
		Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
		intent.putExtras(overviewBundle);
				
		//displaying the overview of the current transaction in the same tab
		
		TabGroupActivity parentActivity1 = (TabGroupActivity)getParent();
		parentActivity1.startChildActivity("OverViewActivity", intent);
		
		

		}
	}
	}

	
	//selection sort function
	
	public void sort(String userId[], float diff[])	{
		
		int l = userId.length;
		int i,j,maxIndex;
		float temp1;
		String temp2;
		
		for(i = 0; i < l-1; i++)	{
			
			maxIndex = i;
			
			for (j=i+1; j < l; j++)	{
				
				if(diff[j] > diff[maxIndex])	{
					maxIndex = j;					
				}
			}
				
				if(maxIndex != i)	{
					temp1 = diff[i];
					diff[i] = diff[maxIndex];
					diff[maxIndex] = temp1;
					
					temp2 = userId[i];
					userId[i]= userId[maxIndex];
					userId[maxIndex] = temp2;
				}
			}
		}
	
	//function definition for updateOverview
	public void updateOverview(String userId[], float diff[], int transID)	{
		
		int l = selectedIds.length; 	//last index of the arrays
		int f = 0, i = 0;    					//first index of the array
		long[] overviewIds = new long[selectedIds.length];
		String user1, user2;
		float amount;
		
		//loop the diff array till first index <= last index
		
		while(f < l)	{
			
			user1 = userId[f];
			user2 = userId[l];
			
			// 3 conditions to be taken care of while comparing the difference values
			if(diff[f] > java.lang.Math.abs(diff[l]))	{
				
				diff[f] = diff[f] - java.lang.Math.abs(diff[l]);
				amount = java.lang.Math.abs(diff[l]);
				diff[l] = 0;
				l--;
				if(diff[f] == 0)
					f++;
			}
			
			else if(diff[f] < java.lang.Math.abs(diff[l]))	{
				
				diff[l] = java.lang.Math.abs(diff[l]) - diff[f];
				amount = diff[f];
				diff[f] = 0;
				f++;
				if(diff[l] == 0)
					l--;
			}
			
			else	{
				
				amount = diff[f];
				l--;
				f++;
			}
			
			int id1 = Integer.valueOf(user1);
			int id2 = Integer.valueOf(user2);
			String temp;
			
			if(id1 > id2)	{
				temp = user1;
				user1 = user2;
				user2 = temp;
				amount = -(amount);
			}
			
			// Adding the data to the info table 
			
			mInfoDbAdapter.createInfo(transID, user1, user2, amount);
			
			Log.v(TAG, "Value going in overview update function user1:" + user1 + "user2:" + user2 + "amount:" + amount);
		
			//updating the overview table
			
			overviewIds[i] = overviewHelper.updateAmount(user1, user2, amount);
			Log.v(TAG, "id returned from update:" + overviewIds[i]);
			i++;
		}
		
		overviewBundle.putLongArray("overviewIds", overviewIds);
		overviewBundle.putStringArray("selectedIds", selectedIds);
		overviewBundle.putInt("idLength", i);
		
	}
	
	@Override
	public void onStart()	{
		super.onStart();
		Log.v("TAG", "In onStart of FinishTransactionActivity");
	}
	
	
	public void screenLayout()	{
	
		//getting the modified values after the changes made
		int i=0;
		for(i=0; i< selectedIds.length; i++){
			
			paid[i] = (EditText) mrelativeLayout.findViewById(paid[i].getId());
		}
	}
	

}
