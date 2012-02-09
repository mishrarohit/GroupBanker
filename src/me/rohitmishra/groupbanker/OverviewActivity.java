package me.rohitmishra.groupbanker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class OverviewActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Overview tab");
        setContentView(textview);
    }
}
