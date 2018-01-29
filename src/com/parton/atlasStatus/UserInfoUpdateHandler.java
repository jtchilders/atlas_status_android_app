package com.parton.atlasStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.parton.atlasStatus.R;

public class UserInfoUpdateHandler extends Handler{
	
	private HashMap<String, ISInfo> ISInfoList = null;
	
	public UserInfoUpdateHandler(MainActivity ParentActivity,Menu menu){
		super();
//		Log.v(TAG,"UserInfoUpdateHandler: inside");
		ParentActivity(ParentActivity);
		menu(menu);
		AddViews();
	}
	
	
	private MainActivity ParentActivity = null;
	public void ParentActivity(MainActivity context){
		ParentActivity = context;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg){
		if(msg.arg1 == ISInfoUpdaterThread.UPDATE_VIEWS){
//			Log.v(TAG,"handleMessage: updating views");
			if(msg.obj.getClass().getName().contains("java.util.HashMap")){
				ISInfoList = (HashMap<String, ISInfo>) msg.obj;
				updatePartitionStatusView();
				updateTextViews();
//				updateBusyStatusTextViews();
				updateBkgdColorTextViews();
				updateDetectorMaskTextViews();
				updateMenuTime();
			}
		}
		else if(msg.arg1 == ISInfoUpdaterThread.NO_CONNECTION){
//			Log.v(TAG,"handleMessage: no connection message received");
			NetworkStatusChecker.ShowToast(ParentActivity,"No internet connection");
		}
//		else if(msg.arg1 == ISInfoUpdaterThread.CONNECTION_TYPE_CHANGE){
			 //Log.v(TAG,"onCreate: create LoginActivity");
//			if(!LoginActivity.isActive){
//				NetworkStatusChecker.ShowToast(ParentActivity,"Network Changed, need to re-signin");
//				Intent loginIntent = new Intent(ParentActivity,LoginActivity.class);
//				ParentActivity.startActivityForResult(loginIntent,MainActivity.GET_COOKIE);
//			}
//		}
		else if(msg.arg1 == ISInfoUpdaterThread.XML_IS_SSO){
			if(!LoginActivity.isActive){
				NetworkStatusChecker.ShowToast(ParentActivity, "new cookie needed, please re-signin");
				Intent loginIntent = new Intent(ParentActivity,LoginActivity.class);
				ParentActivity.startActivityForResult(loginIntent,MainActivity.GET_COOKIE);
			}
		}
	}
	
	//////////////////////
	// Partition View
	/////////////////////////////////
	private void updatePartitionStatusView(){
		if(ISInfoList == null){
//			Log.w(TAG,"updatePartitionStatusView: ISInfoList is null");
			return;
		}
		
		ISInfo info = ISInfoList.get("partition_status");
		if(info == null){
//			Log.w(TAG,"updatePartitionStatusView: no ISInfo object named: "+"partition_status");
			return;
		}
		
		ISObjectAttr attr = info.getAttr("state");
		if(attr == null){
//			Log.w(TAG,"updatePartitionStatusView: no attr named: "+"state");
			return;
		}
		
		PartitionStatusView view = (PartitionStatusView) ParentActivity.findViewById(R.id.atlas_partition_status);
		if(view == null){
//			Log.w(TAG,"updatePartitionStatusView: view is null");
			return;
		}
		
		view.setText(attr.values(0));
		
		
	}
	

	//////////////////////
	// Simple Text Views
	/////////////////////////////////
	
	private HashMap<String,HashMap<String,Integer>> simpleTextViewList = new HashMap<String,HashMap<String,Integer>>();
	public void simpleTextViewList(String is_info_name,String is_attr_name,int text_view_id){
		if(simpleTextViewList.containsKey(is_info_name)){
			simpleTextViewList.get(is_info_name).put(is_attr_name, text_view_id);
		}
		else{
			HashMap<String,Integer> tmp = new HashMap<String,Integer>();
			tmp.put(is_attr_name, text_view_id);
			simpleTextViewList.put(is_info_name, tmp);
		}
	}
	
