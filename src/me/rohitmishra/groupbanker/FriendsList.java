package me.rohitmishra.groupbanker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            final long friendId;
            String name ;
            String imageURI ;
            
            for (int i = 0; i < jsonArray.length(); i++)	{
            	JSONObject jsonObject = jsonArray.getJSONObject(i) ;
            	name = jsonArray.getJSONObject(i).getString("name");
            	
            	
            	// TODO Get fbid, name and imageuri of the friend and call mDbHelper.createFriend
            	
            	
            	
            }
        } catch (JSONException e) {
          // showToast("Error: " + e.getMessage());
            Log.v(TAG, "JSONException : " + e.getMessage()) ;
        	return;
        }
    }
}
