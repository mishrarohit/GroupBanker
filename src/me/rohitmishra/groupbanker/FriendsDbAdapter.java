package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// This class is for handling the database operations involving friends

public class FriendsDbAdapter {
	private static final String TAG = "FriendsDbAdapter" ;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_FBID = "fbid";
	public static final String KEY_NAME = "name";
	// public static final String KEY_IMAGEURI = "imageuri" ;
	
	public DatabaseHelper mDbHelper ;
	public SQLiteDatabase mDb ;
	//removing the create string as it will be moved to main DB Adapter class
		
	private static final String TABLE_NAME = "friends" ; 
		
	private final Context mCtx ;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		DatabaseHelper(Context context)	{
			super(context, DbAdapter.DATABASE_NAME, null, DbAdapter.DATABASE_VERSION) ;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)	{
			//Log.v(TAG, "onCreate of FriendsDb Adapter called");
			//db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)	{
			/*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS friends");
            onCreate(db);*/
		}
	}
		
	/**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public FriendsDbAdapter(Context ctx) {
        this.mCtx = ctx;
        
    }
    
    /**
     * Open the friends database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public FriendsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Create a new friend using the fbid, name and imageuri. If the note is successfully 
     * created, return the new rowId for the friend, otherwise return -1 to indicate
     * failure. 
     * 
     *  @param fbid the fbid of the friend
     *  @param name the name of the friend
     *  @param (disabled now) imageuri the uri of the friend's image 
     *  @return rowId or -1 if failed
     */
    
    public long createFriend(String fbid, String name)	{
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_FBID, fbid);
    	initialValues.put(KEY_NAME, name);
    //	initialValues.put(KEY_IMAGEURI, imageuri);
    	
    	return mDb.insert(TABLE_NAME, null, initialValues) ;
    }

// TODO Code to updateFriend, fetchFriend and deleteFriend
    
    /**
     * Implementing fetchAllFriends()
     * Return a Cursor over the list of all friends (their names) in the database
     * 
     * @return Cursor over all friends' names
     */
    
    public Cursor fetchAllFriends() throws SQLException {
    	
    	return mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_FBID, KEY_NAME}, null, null, null, null, null);
    	
    }
    
    public Cursor fetchFriendsWithSelection(String constraint) throws SQLException	{
    	//try {
    	Log.d(TAG, "fetchFriendsWithSelection called. constraint = " + constraint + 
    			" mDb = " + mDb );
    	String selection = KEY_NAME + " LIKE '%"+constraint+"%'";
    	Cursor c =  mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_FBID, KEY_NAME}, selection, null, null, null, null);
    	Log.d(TAG, "fetchFriendsWithSelection Cursor = " + c + " has " + c.getCount()) ;
    	return c ;
    /*	} catch ( Exception e)	{
    		Log.e(TAG, "fetchFriendsWithSelection exception " + e);
    		Cursor c = null ;
    		return c ;
    	}  */
    }
    
    public String fetchFriendName(String selectedID) throws SQLException	{
    	String selection = KEY_ROWID + " = " + selectedID ;
    	Cursor c = mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_FBID, KEY_NAME}, selection, null, null, null, null);
    	if(!c.moveToFirst())	{
    		
    		c.close();
    		return "0";
    	}
    	else {
    	Log.d(TAG, "fetchFriendName cursor count = " + c.getCount());
    	String name =  c.getString(c.getColumnIndexOrThrow(KEY_NAME));
    	c.close();
    	return name ;
    	}
    }
   
}
	
	
	
	