	private void updateTextView(String is_info_name,String is_attr_name,int text_view_id){
		if(ISInfoList == null){
//			Log.w(TAG,"updateTextView: ISInfoList is null");
			return;
		}
		
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"updateTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		ISObjectAttr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"updateTextView: no attr named: "+is_attr_name);
			return;
		}
		
		TextView view = (TextView)ParentActivity.findViewById(text_view_id);
		if(view == null){
//			Log.w(TAG,"updateTextView: view is null");
			return;
		}
		
		view.setText(attr.values(0));
	}
	
	@SuppressWarnings("unchecked")
	private void updateTextViews(){
		
		for(int i=0;i<simpleTextViewList.size();++i){
			String is_info_name = (String) simpleTextViewList.keySet().toArray()[i];
			HashMap<String,TextView> tmp = (HashMap<String, TextView>) simpleTextViewList.values().toArray()[i];
			for(int j=0;j<tmp.size();++j){
				String is_attr_name = (String) tmp.keySet().toArray()[j];
				int text_view_id = (Integer) tmp.values().toArray()[j];
				updateTextView(is_info_name,is_attr_name,text_view_id);
			}
		}
	}
	

	//////////////////////
	// Text Views with changing background colors
	/////////////////////////////////////////////////////
	
	private HashMap<String,HashMap<String,HashMap<String,Integer>>> bkgdColorTextViewList = new HashMap<String,HashMap<String,HashMap<String,Integer>>>();
	public void bkgdColorTextViewList(String is_info_name,String is_attr_name,String attr_value_for_green,int text_view_id){
		if(!bkgdColorTextViewList.containsKey(is_info_name)){
			HashMap<String,Integer> innermost = new HashMap<String,Integer>();
			innermost.put(attr_value_for_green, text_view_id);
			
			HashMap<String,HashMap<String,Integer>> middle = new HashMap<String,HashMap<String,Integer>>();
			middle.put(is_attr_name, innermost);
			
			bkgdColorTextViewList.put(is_info_name, middle);
		}
		else{
			HashMap<String,HashMap<String,Integer>> middle = (HashMap<String,HashMap<String,Integer>>) bkgdColorTextViewList.get(is_info_name);
			if(!middle.containsKey(is_attr_name)){
				HashMap<String,Integer> innermost = new HashMap<String,Integer>();
				innermost.put(attr_value_for_green, text_view_id);
				middle.put(is_attr_name, innermost);
			}
			else{
				HashMap<String,Integer> innermost = (HashMap<String,Integer>)middle.get(is_attr_name);
				if(!innermost.containsKey(attr_value_for_green)){
					innermost.put(attr_value_for_green, text_view_id);
				}
				else{
					innermost.remove(attr_value_for_green);
					innermost.put(attr_value_for_green, text_view_id);
				}
			}
		}
	}
	
	private void updateBkgdColorTextView(String is_info_name,String is_attr_name,String attr_value_for_green,int text_view_id){
		if(ISInfoList == null){
//			Log.w(TAG,"updateBkgdColorTextView: ISInfoList is null");
			return;
		}
		
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"updateBkgdColorTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		ISObjectAttr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"updateBkgdColorTextView: no attr named: "+is_attr_name);
			return;
		}
		
		TextView view = (TextView)ParentActivity.findViewById(text_view_id);
		if(view == null){
//			Log.w(TAG,"updateBkgdColorTextView: view is null");
			return;
		}
		
		if(attr.values(0).contains(attr_value_for_green))
			view.setBackgroundResource(R.color.green);
		else
			view.setBackgroundResource(R.color.red);
	}
	
	@SuppressWarnings("unchecked")
	private void updateBkgdColorTextViews(){
		
		for(int i=0;i<bkgdColorTextViewList.size();++i){
			String is_info_name = (String) bkgdColorTextViewList.keySet().toArray()[i];
			HashMap<String, HashMap<String,Integer>> middle = (HashMap<String, HashMap<String, Integer>>) bkgdColorTextViewList.values().toArray()[i];
			for(int j=0;j<middle.size();++j){
				String is_attr_name = (String) middle.keySet().toArray()[j];
				HashMap<String,Integer> innermost = (HashMap<String,Integer>) middle.values().toArray()[j];
				for(int k=0;k<innermost.size();++k){
					String attr_value_for_green = (String) innermost.keySet().toArray()[j];
					int text_view_id = (Integer) innermost.values().toArray()[j];
					updateBkgdColorTextView(is_info_name,is_attr_name,attr_value_for_green,text_view_id);
				}
				
			}
		}
	}
	

	//////////////////////
	// Text Views for detector masks
	/////////////////////////////////////////////////////
	
	private HashMap<String,Integer> detectorMaskTextViewList = new HashMap<String,Integer>();
	public void detectorMaskTextViewList(String hw_name,int text_view_id){
		detectorMaskTextViewList.put(hw_name, text_view_id);
	}
	
	private void updateDetectorMaskTextView(String detector_name, int text_view_id){
		if(ISInfoList == null){
//			Log.w(TAG,"updateDetectorMaskTextView: ISInfoList is null");
			return;
		}
		
		String is_info_name = "run_parameters";
		String is_attr_name = "detector_mask";
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"updateDetectorMaskTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		ISObjectAttr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"updateDetectorMaskTextView: no attr named: "+is_attr_name);
			return;
		}
		
		TextView view = (TextView)ParentActivity.findViewById(text_view_id);
		if(view == null){
//			Log.w(TAG,"updateDetectorMaskTextView: text_view is null");
			return;
		}
		
		DetectorMaskDecoder detMask = new DetectorMaskDecoder(attr.values(0));
		
		if(detMask.isIncluded(detector_name))
			view.setBackgroundResource(R.color.detector_included);
		else
			view.setBackgroundResource(R.color.detector_excluded);
	}

	private void updateDetectorMaskTextViews(){
		
		for(int i=0;i<detectorMaskTextViewList.size();++i){
			String hw_name = (String) detectorMaskTextViewList.keySet().toArray()[i];
			int text_view_id = (Integer) detectorMaskTextViewList.values().toArray()[i];
			updateDetectorMaskTextView(hw_name,text_view_id);
		}
	}

	//////////////////////
	// Update Menu to read last update time
	/////////////////////////////////////////////////////
	
	private Menu menu = null;
	public void menu(Menu menu){
		this.menu = menu;
	}
	public Menu menu(){
		return menu;
	}
	
	private void updateMenuTime(){
//		(TAG,"updateMenuTime: inside");
		if(menu != null){
			long timeMillis = System.currentTimeMillis();
			Date date = new Date(timeMillis);
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			String dateFormatted = formatter.format(date);
			MenuItem item = menu.findItem(R.id.menu_last_update_time);
			item.setTitle("Update at: " + dateFormatted);
		}
//		else
//			Log.w(TAG,"updateMenuTime: menu not set");
	}
	
	

	//////////////////////
	// Text Views for Busy Status
	/////////////////////////////////////////////////////
	
