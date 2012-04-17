package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalOverviewActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("Consolidated overview of the current transaction");
        setContentView(textview);
    }
}
