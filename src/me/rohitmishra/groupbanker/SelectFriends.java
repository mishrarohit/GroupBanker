package me.rohitmishra.groupbanker;
	
import java.util.ArrayList;
import java.util.HashSet;

import me.rohitmishra.groupbanker.SelectFriends.FriendsAdapter.ViewHolder;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


// TODO Rename this class as SelectFriendsActivity

public class SelectFriends extends ListActivity{
	
	private FriendsDbAdapter mDbHelper;
	private static final String TAG = "SelectFriends";
	private EditText filterText ;
	private FriendsAdapter adapter ;
	
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
		  
		  checkedStates = new ArrayList<Boolean>() ;
		  selectedIds = new HashSet<String>() ;
		  selectedLines = new HashSet<Integer>()  ;
		  
		  mDbHelper = new FriendsDbAdapter(this);
	      mDbHelper.open() ;
	     
	      Log.v(TAG, "database opened") ;
	      
	      Cursor c = mDbHelper.fetchAllFriends();
	      // startManagingCursor(c);
	      
	      Log.v(TAG, "fetchAllFriends Over") ;
	     
	      
	      String[] from = new String[] {mDbHelper.KEY_NAME};
	      int[] to = new int[] { R.id.text1 };
	      
	      final ListView listView = getListView();
	      Log.d(TAG, "Got listView");
	      
	   // Now initialize the  adapter and set it to display using our row
	       adapter =
	    	   new FriendsAdapter(this, R.layout.selectfriendsrow, c, from, to);
	        
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
	    	   		//String selection = mDbHelper.KEY_NAME + " LIKE '%"+constraint+"%'";
            
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
			  adapter.getFilter().filter(s);
		  }
	  };
	  
	  @Override
	  protected void onDestroy()	{
		  	super.onDestroy();
		  	filterText.removeTextChangedListener(filterTextWatcher);
	  }
	  
	  
	  
	  /* Using the code at 
	   * http://codereview.stackexchange.com/questions/1057/android-custom-cursoradapter-design
	   * mixed with code at 
	   * http://stackoverflow.com/questions/9399941/implementing-a-listview-with-multiple-select-with-filter-using-a-cursor-adapter/
	   * for creating a custom SimpleCursorAdapter for SelectFriends.java
	   */
	  public static class FriendsAdapter extends SimpleCursorAdapter {

	  	private static final String TAG = "FriendsSimpleCursorAdapter";
	  	private final Context context ;
	  	private final String[] values ;
	  	private final int[] to ;
	  	private final int layout ;
	  	private final Cursor cursor ;
	  	private final LayoutInflater mInflater ;
	  
	  	static class ViewHolder	{
	  		public CheckedTextView checkedText ;
	  	}
	  	
	  	public FriendsAdapter(Context context, int layout, Cursor c,
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
	  	
	  	 
	  	/*
	  	@Override
	     public View newView(Context context, Cursor cursor, ViewGroup parent) {
	         final View view=mInflater.inflate(layout,parent,false); 
	         return view;
	     }
	  	
	  	
	  	@Override
	  	public void bindView(View view, Context context, Cursor cursor)	{
	  		super.bindView(view, context, cursor);
	  		
	  		Log.d(TAG, "At the start of bindView." ) ;
	  		ViewHolder holder = (ViewHolder) view.getTag();
	  		if(holder == null)	{
	  			Log.d(TAG, "holder = null");
	  			holder = new ViewHolder();
	  			
	  			holder.checkedText = (CheckedTextView) view.findViewById(R.id.text1) ;
	  			view.setTag(holder);
	  			
	  		}
	  		
	  		holder.checkedText.setText(cursor.getString(to[0]));
	  		
	  		
	  		// fill the checkedStates array with amount of bookmarks (prevent OutOfBounds Force close)
	        if (cursor.moveToFirst()) {
	            while (!cursor.isAfterLast()) {  
	                SelectFriends.checkedStates.add(false);
	                cursor.moveToNext();
	            }
	        }
	        
	        String bookmarkID = cursor.getString(0);
	        //CheckedTextView markedItem = (CheckedTextView) row.findViewById(R.id.btitle);
	        if (SelectFriends.selectedIds.contains(new String(bookmarkID))) {
	            holder.checkedText.setChecked(true);
	            SelectFriends.selectedLines.add(object)

	        } else {
	            markedItem.setChecked(false);
	            MainActivity.selectedLines.remove(pos);
	        }
	  		
	  		Log.d(TAG, "At the end of rowView");
	  		return ;
	  		
	  	}
	  	*/
	  	
	  	@Override
	  	public View getView(int position, View convertView, ViewGroup parent)   {
	  	    super.getView(position, convertView, parent);
	  	    Log.d(TAG, "In the getView method of FriendsAdapter");
	  		Log.d(TAG, "At the start of rowView. position = " + position) ;
	  	    View rowView = convertView ;
	  	    if(rowView == null) {
	  	        Log.d(TAG, "rowView = null");
	  	        try {
	  	        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	  	    // fill the checkedStates array with amount of bookmarks (prevent OutOfBounds Force close)
	  	    try {
	  	    if (cursor.moveToFirst()) {
	  	    	Log.d(TAG, "moveToFirst worked");
	            while (!cursor.isAfterLast()) {  
	                SelectFriends.checkedStates.add(false);
	                cursor.moveToNext();
	            }
	        }
	  	    } catch (Exception e)
	  	    {
	  	    	Log.e(TAG, "exception in try block on 305 = " + e);
	  	    }
	  	 
	        cursor.moveToPosition(position);
	        Log.d(TAG, "Cursor position = " + cursor.getPosition());
	        
	        String bookmarkID = cursor.getString(0);
	        Log.d(TAG, "bookmarkID = " + bookmarkID );
	        
	        //CheckedTextView markedItem = (CheckedTextView) row.findViewById(R.id.btitle);
	        if (SelectFriends.selectedIds.contains(new String(bookmarkID))) {
	            holder.checkedText.setChecked(true);
	            SelectFriends.selectedLines.add(position);

	        } else {
	            holder.checkedText.setChecked(false);
	            SelectFriends.selectedLines.remove(position);
	        }
	        //int nameCol = cursor.getColumnIndex(FriendsDbAdapter.KEY_NAME) ;
	  	    //String name = cursor.getString(nameCol);
	  	    Log.d(TAG, "to[0] = " + to[0]);
	        holder.checkedText.setText(cursor.getString(cursor.getColumnIndex(FriendsDbAdapter.KEY_NAME)));

	  	    Log.d(TAG, "At the end of rowView");
	  	    return rowView;

	  	}

	  }
	  
}
