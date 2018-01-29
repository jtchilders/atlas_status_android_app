package com.parton.atlasStatus;

import com.viewpagerindicator.UnderlinePageIndicator;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

@SuppressLint({ "SetJavaScriptEnabled" })
public class MainActivity extends FragmentActivity {
	
	private ISInfoUpdaterThread is_info_thread = null;
	private UserInfoUpdateHandler is_thread_handler = null;
	
	
	public static String TDAQ_PARTITION = "ATLAS_mirror";
	public static String INITIAL_PARTITION = "initial";
	
	private IntentFilter intent_filter = null;
	private CustomBroadcastReceiver broadcastReceiver = null;
	
	public static CernCookie cern_cookie = null;
	public CernCookie cern_cookie(){
		return cern_cookie;
	}
	
	MyFragmentAdapter mAdapter;
    ViewPager mPager;
    UnderlinePageIndicator mIndicator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.v(TAG,"onCreate: inside");

        setContentView(R.layout.activity_main);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(2);

        mIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setFades(false);
        mIndicator.setViewPager(mPager);
        
		
        
        
        // create a cookie
        cern_cookie = new CernCookie(this);
        cern_cookie.readCookie();
        
        // check for network connection
        if(!NetworkStatusChecker.isConnected(this)){
        	//Log.v(TAG,"onCreate: not connected");
        	NetworkStatusChecker.ShowNoNetworkDialogue(this);
        }
        else{

        	if(!cern_cookie.isValid()){
        		LoginActivity.ShowLoginActivity(this);
        	}
	        
        }

        
//        //Log.v(TAG,"onCreate: Getting run number");
//        String text = getString(getResources().getIdentifier("atlas_run_number","string","com.example.myfirstapp"));
//        //Log.v(TAG,"onCreate: Getting run number: "+text);
        
        
	}
	
	@Override
	public void onStart(){
		super.onStart();
//		Log.v(TAG,"onStart: inside");

        // listen for connection changes
        broadcastReceiver = new CustomBroadcastReceiver(this);
        intent_filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intent_filter);

		// create updater thread and handler
        if(is_thread_handler == null)
        	is_thread_handler = new UserInfoUpdateHandler(this,menu);
        if(is_info_thread == null)
			is_info_thread = new ISInfoUpdaterThread(is_thread_handler,this);
        if(cern_cookie != null && cern_cookie.isValid())
        	is_info_thread.updateCookie(cern_cookie);
		is_info_thread.UpdateSyncFrequency();
		
		
		// start thread
		// pause if cookie has not been setup yet
		if(!cern_cookie.isValid()){
			is_info_thread.pause();
		}
		is_info_thread.start();
			
        
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
//		Log.v(TAG,"onRestart: inside");
	}
	
	@Override
	public void onResume(){
		super.onResume();
//		Log.v(TAG,"onResume: inside");
		
		if(cern_cookie.isValid()){
			//Log.v(TAG,"onResume: unpausing is_info_thread");
			is_info_thread.unpause();
		}
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
//		Log.v(TAG,"onPause: inside");
		is_info_thread.pause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
//		Log.v(TAG,"onStop: inside");
		
		unregisterReceiver(broadcastReceiver);

		if(is_info_thread != null){
			is_info_thread.doUpdate(false);
			is_info_thread.pauseUpdate(false);
			is_info_thread.interrupt();
			try {
				is_info_thread.join();
				is_info_thread = null;
				//Log.v(TAG,"onStop: is_info_thread has rejoined");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
//		Log.v(TAG,"onDestroy: inside");
		
		if(cern_cookie != null){
			cern_cookie.writeCookie();
			cern_cookie = null;
		}
	}
	
	private Menu menu = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Log.v(TAG,"onCreateOptionsMenu: inside");
	    getMenuInflater().inflate(R.menu.activity_main, menu);
	    this.menu = menu;
	    if(is_thread_handler != null)
	    	is_thread_handler.menu(menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Log.v(TAG,"onOptionsItemSelected: inside");
		switch(item.getItemId()){
		case R.id.menu_settings:
			//Log.v(TAG,"onOptionsItemSelected: in menu Settings");
			Intent settingsIntent = new Intent(this,SettingsActivity.class);
	        startActivityForResult(settingsIntent,GET_SYNC_FREQ);
			
			return true;
		case R.id.menu_about:
			ShowAboutDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		Log.v(TAG,"onActivityResult: inside");
		// If the request went well (OK) and the request was GET_COOKIE
	    if (resultCode == Activity.RESULT_OK){
	    	switch(requestCode){
	    	case GET_COOKIE:
//	    		Log.v(TAG,"onActivityResult: received cookie from LoginActivity");
	    		
	    		String returnedData = data.getStringExtra(LoginActivity.CookieReturnData);
	    		if(returnedData == null){
//	    			Log.v(TAG,"onActivityResult: no CookieReturnData");
	    			returnedData = data.getStringExtra(LoginActivity.ALREADY_RUNNING);
//	    			if(returnedData == null){
//	    				Log.v(TAG,"onActivityResult: LoginActivity returned with no result");
//	    			}
//	    			else{
//	    				Log.v(TAG,"onActivityResult: LoginActivity returned because it's already running");
//	    			}
	    		}
	    		else{
	    			cern_cookie.setTime();
			        cern_cookie.cookie(returnedData);
			        cern_cookie.setIp();
	    		}
	    		
		        
		        
		        if(is_info_thread != null){
		        	is_info_thread.updateCookie(cern_cookie);
		        	// need to restart thread if it is waiting for a new cookie
		        	is_info_thread.unpause();
		        }
//		        else{
//		        	Log.v(TAG,"onActivityResult: thread is null!");
//		        }
		        
		        break;
	    	case GET_SYNC_FREQ:
	    		is_info_thread.UpdateSyncFrequency();
	    		break;
//	    	default:
//	    		Log.w(TAG,"onActivityResult: Activity returned unknown result.");
	    	}
		}
//    	else{
//    		Log.w(TAG,"onActivityResult: Activity returned and RESULT_OK not set.");
//    	}
	    
	    
	}
	
	
	private void ShowAboutDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setMessage("ATLAS Status\nWritten By: Taylor Childers (CERN)\nDeveloped for expert on-call monitoring of current operations activities.");
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				return;
			}
		});
		builder.setNegativeButton(R.string.no_network_connection_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		builder.show();
	}
	
	
	
	
	private final static int GET_SYNC_FREQ = 44;
	public final static int GET_COOKIE = 33;
//	private final String TAG = "MainActivity";
}
