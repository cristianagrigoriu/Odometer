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
import android.util.AttributeSet;
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
	    
	    setCurrentDigit(4);
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
	    mDigitString = String.valueOf(mCurrentDigit);
	 
	    setDigitYValues();
	    invalidate();
	}

	private void setDigitYValues()
	{
	    mDigitY = findCenterY(mCurrentDigit);
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
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
}
