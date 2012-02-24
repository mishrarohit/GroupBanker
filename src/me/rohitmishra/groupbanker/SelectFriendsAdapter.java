package me.rohitmishra.groupbanker;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
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
	private final Cursor cursor ;
	private final LayoutInflater mInflater ;

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
		this.cursor = c ;
		mInflater = LayoutInflater.from(context);
		Log.d(TAG, "At the end of the constructor") ;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)   {
	    super.getView(position, convertView, parent);
	    Log.d(TAG, "In the getView method of FriendsAdapter");
		Log.d(TAG, "position = " + position) ;
	    View rowView = convertView ;
	    if(rowView == null) {
	        Log.d(TAG, "rowView = null");
	        try {
	        rowView = mInflater.inflate(layout, parent, false);
	        Log.d(TAG, "rowView inflated. rowView = " + rowView);
	        ViewHolder viewHolder = new ViewHolder() ;
	        viewHolder.checkedText = (CheckedTextView) rowView.findViewById(R.id.text1) ;
	        rowView.setTag(viewHolder);
	        }
	        catch (Exception e) {
	            Log.e(TAG, "exception = " + e);
	        }
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();

	    Log.d(TAG, "Cursor = " + cursor) ;
	    Log.d(TAG, "getView cursor has " + cursor.getCount() + " rows. " +
	    		"Cursor's current position is " + cursor.getPosition());
	    
	   
	    
	    try {
	    	cursor.moveToFirst() ;
	    } catch (Exception e)
	    {
	    	Log.e(TAG, "exception in try block on moveToFirst = " + e);
	    }
	    
	    // fill the checkedStates array with amount of bookmarks (prevent OutOfBounds Force close)
	    
	    if (cursor.moveToFirst()) {
	    	Log.d(TAG, "moveToFirst worked");
          while (!cursor.isAfterLast()) { 
              SelectFriends.checkedStates.add(false);
              cursor.moveToNext();
          }
      }
	    
	    
	 
      cursor.moveToPosition(position);
      Log.d(TAG, "Cursor position = " + cursor.getPosition());
      
      String bookmarkID = cursor.getString(cursor.getColumnIndex(FriendsDbAdapter.KEY_ROWID));
      Log.d(TAG, "bookmarkID = " + bookmarkID );
      
      
      if (SelectFriends.selectedIds.contains(new String(bookmarkID))) {
          holder.checkedText.setChecked(true);
          SelectFriends.selectedLines.add(position);

      } else {
          holder.checkedText.setChecked(false);
          SelectFriends.selectedLines.remove(position);
      }
      
	    
      holder.checkedText.setText(cursor.getString(cursor.getColumnIndex(FriendsDbAdapter.KEY_NAME)));

	    Log.d(TAG, "At the end of rowView");
	    return rowView;

	}

}

