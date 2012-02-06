package me.rohitmishra.groupbanker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

public class FriendsList extends Activity  {
	
	private static final String TAG = "FriendsList";
    private Handler mHandler;

    protected ListView friendsList;
    protected static JSONArray jsonArray;
    
    private long mRowId ;
    private FriendsDbAdapter mDbHelper ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new FriendsDbAdapter(this);
        mDbHelper.open() ;
        
        mHandler = new Handler();
        setContentView(R.layout.fbdone);

        Bundle extras = getIntent().getExtras();
        String apiResponse = extras.getString("API_RESPONSE");
        
        try {
            jsonArray = new JSONArray(apiResponse);
            Log.v(TAG, "We have jsonArray  " +  apiResponse) ;
            String friendId;
            String name ;
            Uri imageUri ;
            
            //creating a directory in memory card to store the images
            
            // create a File object for the parent directory
            File GroupBankerFriends = new File("/sdcard/GroupBankerFriends/");
            
            // have the object build the directory structure, if needed.
            GroupBankerFriends.mkdirs();
            
            //changing the upper limit of the for loop to 5 in order to test the code. original:jsonArray.length()
            for (int i = 0; i < 5; i++)	{
            	
            	JSONObject jsonObject = jsonArray.getJSONObject(i) ;
            	name = jsonArray.getJSONObject(i).getString("name");
            	final String picURL = jsonObject.getString("pic_square");
            	Log.v(TAG, "We have url of picture  " +  picURL) ;
            	
            	//storing the image from the url in a folder in SD card
            	URL url = new URL (picURL);
            	InputStream input = url.openStream();
            	
            	try {
            		
            		// create a File object for the output file
            		File outputFile = new File(GroupBankerFriends, name);
            		Log.v(TAG, "outputFile created by the name  " +  name) ;
            		
            		// now attach the OutputStream to the file object, instead of a String representation
            		FileOutputStream fos = new FileOutputStream(outputFile);
            		
            	    try {
            	        byte[] buffer = new byte[1000];
            	        int bytesRead = 0;
            	        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
            	            fos.write(buffer, 0, buffer.length);
            	        }            	        
            	        Log.v(TAG, "the image buffer for output file written:  " +  buffer) ;
            	        
            	    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
            	        try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            	    }
            	} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
            	    try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	
            //getting the uri for the image just stored in sdcard
            	
            	String filename = name;
            	String path = "/mnt/sdcard/GroupBankerFriends" + filename;
            	File f = new File(path);  
            	 
           //uri from the file
                imageUri = Uri.fromFile(f);
            	Log.v(TAG, "URI of the image is:  " +  imageUri) ;
            	
           //converting the uri to string in order to store it in a database
            	String stringUri = imageUri.toString();
            	
           //getting fbid for the friend
            	friendId = jsonArray.getJSONObject(i).getString("uid");
            	
           //storing the friend to the SQLite database using FriendsDbAdapter
            	mDbHelper.createFriend(friendId, name, stringUri);
            	 Log.v(TAG, "createFriend called and friend created") ;	
            	
           }
        } catch (JSONException e) {
          // showToast("Error: " + e.getMessage());
            Log.v(TAG, "JSONException : " + e.getMessage()) ;
        	return;
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    protected void onPause()	{
    	super.onPause();
    	mDbHelper.close();
    }
}
