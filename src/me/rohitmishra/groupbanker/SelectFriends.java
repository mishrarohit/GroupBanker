package me.rohitmishra.groupbanker;
	
import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


// TODO Rename this class as SelectFriendsActivity

public class SelectFriends extends ListActivity{
	
	private FriendsDbAdapter mDbHelper;
	private static final String TAG = "SelectFriends";
	private EditText filterText ;
	private FriendsSimpleCursorAdapter adapter ;
	
	/* http://stackoverflow.com/questions/4188818/java-best-way-to-implement-a-dynamic-size-array-of-objects
	 * as per this discussion, arraylists are not good performance-wise compared to traditional functions. 
	 * We need to look into this. 
	 */
	protected ArrayList<String> selectedIds;
	protected ArrayList<Integer> selectedLines;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.selectfriends);
		  
		  Log.v(TAG, "onCreate called") ;
		  
		  selectedIds = new ArrayList<String>() ;
		  selectedLines = new ArrayList<Integer>()  ;
		  
		  mDbHelper = new FriendsDbAdapter(this);
	      mDbHelper.open() ;
	     
	      Log.v(TAG, "database opened") ;
	      
	      Cursor c = mDbHelper.fetchAllFriends();
	      startManagingCursor(c);
	      
	      Log.v(TAG, "fetchAllFriends Over") ;
	     
	      
	      String[] from = new String[] {mDbHelper.KEY_NAME};
	      int[] to = new int[] { R.id.text1 };
	      
	      final ListView listView = getListView();
	      Log.d(TAG, "Got listView");
	      
	   // Now initialize the  adapter and set it to display using our row
	       adapter =
	    	   new FriendsSimpleCursorAdapter(this, R.layout.selectfriendsrow, c, from, to);
	        
	       Log.d(TAG, "we have got an adapter");
	     // Initialize the filter-text box 
		 //Code adapted from http://stackoverflow.com/questions/1737009/how-to-make-a-nice-looking-listview-filter-on-android
		      
	       filterText = (EditText) findViewById(R.id.filtertext) ;
	       filterText.addTextChangedListener(filterTextWatcher) ;
	        
	     /* Set the FilterQueryProvider, to run queries for choices
	     * that match the specified input.
		 *	Code adapted from http://stackoverflow.com/questions/2002607/android-how-to-text-filter-a-listview-based-on-a-simplecursoradapter
		 */
		        
	       adapter.setFilterQueryProvider(new FilterQueryProvider() {
	    	   	public Cursor runQuery(CharSequence constraint) {
	    	   		// Search for friends whose names begin with the specified letters.
	    	   		Log.v(TAG, "runQuery Constraint = " + constraint) ;
	    	   		String selection = mDbHelper.KEY_NAME + " LIKE '%"+constraint+"%'";
            
	    	   		mDbHelper.open(); 
	    	   		Cursor c = mDbHelper.fetchFriendsWithSelection(
                     (constraint != null ? constraint.toString() : null));
	    	   		return c;
         }
     });
     
     
     
	        
	       setListAdapter(adapter);
        
	       Log.d(TAG, "setListAdapter worked") ;
        
        
	       listView.setItemsCanFocus(false);
	       listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
	       // listView.setOnItemClickListener(mListener);
        
	       Button btn;
	       btn = (Button)findViewById(R.id.buttondone);
        
	       mDbHelper.close();
        
	  }
		 
			
		@Override	
		protected void onListItemClick(ListView parent, View v, int position, long id) {

			String item = (String) getListAdapter().getItem(position);
			Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
			
			//gets the Bookmark ID of selected position
             Cursor cursor = (Cursor)parent.getItemAtPosition(position);
             String bookmarkID = cursor.getString(0);
             
             Log.d(TAG, "mListener -> bookmarkID = " + bookmarkID);
             
             Log.d(TAG, "mListener -> position = " + position);

     //        boolean currentlyChecked = checkedStates.get(position);
     //        checkedStates.set(position, !currentlyChecked);

             
             if (!selectedIds.contains(bookmarkID)) {

                 selectedIds.add(bookmarkID);
                 selectedLines.add(position);

             } else {

                 selectedIds.remove(bookmarkID);
                 selectedLines.remove(position);


                 }

         }
    
	
	
	  private TextWatcher filterTextWatcher = new TextWatcher() {
		  
		  public void afterTextChanged(Editable s)	{
			  
		  }
		  
		  public void beforeTextChanged(CharSequence s, int start, int count, int after)	{
			  
		  }
		  
		  public void onTextChanged(CharSequence s, int start, int before, int count)	{
			  Log.v(TAG, "onTextChanged called. s = " + s);
			  adapter.getFilter().filter(s);
		  }
	  };
	  
	  @Override
	  protected void onDestroy()	{
		  	super.onDestroy();
		  	filterText.removeTextChangedListener(filterTextWatcher);
	  }
	  
}
