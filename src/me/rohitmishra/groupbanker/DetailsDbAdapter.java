package me.rohitmishra.groupbanker;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// This class is for handling the database operations to store the details of the individual transactions

public class DetailsDbAdapter {
	
private static final String TAG = "DetailsDbAdapter" ;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TRANSID = "transID";
	public static final String KEY_FBID = "fbid";
	public static final String KEY_TOPAY = "toPay";
	public static final String KEY_PAID = "paid";
	
	private DatabaseHelper mDbHelper ;
	private SQLiteDatabase mDb ;
	
	// Database creation SQL statement 
	
	private static final String DATABASE_CREATE = 
			"create table details (_id integer primary key autoincrement, " + 
			"transID integer not null, fbid text not null, toPay real , paid real , " +
			"FOREIGN KEY(transID) REFERENCES transaction(_id), " +
			"FOREIGN KEY(fbid) REFERENCES friends(fbid));" ;
		
		private static final String DATABASE_NAME = "groupbanker" ;
		private static final String TABLE_NAME = "transaction" ;
		private static final int DATABASE_VERSION = 1 ;
		
		private final Context mCtx ;
		
private static class DatabaseHelper extends SQLiteOpenHelper {
			
			DatabaseHelper(Context context)	{
				super(context, DATABASE_NAME, null, DATABASE_VERSION) ;
			}
			
			@Override
			public void onCreate(SQLiteDatabase db)	{
				db.execSQL(DATABASE_CREATE);
			}
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
				Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS notes");
	            onCreate(db);
			}
		}
		
		/**
	     * Constructor - takes the context to allow the database to be
	     * opened/created
	     * 
	     * @param ctx the Context within which to work
	     */
	    public DetailsDbAdapter(Context ctx) {
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
	    public DetailsDbAdapter open() throws SQLException {
	        mDbHelper = new DatabaseHelper(mCtx);
	        mDb = mDbHelper.getWritableDatabase();
	        return this;
	    }

	    public void close() {
	        mDbHelper.close();
	    }
	    
	    /**
	     * Create a new transaction detail. If the transaction is successfully 
	     * created, return the new rowId for the transaction, otherwise return -1 to indicate
	     * failure. 
	     * 
	     *  @param transID the ID of the transaction in question
	     *  @param fbid the facebook ID of the person involved in the transaction
	     *  @param toPay the amount to be paid by that particular person 
	     *  @param paid the amount paid by the person
	     *  @return rowId or -1 if failed
	     */
	    
	    public long createTransaction(int transID, String fbid, float toPay, float paid)	{
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put(KEY_TRANSID, transID);
	    	initialValues.put(KEY_FBID, fbid);
	    	initialValues.put(KEY_TOPAY, toPay);
	    	initialValues.put(KEY_PAID, paid);
	    	
	    	return mDb.insert(TABLE_NAME, null, initialValues) ;
	    }
	    
	    /**
	     * Update the detail of a transaction to indicate the amount  topay. The note to be updated is
	     * specified using the transID and fbid of the person, and it is altered to modify the value of column toPay 
	     * 
	     * @param transID  id of transaction to update
	     * @param fbid id of the person paying the value
	     * @param topay value of the amount to be paid
	     * @return true if the note was successfully updated, false otherwise
	     */
	    public boolean updateToPay(int transID, String fbid, float toPay) {
	        ContentValues args = new ContentValues();
	        args.put(KEY_TOPAY, toPay);

	        return mDb.update(TABLE_NAME, args, KEY_TRANSID + "=" + transID + "AND" + KEY_FBID + "=" + fbid, null) > 0;
	    }
	    
	    /**
	     * Update the detail of a transaction to indicate the amount  paid. The note to be updated is
	     * specified using the transID and fbid of the person, and it is altered to modify the value of column paid 
	     * 
	     * @param transID  id of transaction to update
	     * @param fbid id of the person paying the value
	     * @param paid value of the amount already paid
	     * @return true if the note was successfully updated, false otherwise
	     */
	    public boolean updatePaid(int transID, String fbid, float paid) {
	        ContentValues args = new ContentValues();
	        args.put(KEY_PAID, paid);

	        return mDb.update(TABLE_NAME, args, KEY_TRANSID + "=" + transID + "AND" + KEY_FBID + "=" + fbid, null) > 0;
	    }

}
