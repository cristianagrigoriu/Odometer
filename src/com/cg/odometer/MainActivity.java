package com.cg.odometer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import com.cg.odometer.Odometer;

public class MainActivity extends Activity {

	private static final String KEY_VALUE = "com.cg.odometer.OdometerValue";
	 
	private Odometer mOdometer;
	 
	private int mOdometerValue;
	
	private TextView mValueDisplay;

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
	    mValueDisplay = (TextView) findViewById(R.id.main_valuedisplay);
	     
	    mOdometer.setOnValueChangeListener(new Odometer.OnValueChangeListener()
	    {
	        @Override
	        public void onValueChange(Odometer sender, int newValue)
	        {
	            updateOdometerValue();
	        }
	    });
	    
	    if(savedInstanceState != null)
	    {
	        mOdometerValue = savedInstanceState.getInt(KEY_VALUE);
	        ((Odometer) mOdometer).setValue(mOdometerValue);
	    }
	    updateOdometerValue();
	}
	 
	protected void onSaveInstanceState(Bundle outState)
	{
	    super.onSaveInstanceState(outState);
	     
	    mOdometerValue = mOdometer.getValue();
	    outState.putInt(KEY_VALUE, mOdometerValue);
	}
	
	private void updateOdometerValue()
	{
	    mOdometerValue = mOdometer.getValue();
	     
	    String text = String.format("%06d", mOdometerValue);
	    mValueDisplay.setText(text);
	}
	
}
