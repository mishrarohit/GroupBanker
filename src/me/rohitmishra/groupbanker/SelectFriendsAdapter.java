package me.rohitmishra.groupbanker;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

/* Using the code at 
 * http://codereview.stackexchange.com/questions/1057/android-custom-cursoradapter-design
 * mixed with code at 
 * http://stackoverflow.com/questions/9399941/implementing-a-listview-with-multiple-select-with-filter-using-a-cursor-adapter/
 * for creating a custom SimpleCursorAdapter for SelectFriends.java
 */
public class SelectFriendsAdapter extends SimpleCursorAdapter {

	private static final String TAG = "SelectFriendsAdapter";
	private final Context context ;
	private final String[] values ;
	private final int[] to ;
	private final int layout ;
	private final LayoutInflater mInflater ;
	private FriendsDbAdapter mDbHelper;
	protected Cursor mCursor;
	
	/*
	protected FilterQueryProvider mFilterQueryProvider = 
	  new FilterQueryProvider() {
  	   	public Cursor runQuery(CharSequence constraint) {
  	   		
  	   		//	try {
  	   		// Search for friends whose names begin with the specified letters.
  	   		Log.v(TAG, "runQuery Constraint = " + constraint) ;
  	   		//String selection = mDbHelper.KEY_NAME + " LIKE '%"+constraint+"%'";
  		      
  	   		mDbHelper = new FriendsDbAdapter(context);
  	   		mDbHelper.open();
  	   		
  	   		
  	   		Cursor cur = mDbHelper.fetchFriendsWithSelection(
              (constraint != null ? constraint.toString() : null));
  	   		
  	   		Log.d(TAG, "in filterqueryprovider mCursor = " + mCursor + " isClosed = " + mCursor.isClosed() ) ;
  	   		
  	   		Log.d(TAG, "runQuery cursor is " + cur + " has " + cur.getCount() + " rows  " +
  	   				"is closed = " + cur.isClosed());
  	   		
  	   		//Cursor c1 = adapter.getCursor() ;
  	   		
  	   		//Log.d(TAG, "adapter's cursor is " + c1 + " isClosed is " + c1.isClosed());
  	   		
  	   		//adapter.changeCursor(cur) ;
  	   		
  	   		return cur;
  	   		}  catch (Exception e)	{
  	   			Log.e(TAG, "runQuery Exception = " + e);
  	   			Cursor cur = null ;
  	   			return cur ;
  	   		} 
  	   		
   }
} ;

*/
	static class ViewHolder	{
		public CheckedTextView checkedText ;
	}
	
	public SelectFriendsAdapter(Context context, int layout, Cursor c,
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
	public void changeCursor(Cursor c)	{
		Log.d(TAG, "in changeCursor") ;
		super.changeCursor(c) ;
		SelectFriends.listView.clearChoices();
	}
	
	
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Log.d(TAG, "In runQueryOnBgThread") ;
		if (getFilterQueryProvider() != null) {
			Log.d(TAG,"In runQueryOnBgThread call runQuery" );
            return getFilterQueryProvider().runQuery(constraint);
        }

		Log.d(TAG, "mCursor = " + mCursor + " isClosed = " + mCursor.isClosed() );
        Log.d(TAG, "in runQueryonBG else");
        
        Cursor cursor =  ((GroupBankerApplication) context.getApplicationContext())
        .getFriendsDbAdapter().fetchFriendsWithSelection(constraint != null ? constraint.toString() : null);

        return cursor;
		
    }	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)   {
	    super.getView(position, convertView, parent);
	    Cursor cursor = getCursor() ;
	    cursor.moveToPosition(position);
	    Log.d(TAG, "Cursor position = " + cursor.getPosition());
	      
	    String bookmarkID = cursor.getString(cursor.getColumnIndex(FriendsDbAdapter.KEY_ROWID));
	    Log.d(TAG, "bookmarkID = " + bookmarkID );
	    
	    
	    ViewHolder viewHolder ;
		
	    // Log messages. 
		Log.d(TAG, "In the getView method of FriendsAdapter");
		Log.d(TAG, "position = " + position) ;
		Log.d(TAG, "SelectedIds = " + SelectFriends.selectedIds) ;
		//	Log.d(TAG, "SelectedLines = " + SelectFriends.selectedLines) ;
		//	Log.d(TAG, "checkedStates = " + SelectFriends.checkedStates) ;
		

		//mDbHelper = new FriendsDbAdapter(context) ;
		//mDbHelper.open();
		//cursor = mDbHelper.fetchAllFriends();
		
	
	
		View rowView = convertView ;
	    if(rowView == null) {
	        Log.d(TAG, "rowView = null");
	        try {
	        rowView = mInflater.inflate(layout, parent, false);
	        Log.d(TAG, "rowView inflated. rowView = " + rowView);
	        viewHolder = new ViewHolder() ;
	        viewHolder.checkedText = (CheckedTextView) rowView.findViewById(R.id.text1) ;
	        rowView.setTag(viewHolder);
	      }
	        catch (Exception e) {
	            Log.e(TAG, "getview rowView = null exception = " + e);
	        } 
	    }

	    viewHolder = (ViewHolder) rowView.getTag();

	    Log.d(TAG, "getView Cursor = " + cursor + " isClosed = " + cursor.isClosed()) ;
	    Log.d(TAG, "getView cursor has " + cursor.getCount() + " rows. " +
	    		"Cursor's current position is " + cursor.getPosition());
	    Log.d(TAG, "start viewHolder name = " + viewHolder.checkedText.toString() + 
	    		" isChecked = " + viewHolder.checkedText.isChecked()) ;
	    
	    // fill the checkedStates array with amount of bookmarks (prevent OutOfBounds Force close)
	    /*
	    if (mCursor.moveToFirst()) {
	    	Log.d(TAG, "moveToFirst worked");
          while (!mCursor.isAfterLast()) { 
              SelectFriends.checkedStates.add(false);
              mCursor.moveToNext();
          }
      }
	     */
      
	    if (SelectFriends.selectedIds.contains(new String(bookmarkID))) {
          viewHolder.checkedText.setChecked(true);
          SelectFriends.listView.setItemChecked(position, true) ;
          //    SelectFriends.selectedLines.add(position);
          Log.d(TAG, "checkedText true for bookmarkId = " + bookmarkID + " position = " + position);

	    } else {
          viewHolder.checkedText.setChecked(false);
          SelectFriends.listView.setItemChecked(position, false) ;
          //    SelectFriends.selectedLines.remove(position);
          Log.d(TAG, "checkedText false for bookmarkId = " + bookmarkID + " position = " + position);
	    }
      
	    
	    viewHolder.checkedText.setText(cursor.getString(cursor.getColumnIndex(FriendsDbAdapter.KEY_NAME)));

	    Log.d(TAG, "end viewHolder name = " + viewHolder.checkedText.toString() + 
	    		" isChecked = " + viewHolder.checkedText.isChecked()) ;
	    Log.d(TAG, "At the end of rowView");
	    return rowView;

	}

}

