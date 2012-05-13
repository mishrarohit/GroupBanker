package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	
	private static final String TAG = "DbAdapter" ;
	
	public static final String DATABASE_NAME = "groupbanker" ;
	public static final int DATABASE_VERSION = 1;
	
	//create table string for friends
	
	private static final String CREATE_TABLE_FRIENDS = 
			"create table friends (_id integer primary key autoincrement, " + 
			"fbid text not null, name text not null);" ;
	
	//create table string for transaction
	
	private static final String CREATE_TABLE_TRANS = 
			"create table trans (_id integer primary key autoincrement, " + 
			"amount real not null, description text not null, time text not null);" ;

	//create table string for overview
	
	private static final String CREATE_TABLE_OVERVIEW =
			"create table overview (_id integer primary key autoincrement, " + 
			"userId1 text not null, userId2 text not null, amount real, " +
			"FOREIGN KEY(userId1) REFERENCES friends(_id), " +
			"FOREIGN KEY(userId2) REFERENCES friends(_id));" ;
	
	//create table string for details
	
	private static final String CREATE_TABLE_DETAILS = 
			"create table details (_id integer primary key autoincrement, " + 
			"transID integer not null, fbid text not null, toPay real , paid real , " +
			"FOREIGN KEY(transID) REFERENCES trans(_id), " +
			"FOREIGN KEY(fbid) REFERENCES friends(fbid));" ;
	
	//create table string for info 
	
	private static final String CREATE_TABLE_INFO = 
		"create table info (_id integer primary key autoincrement, " +
		"transID integer not null, userID1 integer not null, userID2 integer not null, amount real, " +
		"FOREIGN KEY(transID) REFERENCES trans(_id)," +
		"FOREIGN KEY(userID1) REFERENCES friends(fbid), " +
		"FOREIGN KEY(userID2) REFERENCES friends(fbid));" ;
	
	
	
	 private final Context context; 
	 private DatabaseHelper DBHelper;
	 private SQLiteDatabase db;

	    /**
	     * Constructor
	     * @param ctx
	     */
	    public DbAdapter(Context ctx)
	    {
	        this.context = ctx;
	        this.DBHelper = new DatabaseHelper(this.context);
	    }

	    private static class DatabaseHelper extends SQLiteOpenHelper 
	    {
	        DatabaseHelper(Context context) 
	        {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) 
	        {
	            db.execSQL(CREATE_TABLE_FRIENDS);
	            db.execSQL(CREATE_TABLE_TRANS);
	            db.execSQL(CREATE_TABLE_OVERVIEW);
	            db.execSQL(CREATE_TABLE_DETAILS);
	            db.execSQL(CREATE_TABLE_INFO);
	        }
	        
	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	        int newVersion) 
	        {               
	        	Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS friends");
	            db.execSQL("DROP TABLE IF EXISTS trans");
	            db.execSQL("DROP TABLE IF EXISTS overview");
	            db.execSQL("DROP TABLE IF EXISTS details");
	            db.execSQL("DROP TABLE IF EXISTS info");
	            onCreate(db);
	        }
	    } 

	   /**
	     * open the db
	     * @return this
	     * @throws SQLException
	     * return type: DBAdapter
	     */
	    public DbAdapter open() throws SQLException 
	    {
	        this.db = this.DBHelper.getWritableDatabase();
	        return this;
	    }

	    /**
	     * close the db 
	     * return type: void
	     */
	    public void close() 
	    {
	        this.DBHelper.close();
	    }
}


