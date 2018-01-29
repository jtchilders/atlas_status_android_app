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
    private Paint textPaint;
    private static final String initialText = "Busy Percent";
    private static final double textHeightScaler = 0.8;
  
    public TextProgressBar(Context context) {  
        super(context);  
        text = initialText;  
        textPaint = new Paint();  
        textPaint.setColor(Color.BLACK);  
    }  
  
    public TextProgressBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        text = initialText;  
        textPaint = new Paint();  
        textPaint.setColor(Color.BLACK);
    }  
  
    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        text = initialText;  
        textPaint = new Paint();  
        textPaint.setColor(Color.BLACK);  
    }  
  
    @Override  
    protected synchronized void onDraw(Canvas canvas) {  
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
        this.text =  text;  
        drawableStateChanged();  
    }  
  
    public void setTextColor(int color) {
    	if(enabled())
    		textPaint.setColor(color);
    	else
    		textPaint.setColor(getResources().getColor(R.color.grey));
        drawableStateChanged();  
    }
    
    private boolean enabled = true;
    public void enabled(boolean enabled){
    	this.enabled = enabled;
    }
    public boolean enabled(){
    	return enabled;
    }
    
    
}  
