package com.parton.atlasStatus;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.parton.atlasStatus.R;

@SuppressLint({ "SetJavaScriptEnabled" })
public class MainActivity extends Activity {
	
	private String atlas_cookie = "";
	private long atlas_cookie_time_of_last_update = 0;
	private int atlas_cookie_networkType = 0;
	private String atlas_cookie_ip = "";
	private ISInfoUpdaterThread is_info_thread = null;
	private UserInfoUpdateHandler is_thread_handler = null;
	
	private String partitionName = "ATLAS_mirror";
	
	private IntentFilter intent_filter = null;
	private BroadcastReceiver networkStateReceiver = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.v(TAG,"onCreate: inside");
		
		
        setContentView(R.layout.activity_main);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
	    
        // check for network connection
        if(!NetworkStatusChecker.isConnected(this)){
        	//Log.v(TAG,"onCreate: not connected");
        	NetworkStatusChecker.ShowNoNetworkDialogue(this);
        	return;
        }
        
		readCookie();
		// if no cookies set, get one
		if(atlas_cookie_time_of_last_update == 0 || 
				!atlas_cookie_ip.contains(NetworkStatusChecker.getLocalIpAddress()) ||
				atlas_cookie_networkType != NetworkStatusChecker.getNetworkType(this)
				){
			showSSO();
		}
		

        
//        //Log.v(TAG,"onCreate: Getting run number");
//        String text = getString(getResources().getIdentifier("atlas_run_number","string","com.example.myfirstapp"));
//        //Log.v(TAG,"onCreate: Getting run number: "+text);
        
        
	}
	
	@Override
	public void onStart(){
		super.onStart();
		//Log.v(TAG,"onStart: inside");
		final Context ParentActivity = this;

        // listen for connection changes
        networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "onStart: Network Type Changed");
                if(NetworkStatusChecker.isConnected(ParentActivity)){
                	Log.v(TAG, "onStart: Network Connected");
	                String ip = NetworkStatusChecker.getLocalIpAddress();
	                if(ip != null){
		                if(!ip.equals(atlas_cookie_ip)){
		                	NetworkStatusChecker.ShowToast(ParentActivity, "Network IP Changed, new login needed");
		                	Log.v(TAG,"old ip: "+atlas_cookie_ip+" new ip: "+NetworkStatusChecker.getLocalIpAddress());
		                	showSSO();
		                }
		                // ip is the same, but thread was paused, restart it
		                else if(is_info_thread.pauseUpdate()){
		                	NetworkStatusChecker.ShowToast(ParentActivity, "Conection returned, restarting update");
		                	is_info_thread.pauseUpdate(false);
		                	is_info_thread.interrupt();
		                }
	                }
                }
            }
        };

        intent_filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);        
        registerReceiver(networkStateReceiver, intent_filter);

		// create updater thread
		getNewISInfoUpdaterThread();
		is_info_thread.updateCookie(atlas_cookie);
		//Log.v(TAG,"onStart: menu: " + menu);
		is_thread_handler.menu(menu);
		// start thread
		is_info_thread.start();
			
        
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		//Log.v(TAG,"onRestart: inside");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		//Log.v(TAG,"onResume: inside");
		
		if(is_info_thread.pauseUpdate()){
			//Log.v(TAG,"onResume: unpausing is_info_thread");
			is_info_thread.pauseUpdate(false);
			is_info_thread.interrupt();
		}
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		//Log.v(TAG,"onPause: inside");
		is_info_thread.pauseUpdate(true);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		//Log.v(TAG,"onStop: inside");
		
		unregisterReceiver(networkStateReceiver);

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
		//Log.v(TAG,"onDestroy: inside");
		
		writeCookie();
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
		//Log.v(TAG,"onActivityResult: inside");
		// If the request went well (OK) and the request was GET_COOKIE
	    if (resultCode == Activity.RESULT_OK){
	    	switch(requestCode){
	    	case GET_COOKIE:
		        atlas_cookie_time_of_last_update = System.currentTimeMillis();
		        atlas_cookie = data.getStringExtra(LoginActivity.CookieReturnData);
		        if(is_info_thread != null){
		        	is_info_thread.updateCookie(atlas_cookie);
		        	// need to restart thread if it is waiting for a new cookie
			        if(is_info_thread.pauseUpdate()){
			        	is_info_thread.pauseUpdate(false);
			        	is_info_thread.interrupt();
			        }
		        }
		        else{
		        	Log.v(TAG,"onActivityResult: thread is null!");
		        }
		        atlas_cookie_networkType = NetworkStatusChecker.getNetworkType(this);
		        atlas_cookie_ip = NetworkStatusChecker.getLocalIpAddress();
		        
		        break;
	    	case GET_SYNC_FREQ:
	    		SetSyncFrequency();
	    		break;
	    	default:
	    		Log.w(TAG,"onActivityResult: Activity returned unknown result.");
	    	}
		}
    	else{
    		Log.w(TAG,"onActivityResult: Activity returned and RESULT_OK not set.");
    	}
	    
	    
	}
	
