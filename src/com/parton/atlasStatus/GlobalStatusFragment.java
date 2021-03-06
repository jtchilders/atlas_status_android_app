package com.parton.atlasStatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class GlobalStatusFragment extends Fragment {
	
	public static GlobalStatusFragment newInstance(){
		return new GlobalStatusFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.v(TAG,"onCreateView: inside");
        // Inflate the layout for this fragment
		
		View view = inflater.inflate(R.layout.global_status, container, false);
		
//		if(view instanceof LinearLayout){
//			Log.v(TAG,"onCreateView: in linearlayout");
//			LinearLayout top_layout = (LinearLayout)view;
//			Log.v(TAG,"onCreateView: top_layout: "+top_layout.getChildCount());
//			
//			final double scaler = 0.8;
//			for(int i = 0;i<top_layout.getChildCount();++i){
//				View child = top_layout.getChildAt(i);
//				Log.v(TAG,"onCreateView: child: i = "+i);
//				if(child instanceof LinearLayout){
//					Log.v(TAG,"onCreateView: child is linearlayout");
//					LinearLayout layout = (LinearLayout)child;
//					for(int j=0;j<layout.getChildCount();++j){
//						Log.v(TAG,"onCreateView: layout_child: j = "+j);
//						View layout_child = layout.getChildAt(j);
//						if(layout_child instanceof TextView){
//							Log.v(TAG,"onCreateView: layout_child is textview ");
//							TextView text_view = (TextView)layout_child;
//							Log.v(TAG,"onCreateView: height = "+text_view.get);
//							text_view.setTextSize((float) (text_view.getHeight()*scaler));
//						}
//					}
//				}
//				else if(child instanceof TextView){
//					Log.v(TAG,"onCreateView: child is textview");
//					TextView text_view = (TextView)child;
//					Log.v(TAG,"onCreateView: "+child.getHeight());
//					text_view.setTextSize((float) (text_view.getHeight()*scaler));
//				}
//			}
//		}
//		
        return view;
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
//		Log.v(TAG,"onOptionsItemSelected: inside");
//		switch(item.getItemId()){
//		case R.id.menu_settings:
//			//Log.v(TAG,"onOptionsItemSelected: in menu Settings");
//			Intent settingsIntent = new Intent(this,SettingsActivity.class);
//	        startActivityForResult(settingsIntent,GET_SYNC_FREQ);
//			
//			return true;
//		case R.id.menu_about:
//			ShowAboutDialog();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
		return true;
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
//		Log.v(TAG,"onActivityResult: inside");
		// If the request went well (OK) and the request was GET_COOKIE
//	    if (resultCode == Activity.RESULT_OK){
//	    	switch(requestCode){
//	    	case GET_COOKIE:
////	    		Log.v(TAG,"onActivityResult: received cookie from LoginActivity");
//	    		
//	    		String returnedData = data.getStringExtra(LoginActivity.CookieReturnData);
//	    		if(returnedData == null){
////	    			Log.v(TAG,"onActivityResult: no CookieReturnData");
//	    			returnedData = data.getStringExtra(LoginActivity.ALREADY_RUNNING);
////	    			if(returnedData == null){
////	    				Log.v(TAG,"onActivityResult: LoginActivity returned with no result");
////	    			}
////	    			else{
////	    				Log.v(TAG,"onActivityResult: LoginActivity returned because it's already running");
////	    			}
//	    		}
//	    		else{
//	    			cern_cookie.setTime();
//			        cern_cookie.cookie(returnedData);
//			        cern_cookie.setIp();
//	    		}
//	    		
//		        
//		        
//		        if(is_info_thread != null){
//		        	is_info_thread.updateCookie(cern_cookie);
//		        	// need to restart thread if it is waiting for a new cookie
//		        	is_info_thread.unpause();
//		        }
////		        else{
////		        	Log.v(TAG,"onActivityResult: thread is null!");
////		        }
//		        
//		        break;
//	    	case GET_SYNC_FREQ:
//	    		is_info_thread.UpdateSyncFrequency();
//	    		break;
////	    	default:
////	    		Log.w(TAG,"onActivityResult: Activity returned unknown result.");
//	    	}
//		}
////    	else{
////    		Log.w(TAG,"onActivityResult: Activity returned and RESULT_OK not set.");
////    	}
	    
	    
	}
	
	
//	private void getNewISInfoUpdaterThread(){
		//Log.v(TAG,"getNewISInfoUpdaterThread: inside");
//		PartitionStatusView partition_status_view = (PartitionStatusView) findViewById(R.id.atlas_partition_status);
//		TextView run_number_view = (TextView) findViewById(R.id.atlas_run_number);
//		TextView project_tag_view = (TextView) findViewById(R.id.atlas_project_tag);
//		TextView partition_start_time_view = (TextView) findViewById(R.id.atlas_partition_start_time);
//		TextView lhc_page_one_status_time_view = (TextView) findViewById(R.id.lhc_page_one_status_time);
//		TextView lhc_page_one_status_view = (TextView) findViewById(R.id.lhc_page_one_status);
//		TextView atlas_included_hw_muctpi = (TextView) findViewById(R.id.atlas_included_hw_muctpi);
//		TextView atlas_included_hw_ctp = (TextView) findViewById(R.id.atlas_included_hw_ctp);
//		TextView atlas_included_hw_l1calo = (TextView) findViewById(R.id.atlas_included_hw_l1calo);
//		TextView atlas_included_hw_hlt = (TextView) findViewById(R.id.atlas_included_hw_hlt);
//		TextView atlas_included_hw_rpc = (TextView) findViewById(R.id.atlas_included_hw_rpc);
//		TextView atlas_included_hw_tgc = (TextView) findViewById(R.id.atlas_included_hw_tgc);
//		TextView atlas_included_hw_mdt = (TextView) findViewById(R.id.atlas_included_hw_mdt);
//		TextView atlas_included_hw_csc = (TextView) findViewById(R.id.atlas_included_hw_csc);
//		TextView atlas_included_hw_lar = (TextView) findViewById(R.id.atlas_included_hw_lar);
//		TextView atlas_included_hw_tile = (TextView) findViewById(R.id.atlas_included_hw_tile);
//		TextView atlas_included_hw_pix = (TextView) findViewById(R.id.atlas_included_hw_pix);
//		TextView atlas_included_hw_sct = (TextView) findViewById(R.id.atlas_included_hw_sct);
//		TextView atlas_included_hw_trt = (TextView) findViewById(R.id.atlas_included_hw_trt);
//		TextView atlas_included_hw_lucid = (TextView) findViewById(R.id.atlas_included_hw_lucid);
//		TextView atlas_included_hw_bcm = (TextView) findViewById(R.id.atlas_included_hw_bcm);
//		TextView atlas_included_hw_alfa = (TextView) findViewById(R.id.atlas_included_hw_alfa);
//		TextView atlas_included_hw_zdc = (TextView) findViewById(R.id.atlas_included_hw_zdc);
//		TextView atlas_ready4physics_view = (TextView) findViewById(R.id.atlas_ready4physics);
//		TextView lhc_stable_beams_flag_view = (TextView) findViewById(R.id.lhc_stable_beams_flag);
//		TextView lhc_beam_mode_view = (TextView) findViewById(R.id.lhc_beam_mode);
//		
//		// busy
//		TextView busy_summary_high = (TextView) findViewById(R.id.busy_summary_high);
//		TextView busy_summary_low = (TextView) findViewById(R.id.busy_summary_low);
//		
//		if(is_thread_handler == null)
//			is_thread_handler = new UserInfoUpdateHandler(this);
//		if(menu != null)
//			is_thread_handler.menu(menu);
//		
//		// partition status
//		is_thread_handler.partitionStatusView(partition_status_view);
//		
//		// some run information
//		is_thread_handler.simpleTextViewList("run_parameters", "run_number", run_number_view);
//		is_thread_handler.simpleTextViewList("run_parameters","T0_project_tag",project_tag_view);
//		is_thread_handler.simpleTextViewList("run_parameters", "timeSOR", partition_start_time_view);
//		is_thread_handler.bkgdColorTextViewList("run_ready4physics", "value", "True", atlas_ready4physics_view);
//		
//		// lhc status page
//		is_thread_handler.simpleTextViewList("lhc_page_one_status", "ts", lhc_page_one_status_time_view);
//		is_thread_handler.simpleTextViewList("lhc_page_one_status", "value", lhc_page_one_status_view);
//		is_thread_handler.bkgdColorTextViewList("lhc_stable_beams_flag", "value", "1", lhc_stable_beams_flag_view);
//		is_thread_handler.simpleTextViewList("lhc_beam_mode", "value", lhc_beam_mode_view);
//		
//		// detector mask
//		is_thread_handler.detectorMaskTextViewList("TDAQ_MUON_CTP_INTERFACE", atlas_included_hw_muctpi);
//		is_thread_handler.detectorMaskTextViewList("TDAQ_CTP", atlas_included_hw_ctp);
//		is_thread_handler.detectorMaskTextViewList("TDAQ_CALO", atlas_included_hw_l1calo);
//		is_thread_handler.detectorMaskTextViewList("TDAQ_HLT", atlas_included_hw_hlt);
//		is_thread_handler.detectorMaskTextViewList("MUON_RPC", atlas_included_hw_rpc);
//		is_thread_handler.detectorMaskTextViewList("MUON_TGC", atlas_included_hw_tgc);
//		is_thread_handler.detectorMaskTextViewList("MUON_MDT", atlas_included_hw_mdt);
//		is_thread_handler.detectorMaskTextViewList("MUON_CSC", atlas_included_hw_csc);
//		is_thread_handler.detectorMaskTextViewList("LAR", atlas_included_hw_lar);
//		is_thread_handler.detectorMaskTextViewList("TILECAL", atlas_included_hw_tile);
//		is_thread_handler.detectorMaskTextViewList("PIXEL", atlas_included_hw_pix);
//		is_thread_handler.detectorMaskTextViewList("SCT", atlas_included_hw_sct);
//		is_thread_handler.detectorMaskTextViewList("TRT", atlas_included_hw_trt);
//		is_thread_handler.detectorMaskTextViewList("FORWARD_LUCID", atlas_included_hw_lucid);
//		is_thread_handler.detectorMaskTextViewList("FORWARD_BCM", atlas_included_hw_bcm);
//		is_thread_handler.detectorMaskTextViewList("FORWARD_ALFA", atlas_included_hw_alfa);
//		is_thread_handler.detectorMaskTextViewList("FORWARD_ZDC", atlas_included_hw_zdc);
//		
//		// busy status
//		is_thread_handler.busyStatusTextViewList("ctpcore_moni0_rate", busy_summary_low);
//		is_thread_handler.busyStatusTextViewList("ctpcore_moni1_rate", busy_summary_high);
		
		
//		is_thread_handler.ParentActivity(this);
		
//		if(is_info_thread == null)
//			is_info_thread = new ISInfoUpdaterThread(is_thread_handler,this);
        
        // get update time
        //Log.v(TAG,"Setting syncFrequency");
//        SetSyncFrequency();
        

//        ISInfo partition_status = new ISInfo(TDAQ_PARTITION,"RunCtrl","RootController",cern_cookie);
//        is_info_thread.addISInfo("partition_status", partition_status);
//        ISInfo run_parameters = new ISInfo(TDAQ_PARTITION,"RunParams","RunParams",cern_cookie);
//        is_info_thread.addISInfo("run_parameters", run_parameters);
//        ISInfo run_ready4physics = new ISInfo(TDAQ_PARTITION,"RunParams","Ready4Physics",cern_cookie);
//        is_info_thread.addISInfo("run_ready4physics", run_ready4physics);
//        ISInfo lhc_page_one_status = new ISInfo(INITIAL_PARTITION,"LHC","LHCPage1Msg",cern_cookie);
//        is_info_thread.addISInfo("lhc_page_one_status", lhc_page_one_status);
//        ISInfo lhc_stable_beams_flag = new ISInfo(INITIAL_PARTITION,"LHC","StableBeamsFlag",cern_cookie);
//        is_info_thread.addISInfo("lhc_stable_beams_flag", lhc_stable_beams_flag);
//        ISInfo lhc_beam_mode = new ISInfo(INITIAL_PARTITION,"LHC","BeamMode",cern_cookie);
//        is_info_thread.addISInfo("lhc_beam_mode", lhc_beam_mode);
//        ISInfo l1ct_busy_status = new ISInfo(TDAQ_PARTITION,"L1CT-History","ISCTPBUSY",cern_cookie);
//        is_info_thread.addISInfo("l1ct_busy_status", l1ct_busy_status);
        
//	}
//	
//	private void SetSyncFrequency(){
//		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//		String str_update_delay_seconds = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_FREQ,"15");
//		int update_delay_seconds = Integer.decode(str_update_delay_seconds);
//        //Log.v(TAG,"pref value: "+update_delay_seconds);
//        is_info_thread.update_delay_seconds(update_delay_seconds);
//	}
	
	
//	public void showSSO(){
//		if(!LoginActivity.isActive){
//			Log.v(TAG,"showSSO: starting Login");
//			if(is_info_thread != null)
//				is_info_thread.pauseUpdate(true);
//			//Log.v(TAG,"onCreate: create LoginActivity");
//	        Intent loginIntent = new Intent(this,LoginActivity.class);
//	        startActivityForResult(loginIntent,GET_COOKIE);
//		}
//	}
	
//	public void unpauseThread(){
//		if(is_info_thread.pauseUpdate()){
//			is_info_thread.pauseUpdate(false);
//        	is_info_thread.interrupt();
//		}
//	}
	
//	private void ShowAboutDialog(){
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setCancelable(true);
//		builder.setMessage("ATLAS Status\nWritten By: Taylor Childers (CERN)\nDeveloped for expert on-call monitoring of current operations activities.");
//		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//			public void onCancel(DialogInterface dialog) {
//				return;
//			}
//		});
//		builder.setNegativeButton(R.string.no_network_connection_cancel, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				return;
//			}
//		});
//
//		builder.show();
//	}
	
	
	
//	private final static int GET_SYNC_FREQ = 44;
//	public final static int GET_COOKIE = 33;
//	private final String TAG = "GlobalStatusFragment";
	
}
