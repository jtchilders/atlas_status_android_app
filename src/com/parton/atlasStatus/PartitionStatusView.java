package com.parton.atlasStatus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import com.parton.atlasStatus.R;

public class PartitionStatusView extends TextView {
	
	public PartitionStatusView(Context ctx, AttributeSet attrs){
		super(ctx,attrs);
//		Log.v(TAG,"PartitionStatusView: inside 1");
	}
	

	public PartitionStatusView(Context ctx){
		super(ctx);
//		Log.v(TAG,"PartitionStatusView: inside 2");
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
//		Log.v(TAG,"onDraw: inside");
	}
	
	public void setText(String text){
		super.setText(text);
//		Log.v(TAG,"setText: inside");
		setBackgroundColor(text);
	}
	
	public void setBackgroundColor(String partition_state){
//		Log.v(TAG,"setBackgroundColor: Setting color for run state: "+partition_state);
		if(partition_state.contains("RUNNING"))
			this.setBackgroundResource(R.color.partition_status_RUNNING);
		else if(partition_state.contains("NONE"))
				this.setBackgroundResource(R.color.partition_status_NONE);
		else if(partition_state.contains("INITIAL"))
				this.setBackgroundResource(R.color.partition_status_INITIAL);
		else if(partition_state.contains("ABSENT"))
				this.setBackgroundResource(R.color.partition_status_ABSENT);
		else if(partition_state.contains("UP"))
				this.setBackgroundResource(R.color.partition_status_UP);
		else if(partition_state.contains("CONNECTED"))
				this.setBackgroundResource(R.color.partition_status_CONNECTED);
		else if(partition_state.contains("CONFIGURED"))
				this.setBackgroundResource(R.color.partition_status_CONFIGURED);
		else if(partition_state.contains("BOOTED"))
				this.setBackgroundResource(R.color.partition_status_BOOTED);
		else if(partition_state.contains("PAUSED"))
			this.setBackgroundResource(R.color.partition_status_PAUSED);
		else if(partition_state.contains("READY"))
			this.setBackgroundResource(R.color.partition_status_READY);
		else if(partition_state.contains("ROIBSTOPPED"))
			this.setBackgroundResource(R.color.partition_status_ROIBSTOPPED);
		else if(partition_state.contains("LV2SVSTOPPED"))
			this.setBackgroundResource(R.color.partition_status_LV2SVSTOPPED);
		else if(partition_state.contains("L2STOPPED"))
			this.setBackgroundResource(R.color.partition_status_L2STOPPED);
		else if(partition_state.contains("EBSTOPPED"))
			this.setBackgroundResource(R.color.partition_status_EBSTOPPED);
		else if(partition_state.contains("EFSTOPPED"))
			this.setBackgroundResource(R.color.partition_status_EFSTOPPED);
		else if(partition_state.contains("SFOSTOPPED"))
			this.setBackgroundResource(R.color.partition_status_SFOSTOPPED);
		else if(partition_state.contains("GTHSTOPPED"))
			this.setBackgroundResource(R.color.partition_status_GTHSTOPPED);
//		else
//			Log.e(TAG,"setBackgroundColor: run state not recognized: "+partition_state);
	}
	
//	private final String TAG = "ParitionStatusView";
}
