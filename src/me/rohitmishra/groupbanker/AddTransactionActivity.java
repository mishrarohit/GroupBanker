package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AddTransactionActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Add Transaction tab");
        setContentView(textview);
    }
}
