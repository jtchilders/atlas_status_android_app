package com.parton.atlasStatus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

@SuppressLint("DrawAllocation")
public class TextProgressBar extends ProgressBar {  
    private String text;
    protected Paint textPaint;
    private static final String initialText = "Busy Percent";
    private static final double textHeightScaler = 0.8;
  
    public TextProgressBar(Context context) {  
        super(context);  
        Initialize(); 
    }  
  
    public TextProgressBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        Initialize();
    }  
  
    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        Initialize();
    }
    
    private void Initialize(){
        text = initialText;  
        textPaint = new Paint();  
        textPaint.setColor(Color.BLACK);  
    }
  
    @Override  
    protected synchronized void onDraw(Canvas canvas) {  
//    	Log.v(TAG,"onDraw: inside " + text);
        // First draw the regular progress bar, then custom draw our text  
        super.onDraw(canvas);
        Rect bounds = new Rect();
        textPaint.setTextSize((float) (getHeight()*textHeightScaler));
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.centerX();
        int y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(text, x, y, textPaint);
    }  
  
    public synchronized void setText(String text) {
//    	Log.v(TAG,"setText: text = " + text);
        this.text =  text;  
        super.postInvalidate();
    }  
  
    public void setTextColor(int color) {
    	textPaint.setColor(color);
    	super.postInvalidate();  
    }
    
//    private static final String TAG = "TextProgressBar";
}  