//	private final float busy_warning_level = 5; // % busy
//	private HashMap<String,Integer> busyStatusTextViewList = new HashMap<String,Integer>();
//	public void busyStatusTextViewList(String is_attr_name,int text_view_id){
//		busyStatusTextViewList.put(is_attr_name, text_view_id);
//	}
//	
//	public void updateBusyStatusTextView(String is_attr_name,int text_view_id){
//		if(ISInfoList == null)
//			return;
//		
//		String is_info_name = "l1ct_busy_status";
//		ISInfo info = ISInfoList.get(is_info_name);
//		if(info == null){
////			Log.w(TAG,"busyStatusTextViewList: no ISInfo object named: "+is_info_name);
//			return;
//		}
//		
//		ISObjectAttr attr = info.getAttr(is_attr_name);
//		if(attr == null){
////			Log.w(TAG,"busyStatusTextViewList: no attr named: "+is_attr_name);
//			return;
//		}
//		
//		TextView view = (TextView)ParentActivity.findViewById(text_view_id);
//		if(view == null){
////			Log.w(TAG,"busyStatusTextViewList: view is null");
//			return;
//		}
//		
//		view.setText(attr.values(0).substring(0,3)+"%");
//		float busy = Float.parseFloat(attr.values(0));
//		if(busy > busy_warning_level){
//			view.setTextColor(ParentActivity.getResources().getColor(R.color.red));
//		}
//		else{
//			view.setTextColor(ParentActivity.getResources().getColor(R.color.green));
//		}
//		
//		
//	}
//
//	private void updateBusyStatusTextViews(){
//		
//		for(int i=0;i<busyStatusTextViewList.size();++i){
//			String attr_name = (String) busyStatusTextViewList.keySet().toArray()[i];
//			int text_view_id = (Integer) busyStatusTextViewList.values().toArray()[i];
//			updateBusyStatusTextView(attr_name,text_view_id);
//		}
//	}
	

	
	private void AddViews(){
//		Log.v(TAG,"AddViews: inside");

		// some run information
		simpleTextViewList("run_parameters", "run_number", R.id.atlas_run_number);
		simpleTextViewList("run_parameters","T0_project_tag",R.id.atlas_project_tag);
		simpleTextViewList("run_parameters", "timeSOR", R.id.atlas_partition_start_time);
		bkgdColorTextViewList("run_ready4physics", "value", "True", R.id.atlas_ready4physics);
		
		// lhc status page
		simpleTextViewList("lhc_page_one_status", "ts", R.id.lhc_page_one_status_time);
		simpleTextViewList("lhc_page_one_status", "value", R.id.lhc_page_one_status);
		bkgdColorTextViewList("lhc_stable_beams_flag", "value", "1", R.id.lhc_stable_beams_flag);
		simpleTextViewList("lhc_beam_mode", "value", R.id.lhc_beam_mode);
		
		// detector mask
		detectorMaskTextViewList("TDAQ_MUON_CTP_INTERFACE", R.id.atlas_included_hw_muctpi);
		detectorMaskTextViewList("TDAQ_CTP", R.id.atlas_included_hw_ctp);
		detectorMaskTextViewList("TDAQ_CALO", R.id.atlas_included_hw_l1calo);
		detectorMaskTextViewList("TDAQ_HLT", R.id.atlas_included_hw_hlt);
		detectorMaskTextViewList("MUON_RPC", R.id.atlas_included_hw_rpc);
		detectorMaskTextViewList("MUON_TGC", R.id.atlas_included_hw_tgc);
		detectorMaskTextViewList("MUON_MDT", R.id.atlas_included_hw_mdt);
		detectorMaskTextViewList("MUON_CSC", R.id.atlas_included_hw_csc);
		detectorMaskTextViewList("LAR", R.id.atlas_included_hw_lar);
		detectorMaskTextViewList("TILECAL", R.id.atlas_included_hw_tile);
		detectorMaskTextViewList("PIXEL", R.id.atlas_included_hw_pix);
		detectorMaskTextViewList("SCT", R.id.atlas_included_hw_sct);
		detectorMaskTextViewList("TRT", R.id.atlas_included_hw_trt);
		detectorMaskTextViewList("FORWARD_LUCID", R.id.atlas_included_hw_lucid);
		detectorMaskTextViewList("FORWARD_BCM", R.id.atlas_included_hw_bcm);
		detectorMaskTextViewList("FORWARD_ALFA", R.id.atlas_included_hw_alfa);
		detectorMaskTextViewList("FORWARD_ZDC", R.id.atlas_included_hw_zdc);
		
		// busy status
//		busyStatusTextViewList("ctpcore_moni0_rate", R.id.busy_summary_low);
//		busyStatusTextViewList("ctpcore_moni1_rate", R.id.busy_summary_high);
		
		
	}
	
	
//	private final String TAG = "UserInfoUpdaterHandler";
}
