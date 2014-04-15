package com.cg.odometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

import com.cg.odometer.Odometer;

public class MainActivity extends Activity implements SensorEventListener{

	private static final String KEY_VALUE = "com.cg.odometer.OdometerValue";
	 
	private Odometer mOdometer;
	 
	private int mOdometerValue;
	
	private TextView mValueDisplay;

	private boolean mInitialized; // used for initializing sensor only once
	 
	private SensorManager mSensorManager;
	 
	private Sensor mAccelerometer;
	
	private final float NOISE = (float) 2.0;

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
	    
	 // Initialize Accelerometer sensor
	    mInitialized = false;
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    startSensor();
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
	
	private void startSensor() {
		mSensorManager.registerListener((SensorEventListener) this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void onSensorChanged(SensorEvent event) {
		// event object contains values of acceleration, read those
		double x = event.values[0];
		double y = event.values[1];
		double z = event.values[2];
		
		int stepsCount = 0;
	 
		final double alpha = 0.8; // constant for our filter below
	 
		double[] gravity = {0,0,0};
	 
		 // Isolate the force of gravity with the low-pass filter.
		 gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		 gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		 gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
	 
		// Remove the gravity contribution with the high-pass filter.
		x = event.values[0] - gravity[0];
		y = event.values[1] - gravity[1];
		z = event.values[2] - gravity[2];
	 
		double mLastX = 0;
		double mLastY = 0;
		double mLastZ = 0;
		
		if (!mInitialized) {
			 // sensor is used for the first time, initialize the last read values
			 mLastX = x;
			 mLastY = y;
			 mLastZ = z;
			 mInitialized = true;
		} else 
		{
		 // sensor is already initialized, and we have previously read values.
		 // take difference of past and current values and decide which
		 // axis acceleration was detected by comparing values
	 
			double deltaX = Math.abs(mLastX - x);
			double deltaY = Math.abs(mLastY - y);
		    double deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE)
			deltaX = (float) 0.0;
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
	 
			if (deltaX > deltaY) {
			// Horizontal shake
			// do something here if you like
	 
			} else if (deltaY > deltaX) {
				 // Vertical shake
				 // do something here if you like
	 
			} else if ((deltaZ > deltaX) && (deltaZ > deltaY)) {
				 // Z shake
				 stepsCount = stepsCount + 1;
				 if (stepsCount > 0) {
					 TextView txtCount = (TextView)findViewById(R.id.txtCount);
					 txtCount.setText(String.valueOf(stepsCount));
				 }
			} 
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
