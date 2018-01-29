package com.parton.atlasStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.parton.atlasStatus.R;

public class UserInfoUpdateHandler extends Handler{
	
	private HashMap<String, ISInfo> ISInfoList = null;
	
	private Activity ParentActivity = null;
	public void ParentActivity(Activity context){
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
			Log.v(TAG,"no connection message received");
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
			Log.w(TAG,"updatePartitionStatusView: no ISInfo object named: "+"partition_status");
			return;
		}
		
		IS_XML_Attr attr = info.getAttr("state");
		if(attr == null){
			Log.w(TAG,"updatePartitionStatusView: no attr named: "+"state");
			return;
		}
		
		if(partitionStatusView == null){
			Log.w(TAG,"updatePartitionStatusView: partitionStatusView is null");
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
			Log.w(TAG,"updateTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
			Log.w(TAG,"updateTextView: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
			Log.w(TAG,"updateTextView: text_view is null");
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
			Log.w(TAG,"updateTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
			Log.w(TAG,"updateTextView: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
			Log.w(TAG,"updateTextView: text_view is null");
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
			Log.w(TAG,"updateDetectorMaskTextView: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
			Log.w(TAG,"updateDetectorMaskTextView: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
			Log.w(TAG,"updateDetectorMaskTextView: text_view is null");
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
		else
			Log.w(TAG,"updateMenuTime: menu not set");
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
			Log.w(TAG,"busyStatusTextViewList: no ISInfo object named: "+is_info_name);
			return;
		}
		
		IS_XML_Attr attr = info.getAttr(is_attr_name);
		if(attr == null){
			Log.w(TAG,"busyStatusTextViewList: no attr named: "+is_attr_name);
			return;
		}
		
		if(text_view == null){
			Log.w(TAG,"busyStatusTextViewList: text_view is null");
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
	
	
	private final String TAG = "UserInfoUpdaterHandler";
}
