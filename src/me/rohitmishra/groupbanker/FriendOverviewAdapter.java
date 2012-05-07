package me.rohitmishra.groupbanker;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FriendOverviewAdapter extends SimpleCursorAdapter{
	private static final String TAG = "FriendOverviewAdapter";
	private final Context context ;
	private final String[] values ;
	private final int[] to ;
	private final int layout ;
	private final LayoutInflater mInflater ;
	//private FriendsDbAdapter mFriendsDbHelper;
	protected Cursor mCursor;
	private String transID ; 
	private String transName ;
	private Double doubleAmount ;
	private String amount ;
	
	static class ViewHolder	{
		public TextView timestamp ;
		public TextView transactionName ;
		public ImageView statusImage ;
		public TextView amount ;
	}
	
	public FriendOverviewAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context ;
		this.values = from ;
		this.layout = layout ;
		this.to = to ;
		this.mCursor = c ;
		this.mInflater = LayoutInflater.from(context);
		Log.d(TAG, "At the end of the constructor") ;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)   {
	    super.getView(position, convertView, parent);
	    Cursor cursor = getCursor() ;
	    cursor.moveToPosition(position);
	    Log.d(TAG, "Cursor position = " + cursor.getPosition());
	    
	    ViewHolder viewHolder ;
	    
	    View rowView = convertView ;
	    
	    if(rowView == null) {
	        Log.d(TAG, "rowView = null");
	        rowView = mInflater.inflate(layout, parent, false);
	        Log.d(TAG, "rowView inflated. rowView = " + rowView);
	        viewHolder = new ViewHolder() ;
	        viewHolder.timestamp = (TextView) rowView.findViewById(R.id.timestamp);
	        viewHolder.transactionName = (TextView) rowView.findViewById(R.id.transactionName) ;
	        viewHolder.statusImage = (ImageView) rowView.findViewById(R.id.statusImage) ;
	        viewHolder.amount = (TextView) rowView.findViewById(R.id.amount);
	        rowView.setTag(viewHolder);
	      }
	    
	    viewHolder = (ViewHolder) rowView.getTag();
	    
	    transID = cursor.getString(cursor.getColumnIndex(InfoDbAdapter.KEY_TRANSID));
		  
	    //fetching the transaction timestamp from the transaction table
		String transTimestamp = ((GroupBankerApplication) context.getApplicationContext())
        .getTransDbAdapter().fetchTransactionTime(transID);
		viewHolder.timestamp.setText(transTimestamp);
		
		//fetching the transaction description from the transaction table
		String transDesc = ((GroupBankerApplication) context.getApplicationContext())
        .getTransDbAdapter().fetchTransactionDesc(transID);
		viewHolder.transactionName.setText(transDesc);
		
		
		doubleAmount = cursor.getDouble(cursor.getColumnIndex(overviewDbAdapter.KEY_AMOUNT));
		
		doubleAmount = ((GroupBankerApplication) context.getApplicationContext())
		.roundTwoDecimals(doubleAmount) ;
	         
		amount = doubleAmount.toString() ;
		viewHolder.amount.setText(amount);
		
		if (doubleAmount > 0){
			viewHolder.statusImage.setImageResource(R.drawable.greenarrow) ;
		}
		else {
			viewHolder.statusImage.setImageResource(R.drawable.redarrow);
		}
		
		return rowView ;
	}
}
