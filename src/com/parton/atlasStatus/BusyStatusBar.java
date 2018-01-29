package com.parton.atlasStatus;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

public class BusyStatusBar extends TextProgressBar {

	public BusyStatusBar(Context context) {
		super(context);
	}
	

	public BusyStatusBar(Context context,AttributeSet attrs) {
		super(context,attrs);
		getXMLattributes(context,attrs);
	}
	

	public BusyStatusBar(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
		getXMLattributes(context,attrs);
	}
	
	private void getXMLattributes(Context context,AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.BusyStatusBar);
		
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i)
		{
		    int attr = a.getIndex(i);
		    switch (attr)
		    {
		        case R.styleable.BusyStatusBar_name:
		            name(a.getString(attr));
		            break;
		        case R.styleable.BusyStatusBar_enabled:
		            enabled(a.getBoolean(attr, true));
		            break;
		        case R.styleable.BusyStatusBar_text_mode:
		            int index = a.getInt(attr, 0);
		            setTextMode(TEXT_MODE.values()[index]);
		            break;
		    }
		}
		a.recycle();

	}
	
	
	@Override
    public void setTextColor(int color) {
		if(enabled())
			super.textPaint.setColor(color);
		else
			super.textPaint.setColor(getResources().getColor(R.color.grey));
        drawableStateChanged();  
    }
	
	public synchronized void setPercent(float percent){
		Log.v(TAG,"setPercent: name: "+name()+ ", percent: "+percent);
		this.setProgress((int)percent);
		String percentText = Float.toString(percent).substring(0,3)+"%";
		Log.v(TAG,"setPercent: percentText: "+percentText);
		
		if(percent > BUSY_WARNING_LEVEL)
			setTextColor(getResources().getColor(R.color.red));
		else
			setTextColor(getResources().getColor(R.color.green));
		
		switch(textMode){
		case TEXT_PERCENT:
			super.setText(percentText);
			Log.v(TAG,"setPercent: text percent");
			return;
		case TEXT_NAME:
			super.setText(name());
			Log.v(TAG,"setPercent: text name");
			return;
		case TEXT_PERCENT_NAME:
			super.setText(percentText+" "+name());
			Log.v(TAG,"setPercent: text percent-name");
			return;
		case TEXT_NAME_PERCENT:
			super.setText(name()+" "+percentText);
			Log.v(TAG,"setPercent: text name-percent");
			return;
		default:
			super.setText(percentText);
			return;
		}
		
	}
	
    private String name = "";
    public void name(String name){
    	this.name = name;
    }
    public String name(){
    	return name;
    }
    
	
    private boolean enabled = true;
    public void enabled(boolean enabled){
    	this.enabled = enabled;
    	if(!enabled()) super.textPaint.setColor(getResources().getColor(R.color.grey));
    }
    public boolean enabled(){
    	return enabled;
    }
    
    public static enum TEXT_MODE { TEXT_PERCENT, TEXT_NAME, TEXT_PERCENT_NAME, TEXT_NAME_PERCENT };
    
    private TEXT_MODE textMode = TEXT_MODE.TEXT_PERCENT;
    public void setTextMode(TEXT_MODE mode){
    	textMode = mode;
    }
    
    private static final int BUSY_WARNING_LEVEL = 5;
    private static final String TAG = "BusyStatusBar";
}
