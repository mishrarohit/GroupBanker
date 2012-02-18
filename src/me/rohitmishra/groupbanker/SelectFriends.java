package me.rohitmishra.groupbanker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class SelectFriends extends ListActivity{
	
	private FriendsDbAdapter mDbHelper;
	private static final String TAG = "SelectFriends";
	
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  Log.v(TAG, "onCreate called") ;
		  
		  mDbHelper = new FriendsDbAdapter(this);
	      mDbHelper.open() ;
	      
	      Log.v(TAG, "database opened") ;
	      
	      Cursor c = mDbHelper.fetchAllFriends();
	      startManagingCursor(c);
	      
	      Log.v(TAG, "fetchAllFriends Over") ;
	      
	      String[] from = new String[] {mDbHelper.KEY_NAME};
	      int[] to = new int[] { R.id.text1 };
	      
	   // Now create an  adapter and set it to display using our row
	        SimpleCursorAdapter friends =
	            new SimpleCursorAdapter(this, R.layout.list_item, c, from, to);
	        setListAdapter(friends);
	        
	        mDbHelper.close();
	        

}
}
