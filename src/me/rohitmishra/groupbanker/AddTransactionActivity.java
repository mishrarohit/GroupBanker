package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

public class AddTransactionActivity extends Activity implements View.OnClickListener{
	
	EditText mDescription ;
	EditText mAmount ;
	Button btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtransaction);
        
        mDescription = (EditText)findViewById(R.id.EditTextDescription);
        mAmount = (EditText)findViewById(R.id.EditTextAmount);
        btn = (Button)findViewById(R.id.addFriends);
        btn.setOnClickListener(this);
        
      }
	
	@Override
	public void onClick(View view) {
		
		Intent selectIntent = new Intent(getApplicationContext(), SelectFriends.class);
		String description = mDescription.getText().toString();
		String amount = mAmount.getText().toString() ;
		
		if (description == null || description.equalsIgnoreCase(""))	{
			mDescription.setError("This field cannot be blank.") ;
			// TODO Change to edittext_blank_error 
		}
		
		else if (amount == null || amount.equalsIgnoreCase(""))	{
			mAmount.setError("This field cannot be left blank");
			// TODO Change to edittext_blank_error
		}
		else	{
			Bundle bundle = new Bundle() ;
			bundle.putString("description", description) ;
			bundle.putString("amount", amount) ;
			selectIntent.putExtras(bundle);
			startActivity(selectIntent);
		}
	}

}
