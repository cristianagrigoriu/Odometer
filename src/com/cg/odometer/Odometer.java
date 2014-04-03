package com.cg.odometer;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;

public class Odometer extends TableLayout {

	private static final int NUM_DIGITS = 6;
	private OdometerSpinner[] mDigitSpinners;
	
	private int mCurrentValue;
	
	private OnValueChangeListener mValueChangeListener;
	
	public Odometer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public Odometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialize();
	}
	
	private void initialize()
	{
	    mDigitSpinners = new OdometerSpinner[NUM_DIGITS];
	     
	    // Inflate the view from the layout resource.
	    String infService = Context.LAYOUT_INFLATER_SERVICE;
	    LayoutInflater li;
	    li = (LayoutInflater)getContext().getSystemService(infService);
	    li.inflate(R.layout.widget_odometer, this, true);
	     
	    mDigitSpinners[0] = (OdometerSpinner)
	        findViewById(R.id.widget_odometer_spinner_1);
	    mDigitSpinners[1] = (OdometerSpinner)
	        findViewById(R.id.widget_odometer_spinner_10);
	    mDigitSpinners[2] = (OdometerSpinner)
	        findViewById(R.id.widget_odometer_spinner_100);
	    mDigitSpinners[3] = (OdometerSpinner)
	        findViewById(R.id.widget_odometer_spinner_1k);
	    mDigitSpinners[4] = (OdometerSpinner)
	        findViewById(R.id.widget_odometer_spinner_10k);
	    mDigitSpinners[5] = (OdometerSpinner)
	        findViewById(R.id.widget_odometer_spinner_100k);    
	    
	    for(OdometerSpinner s : mDigitSpinners)
        {
            s.setOnDigitChangeListener(new OdometerSpinner.OnDigitChangeListener()
            {
                public void onDigitChange(OdometerSpinner s, int newDigit)
                {
                    updateValue();
                }
            });
        }
	}
	
	public int getValue()
	{
	    int value = 0;
	         
	    for(int i = NUM_DIGITS - 1; i >= 0; --i)
	    {
	        value = 10 * value + mDigitSpinners[i].getCurrentDigit();
	    }
	     
	    mCurrentValue = value;
	     
	    return mCurrentValue;
	}
	 
	public void setValue(int value)
	{
	    for(int i = 0; i < NUM_DIGITS; ++i)
	    {
	        mDigitSpinners[i].setCurrentDigit(value % 10);
	        value /= 10;
	    }
	}
	
	private void updateValue()
    {
        int value = 0;
         
        for(int i = NUM_DIGITS - 1; i >= 0; --i)
        {
            value = 10 * value + mDigitSpinners[i].getCurrentDigit();
        }
         
        int old = mCurrentValue;
        mCurrentValue = value;
         
        if(old != mCurrentValue && mValueChangeListener != null)
            mValueChangeListener.onValueChange(this, mCurrentValue);
    }
    
    public void setOnValueChangeListener(OnValueChangeListener listener)
    {
        mValueChangeListener = listener;
    }
    //...
    public interface OnValueChangeListener
    {
        abstract void onValueChange(Odometer sender, int newValue);
    }
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	     
	    // get width and height size and mode
	    int wSpec = MeasureSpec.getSize(widthMeasureSpec);
	    int wMode = MeasureSpec.getMode(widthMeasureSpec);
	     
	    int hSpec = MeasureSpec.getSize(heightMeasureSpec);
	    int hMode = MeasureSpec.getMode(heightMeasureSpec);
	     
	    // calculate max height from width
	    float contentHeight = ((float)wSpec / NUM_DIGITS)
	            * OdometerSpinner.IDEAL_ASPECT_RATIO;
	     
	    int maxHeight = (int)Math.ceil(contentHeight);
	     
	    int width = wSpec;
	    int height = hSpec;
	     
	    if(maxHeight < hSpec)
	    {
	        height = maxHeight;
	    }
	     
	    setMeasuredDimension(width, height);
	}
}
