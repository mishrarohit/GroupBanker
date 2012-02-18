package me.rohitmishra.groupbanker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SelectFriends extends ListActivity{
	
	private FriendsDbAdapter mDbHelper;
	private static final String TAG = "SelectFriends";
	
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.list);
		  
		  Log.v(TAG, "onCreate called") ;
		  
		  mDbHelper = new FriendsDbAdapter(this);
	      mDbHelper.open() ;
	      
	      //ListView listView = (ListView)findViewById(R.id.mylist);
	      //listView = getListView();
	     
	     
	      
	      Log.v(TAG, "database opened") ;
	      
	      Cursor c = mDbHelper.fetchAllFriends();
	      startManagingCursor(c);
	      
	      Log.v(TAG, "fetchAllFriends Over") ;
	      
	      String[] from = new String[] {mDbHelper.KEY_NAME};
	      int[] to = new int[] { android.R.id.text1 };
	      
	      //attaching a footer view to the list displayed
	      View footer = getLayoutInflater().inflate(R.layout.footer, null);
	      final ListView listView = getListView();
	      
	      //attaching a footer to the listview before setting the adapter
	      listView.addFooterView(footer);
	      
	   // Now create an  adapter and set it to display using our row
	        SimpleCursorAdapter friends =
	            new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, c, from, to);
	        
	        setListAdapter(friends);
	        
	        
 	        listView.setItemsCanFocus(false);
	        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        
	        //Button btn;
		    //btn = (Button)findViewById(R.id.selectedFriends);
	        
	        mDbHelper.close();
	        
	        
}
}
