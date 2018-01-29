package com.parton.atlasStatus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	private static final double SCALER = 0.8;

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}


	public MyTextView(Context ctx, AttributeSet attrs){
		super(ctx,attrs);
//		Log.v(TAG,"MyTextView: inside 1");
	}
	

	public MyTextView(Context ctx){
		super(ctx);
//		Log.v(TAG,"MyTextView: inside 2");
	}
	

	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh){
		super.onSizeChanged(w,h,oldw,oldh);
		
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (this.getLineHeight()*SCALER));
	}
	
	
//	private final static String TAG = "MyTextView";
}
