package com.parton.atlasStatus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BusyStatusFragment extends Fragment {
	
	public static BusyStatusFragment newInstance(){
		return new BusyStatusFragment();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.v(TAG,"onCreateView: inside");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.busy_status, container, false);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.v(TAG,"onCreate: inside");
		
        
        
	}
	
	@Override
	public void onStart(){
		super.onStart();
//		Log.v(TAG,"onStart: inside");
        
	}
	
	@Override
	public void onResume(){
		super.onResume();
//		Log.v(TAG,"onResume: inside");
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
//		Log.v(TAG,"onPause: inside");
	}
	
	@Override
	public void onStop(){
		super.onStop();
//		Log.v(TAG,"onStop: inside");
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
//		Log.v(TAG,"onDestroy: inside");
		
	}
	
//	private static final String TAG = "BusyStatusFragment";
}
