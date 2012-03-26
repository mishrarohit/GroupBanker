package me.rohitmishra.groupbanker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//This class is for handling the database operations to store the details of the payment details between two people

public class overviewDbAdapter {

private static final String TAG = "OverviewDbAdapter" ;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_USERID1 = "userId1";
	public static final String KEY_USERID2 = "userId2";
	public static final String KEY_AMOUNT = "amount";
	
	
	private DatabaseHelper mDbHelper ;
	private SQLiteDatabase mDb ;
	
	// Database creation SQL statement moved to DbAdapter
	private static final String TABLE_NAME = "overview" ;
	private final Context mCtx ;
	
			
			private static class DatabaseHelper extends SQLiteOpenHelper {
				
				DatabaseHelper(Context context)	{
					super(context, DbAdapter.DATABASE_NAME, null, DbAdapter.DATABASE_VERSION) ;
				}
				
				@Override
				public void onCreate(SQLiteDatabase db)	{
					//db.execSQL(DATABASE_CREATE);
				}
				
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
					/*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		                    + newVersion + ", which will destroy all old data");
		            db.execSQL("DROP TABLE IF EXISTS notes");
		            onCreate(db);*/
				}
			}
			
			/**
		     * Constructor - takes the context to allow the database to be
		     * opened/created
		     * 
		     * @param ctx the Context within which to work
		     */
		    public overviewDbAdapter(Context ctx) {
		        this.mCtx = ctx;
		    }
		    
		    /**
		     * Open the groupbanker database. If it cannot be opened, try to create a new
		     * instance of the database. If it cannot be created, throw an exception to
		     * signal the failure
		     * 
		     * @return this (self reference, allowing this to be chained in an
		     *         initialization call)
		     * @throws SQLException if the database could be neither opened or created
		     */
		    public overviewDbAdapter open() throws SQLException {
		        mDbHelper = new DatabaseHelper(mCtx);
		        mDb = mDbHelper.getWritableDatabase();
		        return this;
		    }

		    public void close() {
		        mDbHelper.close();
		    }
		    
		    /**
		     * Create a new entry for a pair of friends involved in a transaction. If the transaction is successfully 
		     * created, return the new rowId for the transaction, otherwise return -1 to indicate
		     * failure. 
		     * 
		     *  @param fbid1 the facebook id of the first person
		     *  @param fbid2 the facebook ID of the second person 
		     *  @param amount the amount involved among them
		     *  @return rowId or -1 if failed
		     */
		    
		    public long createOverview(String userId1, String userId2, float amount)	{
		    	ContentValues initialValues = new ContentValues();
		    	initialValues.put(KEY_USERID1, userId1);
		    	initialValues.put(KEY_USERID2, userId2);
		    	initialValues.put(KEY_AMOUNT, amount);
		    			    	
		    	return mDb.insert(TABLE_NAME, null, initialValues) ;
		    }
		    
		    /**
		     * Update the amount involved between two friends. 
		    
		     @param fbid1 the facebook id of the first person
		     *  @param fbid2 the facebook ID of the second person 
		     *  @param amount the amount involved among them
		     * @return true if the note was successfully updated, false otherwise
		     */
		    public long updateAmount(String userId1, String userId2, float amt) {
		        
		    	/* first we have to check if user1 and user2 combination exists in the table
		         * if yes => update the entry with the incoming amount
		         * if no => call createOveriew function and create a new entry
		         * in both cases we need the id of the row updated or created*/
		    	
		    	String selection = "userId1 = ? AND userId2 = ?";
		    	Cursor c =  mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_USERID1, KEY_USERID2, KEY_AMOUNT}, selection, new String[]{userId1, userId2}, null, null, null);
		    			    	
		    	//if cursor has non zero rows the tuple does not exists moveToFirst returns false
		    	if (!c.moveToFirst())	{
		    		
		    		long id = createOverview(userId1, userId2, amt);
		    		c.close();
		    		return id;
		    	}
		    	
		    	//else update the existing record and update and enter the consolidated amount
		    	else {
		    		
		    		c.moveToFirst();
			    	float previousAmt;
			    	previousAmt = c.getFloat(c.getColumnIndex(KEY_AMOUNT));
			    	amt = previousAmt + amt;
			    	Log.v(TAG, "The updated amount value = " + amt);
			    	ContentValues args = new ContentValues();
			        args.put(KEY_AMOUNT, amt);
			        mDb.update(TABLE_NAME, args,  selection, new String[] {userId1, userId2});
			        long id = c.getLong(c.getColumnIndexOrThrow(KEY_ROWID));
			        c.close();
			        return id;
		       
		    }
		    	
		  }
}
