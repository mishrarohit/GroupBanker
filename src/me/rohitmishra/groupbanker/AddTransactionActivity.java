package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class AddTransactionActivity extends Activity implements View.OnClickListener{
	
	Button btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtransaction);
        
        btn = (Button)findViewById(R.id.addFriends);
        btn.setOnClickListener(this);
        
      }
	
	@Override
	public void onClick(View view) {
		
		Intent selectIntent = new Intent(getApplicationContext(), SelectFriends.class);
        startActivityForResult(selectIntent, 0);
	}

}
