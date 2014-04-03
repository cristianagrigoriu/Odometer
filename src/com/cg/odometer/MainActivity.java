package com.cg.odometer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.cg.odometer.Odometer;

public class MainActivity extends Activity {

	private static final String KEY_VALUE = "com.cg.odometer.OdometerValue";
	 
	private Odometer mOdometer;
	 
	private int mOdometerValue;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	     
	    mOdometer = (Odometer) findViewById(R.id.odometer);
	     
	    if(savedInstanceState != null)
	    {
	        mOdometerValue = savedInstanceState.getInt(KEY_VALUE);
	        ((Odometer) mOdometer).setValue(mOdometerValue);
	    }
	}
	 
	protected void onSaveInstanceState(Bundle outState)
	{
	    super.onSaveInstanceState(outState);
	     
	    mOdometerValue = mOdometer.getValue();
	    outState.putInt(KEY_VALUE, mOdometerValue);
	}
	
}
