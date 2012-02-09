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
	public static final String KEY_FBID1 = "fbid1";
	public static final String KEY_FBID2 = "fbid2";
	public static final String KEY_AMOUNT = "amount";
	
	
	private DatabaseHelper mDbHelper ;
	private SQLiteDatabase mDb ;
	
	// Database creation SQL statement 
	
		private static final String DATABASE_CREATE = 
				"create table overview (_id integer autoincrement, " + 
				"fbid1 text not null, fbid2 text not null, amount real, " +
				"PRIMARY KEY(fbid1, fbid2), " +
				"FOREIGN KEY(fbid1) REFERENCES friends(fbid), " +
				"FOREIGN KEY(fbid2) REFERENCES friends(fbid));" ;
			
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
		    
		    public long createTransaction(String fbid1, String fbid2, float amount)	{
		    	ContentValues initialValues = new ContentValues();
		    	initialValues.put(KEY_FBID1, fbid1);
		    	initialValues.put(KEY_FBID2, fbid2);
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
		    public boolean updateAmount(String fbid1, String fbid2, float amt) {
		        ContentValues args = new ContentValues();
		        args.put(KEY_AMOUNT, amt);

		        return mDb.update(TABLE_NAME, args, KEY_FBID1 + "=" + fbid1 + "AND" + KEY_FBID2 + "=" + fbid2, null) > 0;
		    }
}
