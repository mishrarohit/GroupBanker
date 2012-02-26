package me.rohitmishra.groupbanker;
	
import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


// TODO Rename this class as SelectFriendsActivity

public class SelectFriends extends ListActivity{
	
	private FriendsDbAdapter mDbHelper;
	private static final String TAG = "SelectFriends";
	private EditText filterText ;
	private SelectFriendsAdapter adapter ;
	private Cursor c;
	private Context context ;
	
	/* http://stackoverflow.com/questions/4188818/java-best-way-to-implement-a-dynamic-size-array-of-objects
	 * as per this discussion, arraylists are not good performance-wise compared to traditional functions. 
	 * We need to look into this. 
	 */
	
	/* http://stackoverflow.com/questions/9399941/implementing-a-listview-with-multiple-select-with-filter-using-a-cursor-adapter/
	 * using the answer to my own SO question. 
	 */
	static ArrayList<Boolean> checkedStates = new ArrayList<Boolean>();
	static HashSet<String> selectedIds = new HashSet<String>();
	static HashSet<Integer> selectedLines = new HashSet<Integer>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.selectfriends);
		  
		  Log.v(TAG, "onCreate called") ;
		  
		  context = this.getApplicationContext() ;
		  
		  mDbHelper = new FriendsDbAdapter(this);
		  
	      mDbHelper.open() ;
	     
	      Log.v(TAG, "database opened") ;
	      
	      c = mDbHelper.fetchAllFriends();
	     // startManagingCursor(c);
	      
	      Log.v(TAG, "fetchAllFriends Over") ;
	     
	      
	      String[] from = new String[] {mDbHelper.KEY_NAME};
	      int[] to = new int[] { R.id.text1 };
	      
	      final ListView listView = getListView();
	      Log.d(TAG, "Got listView");
	      
	   // Now initialize the  adapter and set it to display using our row
	       adapter =
	    	   new SelectFriendsAdapter(this, R.layout.selectfriendsrow, c, from, to);
	        
	       Log.d(TAG, "we have got an adapter");
	     // Initialize the filter-text box 
		 //Code adapted from http://stackoverflow.com/questions/1737009/how-to-make-a-nice-looking-listview-filter-on-android
		      
	       filterText = (EditText) findViewById(R.id.filtertext) ;
	       filterText.addTextChangedListener(filterTextWatcher) ;
	        
	     /* Set the FilterQueryProvider, to run queries for choices
	     * that match the specified input.
		 *	Code adapted from http://stackoverflow.com/questions/2002607/android-how-to-text-filter-a-listview-based-on-a-simplecursoradapter
		 */
		/*        
	       adapter.setFilterQueryProvider(new FilterQueryProvider() {
	    	   	public Cursor runQuery(CharSequence constraint) {
	    	   		
	    	   		try {
	    	   		// Search for friends whose names begin with the specified letters.
	    	   		Log.v(TAG, "runQuery Constraint = " + constraint) ;
	    	   		//String selection = mDbHelper.KEY_NAME + " LIKE '%"+constraint+"%'";
	    		      
	    	   		mDbHelper = new FriendsDbAdapter(context);
	    	   		mDbHelper.open();
	    	   		
	    	   		
	    	   		Cursor cur = mDbHelper.fetchFriendsWithSelection(
                    (constraint != null ? constraint.toString() : null));
	    	   		
	    	   		Log.d(TAG, "runQuery cursor is " + cur + " has " + cur.getCount() + " rows  " +
	    	   				"cursor is closed = " + cur.isClosed());
	    	   		
	    	   		Cursor c1 = adapter.getCursor() ;
	    	   		
	    	   		Log.d(TAG, "adapter's cursor is " + c1 + " isClosed is " + c1.isClosed());
	    	   		
	    	   		//adapter.changeCursor(cur) ;
	    	   		
	    	   		return cur;
	    	   		} catch (Exception e)	{
	    	   			Log.e(TAG, "runQuery Exception = " + e);
	    	   			Cursor cur = null ;
	    	   			return cur ;
	    	   		}
	    	   		
         }
     });
    */
	       setListAdapter(adapter);
        
	       Log.d(TAG, "setListAdapter worked") ;
        
        
	       listView.setItemsCanFocus(false);
	       listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	       listView.setFastScrollEnabled(true);
        
	       Button btn;
	       btn = (Button)findViewById(R.id.buttondone);
        
	       // mDbHelper.close();
        
	  }
	
	@Override
	protected void onStop() {
		try{
			Log.d(TAG, "in onStop");
			super.onStop();
			
			if(this.adapter != null)	{
				this.adapter.getCursor().close();
				this.adapter = null ;
			}
			
			if(this.c != null)	{
				this.c.close();
			}
			
			if(this.mDbHelper.mDb != null)	{
				this.mDbHelper.mDb.close();
			}
			
			if(this.mDbHelper != null)	{
				this.mDbHelper.close() ;
			}
		} catch (Exception error)	{
			Log.e(TAG, "We have an exception = " + error);
		}
	}
		 
			
		@Override	
		protected void onListItemClick(ListView parent, View v, int position, long id) {
			
			//gets the Bookmark ID of selected position
             Cursor cursor = (Cursor)parent.getItemAtPosition(position);
             String bookmarkID = cursor.getString(0);
             
             Log.d(TAG, "mListener -> bookmarkID = " + bookmarkID);
             
             Log.d(TAG, "mListener -> position = " + position);

             if (!selectedIds.contains(bookmarkID)) {

            	    selectedIds.add(bookmarkID);
            	    selectedLines.add(position);


            	} else {

            	     selectedIds.remove(bookmarkID);
            	     selectedLines.remove(position);



            	 if (selectedIds.isEmpty()) {
            	    //clear everything
            	        selectedIds.clear();
            	        checkedStates.clear();      
            	        selectedLines.clear();

            	        //refill checkedStates to avoid force close bug - out of bounds
            	        if (cursor.moveToFirst()) {
            	            while (!cursor.isAfterLast()) {    
            	                SelectFriends.checkedStates.add(false);

            	                cursor.moveToNext();
            	            }
            	        }                       

            	 }
         }
	}
	
	
	  private TextWatcher filterTextWatcher = new TextWatcher() {
		  
		  public void afterTextChanged(Editable s)	{
			  
		  }
		  
		  public void beforeTextChanged(CharSequence s, int start, int count, int after)	{
			  
		  }
		  
		  public void onTextChanged(CharSequence s, int start, int before, int count)	{
			  Log.v(TAG, "onTextChanged called. s = " + s);
			  adapter.getFilter().filter(s) ;
		  }
	  };
	  
	  @Override
	  protected void onDestroy()	{
		  	super.onDestroy();
		  	filterText.removeTextChangedListener(filterTextWatcher);
		  
	  }	  
}
