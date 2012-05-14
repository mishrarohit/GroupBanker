package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends Activity{
	
	GroupBankerApplication application ;
    private TransactionDbAdapter transHelper;
    private static final String TAG = "FinalOverviewActivity";
    private int mID = 876543;
    private int length;
	TableLayout historyTable;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("TAG", "in onCreate of HistoryActivity");
    }
	
	public void onPause()	{
		super.onPause();
		Log.v("TAG", "In onPause() of FinalOverviewActivity");
	}
	
	public void onResume()	{
		super.onResume();
		
		Log.v("TAG", "in onResume of HistoryActivity");
		
		//setting the content layout and defining the dynamic table
		setContentView(R.layout.history);
		historyTable = (TableLayout)findViewById(R.id.history_table);
		fillHistoryTable();
	}
	
	public void fillHistoryTable()	{
		
		//initialising the database variable and opening the database
		transHelper = new TransactionDbAdapter(this);
		transHelper.open();
		
		Cursor c = transHelper.fetchAllTransactions();
		startManagingCursor(c);
		length = c.getCount();
		Log.v("TAG", "The number of rows in cursor in finalOverview = " + length);
		
		if(!c.moveToFirst())	{
			//cursor empty
			TextView tv = new TextView(this);
			tv.setText("No transaction records available. Add a transaction in the New Tab.");
			setContentView(tv);
		}
		
		else  {
		   //cursor is not empty we need to generate a dynamic table from cursor values     
	       
	    TableRow row;
	    TextView desc, amt, time;
	    int i = 0;
	    
	    //iterating over consequetive cursor values
	    do	{
	    	
	    	row = new TableRow(this);
	    	desc = new TextView(this);
	    	amt = new TextView(this);
	    	time = new TextView(this);
	    	
	    	desc.setText(c.getString(c.getColumnIndex(TransactionDbAdapter.KEY_DESC)));
	    	amt.setText(c.getString(c.getColumnIndex(TransactionDbAdapter.KEY_AMOUNT)));
	    	// time.setText(c.getString(c.getColumnIndex(TransactionDbAdapter.KEY_TIME)));
	    	long timestamp =  c.getLong(c.getColumnIndexOrThrow(TransactionDbAdapter.KEY_TIME));
	    	
	    	CharSequence t = DateFormat.format("MMM dd", timestamp);
	    	String timeString = t.toString();
	    	
	    	time.setText(timeString);
	    	
	    	desc.setPadding(5, 5, 5, 5);
	    	amt.setPadding(5, 5, 5, 5);
	    	time.setPadding(5, 5, 5, 5);
	    	
	    	row.addView(desc);
	    	row.addView(amt);
	    	row.addView(time);
	    	
	    	historyTable.addView(row, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));	    	
	    	
	    }while(c.moveToNext());
	    
	        
	}
		
		transHelper.close();
}
	
	public int getId()	{
		mID = mID + 1 ;
		return mID ;
	}
}
