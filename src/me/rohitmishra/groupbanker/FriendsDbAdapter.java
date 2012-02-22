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
	public static final String KEY_IMAGEURI = "imageuri" ;
	
	private DatabaseHelper mDbHelper ;
	private SQLiteDatabase mDb ;
	
	// Database creation SQL statement 
	
	private static final String DATABASE_CREATE = 
		"create table friends (_id integer primary key autoincrement, " + 
		"fbid text not null, name text not null, imageuri text not null);" ;
	
	private static final String DATABASE_NAME = "groupbanker" ;
	private static final String TABLE_NAME = "friends" ; 
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
     *  @param imageuri the uri of the friend's image
     *  @return rowId or -1 if failed
     */
    
    public long createFriend(String fbid, String name, String imageuri)	{
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_FBID, fbid);
    	initialValues.put(KEY_NAME, name);
    	initialValues.put(KEY_IMAGEURI, imageuri);
    	
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
    	
    	return mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_FBID, KEY_NAME, KEY_IMAGEURI}, null, null, null, null, null);
    	
    }
    
    public Cursor fetchFriendsWithSelection(String constraint) throws SQLException	{
    	Log.d(TAG, "fetchFriendsWithSelection called. constraint = " + constraint);
    	String selection = KEY_NAME + " LIKE '%"+constraint+"%'";
    	return mDb.query(TABLE_NAME,new String[] {KEY_ROWID, KEY_FBID, KEY_NAME, KEY_IMAGEURI}, selection, null, null, null, null);
    }
   
}
	
	
	
	

