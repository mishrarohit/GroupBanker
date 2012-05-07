package me.rohitmishra.groupbanker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//This class is for handling the database operations to store the details of the payment details between two people

public class InfoDbAdapter {

private static final String TAG = "InfoDbAdapter" ;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_USERID1 = "userID1";
	public static final String KEY_USERID2 = "userID2";
	public static final String KEY_TRANSID = "transID";
	public static final String KEY_AMOUNT = "amount";
	
	
	private DatabaseHelper mDbHelper ;
	private SQLiteDatabase mDb ;
	
	// Database creation SQL statement moved to DbAdapter
	private static final String TABLE_NAME = "info" ;
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
    public InfoDbAdapter(Context ctx) {
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
    public InfoDbAdapter open() throws SQLException {
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
     *  @param tansID the transaction ID 
     *  @param fbid1 the facebook id of the first person
     *  @param fbid2 the facebook ID of the second person 
     *  @param amount the amount involved among them
     *  @return rowId or -1 if failed
     */
    
    public long createInfo(int transID, String userID1, String userID2, float amount)	{
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_TRANSID, transID);
    	initialValues.put(KEY_USERID1, userID1);
    	initialValues.put(KEY_USERID2, userID2);
    	initialValues.put(KEY_AMOUNT, amount);
    			    	
    	return mDb.insert(TABLE_NAME, null, initialValues) ;
    }
    
    public Cursor fetchTransIDAmount(String userID1, String userID2)	{
    	String selection = KEY_USERID1 + "=" + userID1 + " AND " + KEY_USERID2 + "=" + userID2 ;
    	Cursor c =  mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_TRANSID, KEY_AMOUNT}, selection, null, null, null, null);
    	Log.d(TAG, "fetchTransIDAmount Cursor = " + c + " has " + c.getCount()) ;
    	return c ;
    }
 
}
