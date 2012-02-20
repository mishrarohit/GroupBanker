package me.rohitmishra.groupbanker;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;

// TODO Rename this class as SelectFriendsActivity

public class SelectFriends extends ListActivity{
	
	private FriendsDbAdapter mDbHelper;
	private static final String TAG = "SelectFriends";
	private EditText filterText ;
	private SimpleCursorAdapter adapter ;
	
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.list);
		  
		  Log.v(TAG, "onCreate called") ;
		  
		  mDbHelper = new FriendsDbAdapter(this);
	      mDbHelper.open() ;
	      
	      // Initialize the filter-text box 
	      // Code adapted from http://stackoverflow.com/questions/1737009/how-to-make-a-nice-looking-listview-filter-on-android
	      
	      filterText = (EditText) findViewById(R.id.filtertext) ;
	      filterText.addTextChangedListener(filterTextWatcher) ;
	     
	     
	      
	      Log.v(TAG, "database opened") ;
	      
	      Cursor c = mDbHelper.fetchAllFriends();
	      startManagingCursor(c);
	      
	      Log.v(TAG, "fetchAllFriends Over") ;
	      
	      String[] from = new String[] {mDbHelper.KEY_NAME};
	      int[] to = new int[] { android.R.id.text1 };
	      
	      final ListView listView = getListView();
	      
	      
	   // Now initialize the  adapter and set it to display using our row
	        adapter =
	            new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, c, from, to);
	        
     
	        
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
        
        
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        Button btn;
	    btn = (Button)findViewById(R.id.buttondone);
        
        mDbHelper.close();
        
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
