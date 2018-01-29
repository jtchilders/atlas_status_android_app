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
			if(msg.obj.getClass().getName().contains("java.util.HashMap")){
				ISInfoList = (HashMap<String, ISInfo>) msg.obj;
				updatePartitionStatusView();
				updateTextViews();
				updateBusyStatusTextViews();
				updateBkgdColorTextViews();
				updateDetectorMaskTextViews();
				updateMenuTime();
			}
		}
		else if(msg.arg1 == ISInfoUpdaterThread.NO_CONNECTION){
//			Log.v(TAG,"no connection message received");
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
	

	private PartitionStatusView partitionStatusView = null;
	public void partitionStatusView(PartitionStatusView partitionStatusView){
		this.partitionStatusView = partitionStatusView;
	}
	public PartitionStatusView partitionStatusView(){
		return partitionStatusView;
	}

	private void updatePartitionStatusView(){
		if(ISInfoList == null) return;
		
		ISInfo info = ISInfoList.get("partition_status");
		if(info == null){
//			Log.w(TAG,"updatePartitionStatusView: no ISInfo object named: "+"partition_status");
			return;
		}
		
		IS_XML_Attr attr = info.getAttr("state");
		if(attr == null){
//			Log.w(TAG,"updatePartitionStatusView: no attr named: "+"state");
			return;
		}
		
		if(partitionStatusView == null){
//			Log.w(TAG,"updatePartitionStatusView: partitionStatusView is null");
			return;
		}
		
		partitionStatusView.setText(attr.value());
		
		
	}
	
	private HashMap<String,HashMap<String,TextView>> simpleTextViewList = new HashMap<String,HashMap<String,TextView>>();
	public void simpleTextViewList(String is_info_name,String is_attr_name,TextView text_view){
		if(simpleTextViewList.containsKey(is_info_name)){
			simpleTextViewList.get(is_info_name).put(is_attr_name, text_view);
		}
		else{
			HashMap<String,TextView> tmp = new HashMap<String,TextView>();
			tmp.put(is_attr_name, text_view);
			simpleTextViewList.put(is_info_name, tmp);
		}
	}
	
	private void updateTextView(String is_info_name,String is_attr_name,TextView text_view){
		if(ISInfoList == null) return;
		
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"updateTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"updateTextView: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
//			Log.w(TAG,"updateTextView: text_view is null");
			return;
		}
		
		text_view.setText(attr.value());
	}
	
	@SuppressWarnings("unchecked")
	private void updateTextViews(){
		
		for(int i=0;i<simpleTextViewList.size();++i){
			String is_info_name = (String) simpleTextViewList.keySet().toArray()[i];
			HashMap<String,TextView> tmp = (HashMap<String, TextView>) simpleTextViewList.values().toArray()[i];
			for(int j=0;j<tmp.size();++j){
				String is_attr_name = (String) tmp.keySet().toArray()[j];
				TextView text_view = (TextView) tmp.values().toArray()[j];
				updateTextView(is_info_name,is_attr_name,text_view);
			}
		}
	}
	

	private HashMap<String,HashMap<String,HashMap<String,TextView>>> bkgdColorTextViewList = new HashMap<String,HashMap<String,HashMap<String,TextView>>>();
	public void bkgdColorTextViewList(String is_info_name,String is_attr_name,String attr_value_for_green,TextView text_view){
		if(!bkgdColorTextViewList.containsKey(is_info_name)){
			HashMap<String,TextView> innermost = new HashMap<String,TextView>();
			innermost.put(attr_value_for_green, text_view);
			
			HashMap<String,HashMap<String,TextView>> middle = new HashMap<String,HashMap<String,TextView>>();
			middle.put(is_attr_name, innermost);
			
			bkgdColorTextViewList.put(is_info_name, middle);
		}
		else{
			HashMap<String,HashMap<String,TextView>> middle = (HashMap<String,HashMap<String,TextView>>) bkgdColorTextViewList.get(is_info_name);
			if(!middle.containsKey(is_attr_name)){
				HashMap<String,TextView> innermost = new HashMap<String,TextView>();
				innermost.put(attr_value_for_green, text_view);
				middle.put(is_attr_name, innermost);
			}
			else{
				HashMap<String,TextView> innermost = (HashMap<String,TextView>)middle.get(is_attr_name);
				if(!innermost.containsKey(attr_value_for_green)){
					innermost.put(attr_value_for_green, text_view);
				}
				else{
					innermost.remove(attr_value_for_green);
					innermost.put(attr_value_for_green, text_view);
				}
			}
		}
	}
	
	private void updateBkgdColorTextView(String is_info_name,String is_attr_name,String attr_value_for_green,TextView text_view){
		if(ISInfoList == null) return;
		
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"updateTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"updateTextView: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
//			Log.w(TAG,"updateTextView: text_view is null");
			return;
		}
		
		if(attr.value().contains(attr_value_for_green))
			text_view.setBackgroundResource(R.color.green);
		else
			text_view.setBackgroundResource(R.color.red);
	}
	
	@SuppressWarnings("unchecked")
	private void updateBkgdColorTextViews(){
		
		for(int i=0;i<bkgdColorTextViewList.size();++i){
			String is_info_name = (String) bkgdColorTextViewList.keySet().toArray()[i];
			HashMap<String, HashMap<String,TextView>> middle = (HashMap<String, HashMap<String, TextView>>) bkgdColorTextViewList.values().toArray()[i];
			for(int j=0;j<middle.size();++j){
				String is_attr_name = (String) middle.keySet().toArray()[j];
				HashMap<String,TextView> innermost = (HashMap<String,TextView>) middle.values().toArray()[j];
				for(int k=0;k<innermost.size();++k){
					String attr_value_for_green = (String) innermost.keySet().toArray()[j];
					TextView text_view = (TextView) innermost.values().toArray()[j];
					updateBkgdColorTextView(is_info_name,is_attr_name,attr_value_for_green,text_view);
				}
				
			}
		}
	}
	
	
	private HashMap<String,TextView> detectorMaskTextViewList = new HashMap<String,TextView>();
	public void detectorMaskTextViewList(String hw_name,TextView text_view){
		detectorMaskTextViewList.put(hw_name, text_view);
	}
	
	private void updateDetectorMaskTextView(String detector_name, TextView text_view){
		if(ISInfoList == null) return;
		
		String is_info_name = "run_parameters";
		String is_attr_name = "detector_mask";
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"updateDetectorMaskTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"updateDetectorMaskTextView: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
//			Log.w(TAG,"updateDetectorMaskTextView: text_view is null");
			return;
		}
		
		DetectorMaskDecoder detMask = new DetectorMaskDecoder(attr.value());
		
		if(detMask.isIncluded(detector_name))
			text_view.setBackgroundResource(R.color.detector_included);
		else
			text_view.setBackgroundResource(R.color.detector_excluded);
	}

	private void updateDetectorMaskTextViews(){
		
		for(int i=0;i<detectorMaskTextViewList.size();++i){
			String hw_name = (String) detectorMaskTextViewList.keySet().toArray()[i];
			TextView text_view = (TextView) detectorMaskTextViewList.values().toArray()[i];
			updateDetectorMaskTextView(hw_name,text_view);
		}
	}
	
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
	
	private final float busy_warning_level = 5; // % busy
	private HashMap<String,TextView> busyStatusTextViewList = new HashMap<String,TextView>();
	public void busyStatusTextViewList(String is_attr_name,TextView text_view){
		busyStatusTextViewList.put(is_attr_name, text_view);
	}
	
	public void updateBusyStatusTextView(String is_attr_name,TextView text_view){
		if(ISInfoList == null)
			return;
		
		String is_info_name = "l1ct_busy_status";
		ISInfo info = ISInfoList.get(is_info_name);
		if(info == null){
//			Log.w(TAG,"busyStatusTextViewList: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
//			Log.w(TAG,"busyStatusTextViewList: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
//			Log.w(TAG,"busyStatusTextViewList: text_view is null");
			return;
		}
		
		text_view.setText(attr.value().substring(0,3)+"%");
		float busy = Float.parseFloat(attr.value());
		if(busy > busy_warning_level){
			text_view.setTextColor(ParentActivity.getResources().getColor(R.color.red));
		}
		else{
			text_view.setTextColor(ParentActivity.getResources().getColor(R.color.green));
		}
		
		
	}

	private void updateBusyStatusTextViews(){
		
		for(int i=0;i<busyStatusTextViewList.size();++i){
			String attr_name = (String) busyStatusTextViewList.keySet().toArray()[i];
			TextView text_view = (TextView) busyStatusTextViewList.values().toArray()[i];
			updateBusyStatusTextView(attr_name,text_view);
		}
	}
	
	private void AddViews(){
		PartitionStatusView partition_status_view = (PartitionStatusView) ParentActivity.findViewById(R.id.atlas_partition_status);
		TextView run_number_view = (TextView) ParentActivity.findViewById(R.id.atlas_run_number);
		TextView project_tag_view = (TextView) ParentActivity.findViewById(R.id.atlas_project_tag);
		TextView partition_start_time_view = (TextView) ParentActivity.findViewById(R.id.atlas_partition_start_time);
		TextView lhc_page_one_status_time_view = (TextView) ParentActivity.findViewById(R.id.lhc_page_one_status_time);
		TextView lhc_page_one_status_view = (TextView) ParentActivity.findViewById(R.id.lhc_page_one_status);
		TextView atlas_included_hw_muctpi = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_muctpi);
		TextView atlas_included_hw_ctp = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_ctp);
		TextView atlas_included_hw_l1calo = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_l1calo);
		TextView atlas_included_hw_hlt = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_hlt);
		TextView atlas_included_hw_rpc = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_rpc);
		TextView atlas_included_hw_tgc = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_tgc);
		TextView atlas_included_hw_mdt = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_mdt);
		TextView atlas_included_hw_csc = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_csc);
		TextView atlas_included_hw_lar = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_lar);
		TextView atlas_included_hw_tile = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_tile);
		TextView atlas_included_hw_pix = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_pix);
		TextView atlas_included_hw_sct = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_sct);
		TextView atlas_included_hw_trt = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_trt);
		TextView atlas_included_hw_lucid = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_lucid);
		TextView atlas_included_hw_bcm = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_bcm);
		TextView atlas_included_hw_alfa = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_alfa);
		TextView atlas_included_hw_zdc = (TextView) ParentActivity.findViewById(R.id.atlas_included_hw_zdc);
		TextView atlas_ready4physics_view = (TextView) ParentActivity.findViewById(R.id.atlas_ready4physics);
		TextView lhc_stable_beams_flag_view = (TextView) ParentActivity.findViewById(R.id.lhc_stable_beams_flag);
		TextView lhc_beam_mode_view = (TextView) ParentActivity.findViewById(R.id.lhc_beam_mode);
		
		// busy
		TextView busy_summary_high = (TextView) ParentActivity.findViewById(R.id.busy_summary_high);
		TextView busy_summary_low = (TextView) ParentActivity.findViewById(R.id.busy_summary_low);
		

		// partition status
		partitionStatusView(partition_status_view);
		
		// some run information
		simpleTextViewList("run_parameters", "run_number", run_number_view);
		simpleTextViewList("run_parameters","T0_project_tag",project_tag_view);
		simpleTextViewList("run_parameters", "timeSOR", partition_start_time_view);
		bkgdColorTextViewList("run_ready4physics", "value", "True", atlas_ready4physics_view);
		
		// lhc status page
		simpleTextViewList("lhc_page_one_status", "ts", lhc_page_one_status_time_view);
		simpleTextViewList("lhc_page_one_status", "value", lhc_page_one_status_view);
		bkgdColorTextViewList("lhc_stable_beams_flag", "value", "1", lhc_stable_beams_flag_view);
		simpleTextViewList("lhc_beam_mode", "value", lhc_beam_mode_view);
		
		// detector mask
		detectorMaskTextViewList("TDAQ_MUON_CTP_INTERFACE", atlas_included_hw_muctpi);
		detectorMaskTextViewList("TDAQ_CTP", atlas_included_hw_ctp);
		detectorMaskTextViewList("TDAQ_CALO", atlas_included_hw_l1calo);
		detectorMaskTextViewList("TDAQ_HLT", atlas_included_hw_hlt);
		detectorMaskTextViewList("MUON_RPC", atlas_included_hw_rpc);
		detectorMaskTextViewList("MUON_TGC", atlas_included_hw_tgc);
		detectorMaskTextViewList("MUON_MDT", atlas_included_hw_mdt);
		detectorMaskTextViewList("MUON_CSC", atlas_included_hw_csc);
		detectorMaskTextViewList("LAR", atlas_included_hw_lar);
		detectorMaskTextViewList("TILECAL", atlas_included_hw_tile);
		detectorMaskTextViewList("PIXEL", atlas_included_hw_pix);
		detectorMaskTextViewList("SCT", atlas_included_hw_sct);
		detectorMaskTextViewList("TRT", atlas_included_hw_trt);
		detectorMaskTextViewList("FORWARD_LUCID", atlas_included_hw_lucid);
		detectorMaskTextViewList("FORWARD_BCM", atlas_included_hw_bcm);
		detectorMaskTextViewList("FORWARD_ALFA", atlas_included_hw_alfa);
		detectorMaskTextViewList("FORWARD_ZDC", atlas_included_hw_zdc);
		
		// busy status
		busyStatusTextViewList("ctpcore_moni0_rate", busy_summary_low);
		busyStatusTextViewList("ctpcore_moni1_rate", busy_summary_high);
	}
	
	
//	private final String TAG = "UserInfoUpdaterHandler";
}
