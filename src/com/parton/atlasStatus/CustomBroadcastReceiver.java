package com.parton.atlasStatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CustomBroadcastReceiver extends BroadcastReceiver{
	
	private MainActivity ParentActivity = null;
	public CustomBroadcastReceiver(MainActivity ParentActivity){
		super();
		this.ParentActivity = ParentActivity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.v(TAG, "onStart: Network Type Changed");
        if(NetworkStatusChecker.isConnected(ParentActivity)){
//        	Log.v(TAG, "onStart: Network Connected");
        	String ip = NetworkStatusChecker.getLocalIpAddress();
            if(ip != null){
            	// if existing cookie is empty, then need a new cookie
            	if(ParentActivity.cern_cookie().ip().length() == 0){
            		NetworkStatusChecker.ShowToast(ParentActivity, "login to CERN needed");
//                	Log.v(TAG,"cookie empty, need a cookie");
                	LoginActivity.ShowLoginActivity(ParentActivity);
            	}
            	// if existing cookie is from a different ip address, then need a new cookie
            	else if(!ip.equals(ParentActivity.cern_cookie().ip())){
                	NetworkStatusChecker.ShowToast(ParentActivity, "Network IP Changed, new login needed");
//                	Log.v(TAG,"old ip: "+ParentActivity.cern_cookie().ip()+"("+ParentActivity.cern_cookie().ip().length()+") new ip: "+ip);
                	LoginActivity.ShowLoginActivity(ParentActivity);
                }
                // ip is the same, but thread was paused, restart it
//                else {
//                	NetworkStatusChecker.ShowToast(ParentActivity, "Connection returned, restarting update");
//                	ParentActivity.unpauseThread();
//                }
            }
        }
		
	}
//	private static final String TAG = "CustomBroadcastReceiver";
}
