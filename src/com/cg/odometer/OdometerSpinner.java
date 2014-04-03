/**
 * 
 */
package com.cg.odometer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author cristioara
 *
 */
public class OdometerSpinner extends View {

	private float mWidth;
    private float mHeight;
    
    private GradientDrawable mBGGrad;

    private String mDigitString;
    private Paint mDigitPaint;
    private float mDigitX;
    private float mDigitY;
    private int mCurrentDigit;
    
	private float mTouchLastY;
	private float mTouchStartY;

	private int mDigitAbove;
	private int mDigitBelow;
	private float mDigitAboveY;
	private float mDigitBelowY;
	private String mDigitAboveString;
	private String mDigitBelowString;
	
	public static final float IDEAL_ASPECT_RATIO = 1.5f;
	
    //constructors
	/**
	 * @param context
	 */
	public OdometerSpinner(Context context) {
		super(context);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public OdometerSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public OdometerSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize();
	}

	
	
	//our methods
	private void initialize()
	{
	    mBGGrad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { 0xFF000000, 0xFFAAAAAA, 0xFF000000 });
	     
	    mDigitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    mDigitPaint.setColor(Color.WHITE);
	    mDigitPaint.setTextAlign(Align.CENTER);
	    
	    setCurrentDigit(0);
	}

	public int getCurrentDigit()
	{
	    return mCurrentDigit;
	}
	
	public void setCurrentDigit(int digit)
	{
	    /*
	     *  Basic range limiting - in a production widget,
	     *  you might want to throw an exception if the number passed
	     *  if less than 0 or greater than 9
	     */
	    int newVal = digit;
	 
	    
	    if(newVal < 0)
	        newVal = 0;
	    if(newVal > 9)
	        newVal = 9;
	 
	    mCurrentDigit = newVal;
	    
	 // Digit above - greater
	    mDigitAbove = mCurrentDigit + 1;
	     
	    if(mDigitAbove > 9)
	        mDigitAbove = 0;
	     
	    // digit below - lower
	    mDigitBelow = mCurrentDigit - 1;
	     
	    if(mDigitBelow < 0)
	        mDigitBelow = 9;

	    mDigitString = String.valueOf(mCurrentDigit);
	    mDigitAboveString = String.valueOf(mDigitAbove);
	    mDigitBelowString = String.valueOf(mDigitBelow);
	    
	    setDigitYValues();
	    invalidate();
	}

	private void setDigitYValues()
	{
	    mDigitY = findCenterY(mCurrentDigit);
	    mDigitAboveY = findCenterY(mDigitAbove) - mHeight;
	    mDigitBelowY = mHeight + findCenterY(mDigitBelow);
	}
	 
	private float findCenterY(int digit)
	{
	    String text = String.valueOf(digit);
	    Rect bounds = new Rect();
	    mDigitPaint.getTextBounds(text, 0, text.length(), bounds);
	 
	    int textHeight = Math.abs(bounds.height());
	 
	    float result = mHeight - ((mHeight - textHeight) / 2);
	 
	    return result;
	}

	//their methods
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mBGGrad.draw(canvas);
		canvas.drawText(mDigitString, mDigitX, mDigitY, mDigitPaint);
		canvas.drawText(mDigitAboveString, mDigitX, mDigitAboveY, mDigitPaint);
	    canvas.drawText(mDigitBelowString, mDigitX, mDigitBelowY, mDigitPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// get width and height size and mode
	    int wSpec = MeasureSpec.getSize(widthMeasureSpec);
	    int wMode = MeasureSpec.getMode(widthMeasureSpec);
	     
	    int hSpec = MeasureSpec.getSize(heightMeasureSpec);
	    int hMode = MeasureSpec.getMode(heightMeasureSpec);
	     
	    int width = wSpec;
	    int height = hSpec;
	     
	    // ideal height for the number display
	    int idealHeight = (int) (wSpec * IDEAL_ASPECT_RATIO);
	     
	    if(idealHeight < hSpec)
	    {
	        height = idealHeight;
	    }
	     
	    setMeasuredDimension(width, height);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mWidth = w;
	    mHeight = h;
	     
	    mBGGrad.setBounds(0, 0, w, h);
	    
	    mDigitPaint.setTextSize(h);
	     
	    mDigitX = mWidth / 2;
	    setDigitYValues();
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
	    // Pull out the Action value from the event for processing
			
	    int action = event.getAction();
	 
	    if(action == MotionEvent.ACTION_DOWN)
	    {
	        mTouchStartY = event.getY();
	        mTouchLastY = mTouchStartY;
	 
	        return true;
	    }
	    else if(action == MotionEvent.ACTION_MOVE)
	    {
	        float currentY = event.getY();
	 
	        float delta = mTouchLastY - currentY;
	        mTouchLastY = currentY;
	 
	        mDigitY -= delta;
	        mDigitAboveY -= delta;
	        mDigitBelowY -= delta;
	 
	        invalidate();
	 
	        return true;
	    }
	    else if(action == MotionEvent.ACTION_UP)
	    {
	    	float currentY = event.getY();
	    	 
	        // delta: negative means a down 'scroll'
	        float deltaY = mTouchStartY - currentY;
	 
	        int newValue = mCurrentDigit;
	 
	        if(Math.abs(deltaY) > (mHeight / 3) )
	        {
	            // higher numbers are 'above' the current, so a scroll down
	            // _increases_ the value
	            if(deltaY < 0)
	            {
	            	newValue = mDigitAbove;
	            }
	            else
	            {
	            	newValue = mDigitBelow;
	            }
	        }
	 
	        setCurrentDigit(newValue);
	 
	        return true;
	    }
	    return false;
	}

}
