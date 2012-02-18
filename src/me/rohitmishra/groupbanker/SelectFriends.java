package me.rohitmishra.groupbanker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
	      int[] to = new int[] { android.R.id.text1 };
	      
	   // Now create an  adapter and set it to display using our row
	        SimpleCursorAdapter friends =
	            new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, c, from, to);
	        setListAdapter(friends);
 	        
            final ListView listView = getListView();
	        
	        listView.setItemsCanFocus(false);
	        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        
	        mDbHelper.close();
	        
	        
}
}