//	private void writeFileToInternalStorage() {
//		String eol = System.getProperty("line.separator");
//		BufferedWriter writer = null;
//		try { // opens file in /data/data/com.example.myfirstapp/files use "adb shell" to browse file system
//			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("myfile", MODE_PRIVATE)));
//			writer.write("This is a test1." + eol);
//			writer.write("This is a test2." + eol);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	private final String cookieFilename = "atlas_cookie.txt";
	private final long max_time_diff_sec = 60*60*2; // 2 hrs
	// write cookie to file so it can be loaded
	private void writeCookie(){
		//Log.v(TAG,"writeCookie: inside");
		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(cookieFilename,MODE_PRIVATE)));
			writer.write(atlas_cookie + eol);
			writer.write(atlas_cookie_time_of_last_update + eol);
			writer.write(atlas_cookie_networkType + eol);
			writer.write(atlas_cookie_ip + eol);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null){
				try {
					writer.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void readCookie(){
		//Log.v(TAG,"readCookie: inside");
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(openFileInput(cookieFilename)));
			String line = null;
			line = reader.readLine();
			if(line != null){
				String cookie = line;
				
				line = reader.readLine();
				if(line != null){
					long cookieTimeMillis = Long.decode(line);
					long localTimeMillis = System.currentTimeMillis();
					
					if((localTimeMillis - cookieTimeMillis)/1000 < max_time_diff_sec){
						atlas_cookie = cookie;
						atlas_cookie_time_of_last_update = cookieTimeMillis;
						
						line = reader.readLine();
						if(line != null){
							int networkType = Integer.decode(line);
							int current_networkType = NetworkStatusChecker.getNetworkType(this);
							if(current_networkType == networkType){
								atlas_cookie_networkType = networkType;
								
								line = reader.readLine();
								if(line != null){
									atlas_cookie_ip = line;
								}
								else{
									atlas_cookie = "";
									atlas_cookie_time_of_last_update = 0;
									atlas_cookie_networkType = 0;
									atlas_cookie_ip = "";
								}
							}
							else{
								atlas_cookie = "";
								atlas_cookie_time_of_last_update = 0;
								atlas_cookie_networkType = 0;
								atlas_cookie_ip = "";
							}
						}
						else{
							atlas_cookie = "";
							atlas_cookie_time_of_last_update = 0;
							atlas_cookie_networkType = 0;
							atlas_cookie_ip = "";
						}
					}
					else{
						atlas_cookie = "";
						atlas_cookie_time_of_last_update = 0;
						atlas_cookie_networkType = 0;
						atlas_cookie_ip = "";
					}
					
				}
			}
			
		} catch (Exception e) {
			atlas_cookie = "";
			atlas_cookie_time_of_last_update = 0;
			atlas_cookie_networkType = 0;
			atlas_cookie_ip = "";
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void getNewISInfoUpdaterThread(){
		//Log.v(TAG,"getNewISInfoUpdaterThread: inside");
		PartitionStatusView partition_status_view = (PartitionStatusView) findViewById(R.id.atlas_partition_status);
		TextView run_number_view = (TextView) findViewById(R.id.atlas_run_number);
		TextView project_tag_view = (TextView) findViewById(R.id.atlas_project_tag);
		TextView partition_start_time_view = (TextView) findViewById(R.id.atlas_partition_start_time);
		TextView lhc_page_one_status_time_view = (TextView) findViewById(R.id.lhc_page_one_status_time);
		TextView lhc_page_one_status_view = (TextView) findViewById(R.id.lhc_page_one_status);
		TextView atlas_included_hw_muctpi = (TextView) findViewById(R.id.atlas_included_hw_muctpi);
		TextView atlas_included_hw_ctp = (TextView) findViewById(R.id.atlas_included_hw_ctp);
		TextView atlas_included_hw_l1calo = (TextView) findViewById(R.id.atlas_included_hw_l1calo);
		TextView atlas_included_hw_hlt = (TextView) findViewById(R.id.atlas_included_hw_hlt);
		TextView atlas_included_hw_rpc = (TextView) findViewById(R.id.atlas_included_hw_rpc);
		TextView atlas_included_hw_tgc = (TextView) findViewById(R.id.atlas_included_hw_tgc);
		TextView atlas_included_hw_mdt = (TextView) findViewById(R.id.atlas_included_hw_mdt);
		TextView atlas_included_hw_csc = (TextView) findViewById(R.id.atlas_included_hw_csc);
		TextView atlas_included_hw_lar = (TextView) findViewById(R.id.atlas_included_hw_lar);
		TextView atlas_included_hw_tile = (TextView) findViewById(R.id.atlas_included_hw_tile);
		TextView atlas_included_hw_pix = (TextView) findViewById(R.id.atlas_included_hw_pix);
		TextView atlas_included_hw_sct = (TextView) findViewById(R.id.atlas_included_hw_sct);
		TextView atlas_included_hw_trt = (TextView) findViewById(R.id.atlas_included_hw_trt);
		TextView atlas_included_hw_lucid = (TextView) findViewById(R.id.atlas_included_hw_lucid);
		TextView atlas_included_hw_bcm = (TextView) findViewById(R.id.atlas_included_hw_bcm);
		TextView atlas_included_hw_alfa = (TextView) findViewById(R.id.atlas_included_hw_alfa);
		TextView atlas_included_hw_zdc = (TextView) findViewById(R.id.atlas_included_hw_zdc);
		TextView atlas_ready4physics_view = (TextView) findViewById(R.id.atlas_ready4physics);
		TextView lhc_stable_beams_flag_view = (TextView) findViewById(R.id.lhc_stable_beams_flag);
		TextView lhc_beam_mode_view = (TextView) findViewById(R.id.lhc_beam_mode);
		
		// busy
		TextView busy_summary_high = (TextView) findViewById(R.id.busy_summary_high);
		TextView busy_summary_low = (TextView) findViewById(R.id.busy_summary_low);
		
		if(is_thread_handler == null)
			is_thread_handler = new UserInfoUpdateHandler();
		if(menu != null)
			is_thread_handler.menu(menu);
		
		// partition status
		is_thread_handler.partitionStatusView(partition_status_view);
		
		// some run information
		is_thread_handler.simpleTextViewList("run_parameters", "run_number", run_number_view);
		is_thread_handler.simpleTextViewList("run_parameters","T0_project_tag",project_tag_view);
		is_thread_handler.simpleTextViewList("run_parameters", "timeSOR", partition_start_time_view);
		is_thread_handler.bkgdColorTextViewList("run_ready4physics", "value", "True", atlas_ready4physics_view);
		
		// lhc status page
		is_thread_handler.simpleTextViewList("lhc_page_one_status", "ts", lhc_page_one_status_time_view);
		is_thread_handler.simpleTextViewList("lhc_page_one_status", "value", lhc_page_one_status_view);
		is_thread_handler.bkgdColorTextViewList("lhc_stable_beams_flag", "value", "1", lhc_stable_beams_flag_view);
		is_thread_handler.simpleTextViewList("lhc_beam_mode", "value", lhc_beam_mode_view);
		
		// detector mask
		is_thread_handler.detectorMaskTextViewList("TDAQ_MUON_CTP_INTERFACE", atlas_included_hw_muctpi);
		is_thread_handler.detectorMaskTextViewList("TDAQ_CTP", atlas_included_hw_ctp);
		is_thread_handler.detectorMaskTextViewList("TDAQ_CALO", atlas_included_hw_l1calo);
		is_thread_handler.detectorMaskTextViewList("TDAQ_HLT", atlas_included_hw_hlt);
		is_thread_handler.detectorMaskTextViewList("MUON_RPC", atlas_included_hw_rpc);
		is_thread_handler.detectorMaskTextViewList("MUON_TGC", atlas_included_hw_tgc);
		is_thread_handler.detectorMaskTextViewList("MUON_MDT", atlas_included_hw_mdt);
		is_thread_handler.detectorMaskTextViewList("MUON_CSC", atlas_included_hw_csc);
		is_thread_handler.detectorMaskTextViewList("LAR", atlas_included_hw_lar);
		is_thread_handler.detectorMaskTextViewList("TILECAL", atlas_included_hw_tile);
		is_thread_handler.detectorMaskTextViewList("PIXEL", atlas_included_hw_pix);
		is_thread_handler.detectorMaskTextViewList("SCT", atlas_included_hw_sct);
		is_thread_handler.detectorMaskTextViewList("TRT", atlas_included_hw_trt);
		is_thread_handler.detectorMaskTextViewList("FORWARD_LUCID", atlas_included_hw_lucid);
		is_thread_handler.detectorMaskTextViewList("FORWARD_BCM", atlas_included_hw_bcm);
		is_thread_handler.detectorMaskTextViewList("FORWARD_ALFA", atlas_included_hw_alfa);
		is_thread_handler.detectorMaskTextViewList("FORWARD_ZDC", atlas_included_hw_zdc);
		
		// busy status
		is_thread_handler.busyStatusTextViewList("ctpcore_moni0_rate", busy_summary_low);
		is_thread_handler.busyStatusTextViewList("ctpcore_moni1_rate", busy_summary_high);
		
		
		is_thread_handler.ParentActivity(this);
		
		if(is_info_thread == null)
			is_info_thread = new ISInfoUpdaterThread(is_thread_handler,this);
        
        // get update time
        //Log.v(TAG,"Setting syncFrequency");
        SetSyncFrequency();
        

        ISInfo partition_status = new ISInfo(partitionName,"RunCtrl","RootController",atlas_cookie);
        is_info_thread.addISInfo("partition_status", partition_status);
        ISInfo run_parameters = new ISInfo(partitionName,"RunParams","RunParams",atlas_cookie);
        is_info_thread.addISInfo("run_parameters", run_parameters);
        ISInfo run_ready4physics = new ISInfo(partitionName,"RunParams","Ready4Physics",atlas_cookie);
        is_info_thread.addISInfo("run_ready4physics", run_ready4physics);
        ISInfo lhc_page_one_status = new ISInfo("initial","LHC","LHCPage1Msg",atlas_cookie);
        is_info_thread.addISInfo("lhc_page_one_status", lhc_page_one_status);
        ISInfo lhc_stable_beams_flag = new ISInfo("initial","LHC","StableBeamsFlag",atlas_cookie);
        is_info_thread.addISInfo("lhc_stable_beams_flag", lhc_stable_beams_flag);
        ISInfo lhc_beam_mode = new ISInfo("initial","LHC","BeamMode",atlas_cookie);
        is_info_thread.addISInfo("lhc_beam_mode", lhc_beam_mode);
        ISInfo l1ct_busy_status = new ISInfo(partitionName,"L1CT-History","ISCTPBUSY",atlas_cookie);
        is_info_thread.addISInfo("l1ct_busy_status", l1ct_busy_status);
        
	}
	
	private void SetSyncFrequency(){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String str_update_delay_seconds = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_FREQ,"15");
		int update_delay_seconds = Integer.decode(str_update_delay_seconds);
        //Log.v(TAG,"pref value: "+update_delay_seconds);
        is_info_thread.update_delay_seconds(update_delay_seconds);
	}
	
	
	private void showSSO(){
		if(!LoginActivity.isActive){
			is_info_thread.pauseUpdate(true);
			//Log.v(TAG,"onCreate: create LoginActivity");
	        Intent loginIntent = new Intent(this,LoginActivity.class);
	        startActivityForResult(loginIntent,GET_COOKIE);
		}
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
	private final String TAG = "MainActivity";
}
