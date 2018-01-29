package com.parton.atlasStatus;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

public class ISInfoUpdaterThread extends Thread {
	
	public final static int UPDATE_VIEWS = 1;
	public final static int NO_CONNECTION = 2;
	public final static int CONNECTION_TYPE_CHANGE = 3;
	public final static int XML_IS_SSO = 4;

	private MainActivity ParentActivity = null;
	
	public ISInfoUpdaterThread(UserInfoUpdateHandler handler,MainActivity ParentActivity){
		userInfoUpdateHandler(handler);
		this.ParentActivity = ParentActivity;
		AddISInfoObjects();
	}
	
	private HashMap<String, ISInfo> ISInfoList = new HashMap<String, ISInfo>();
	public void addISInfo(String name, ISInfo infoObject){
		ISInfoList.put(name,infoObject);
	}
	public ISInfo getISInfo(String name){
		return ISInfoList.get(name);
	}
	
	private UserInfoUpdateHandler userInfoUpdateHandler = null;
	public void userInfoUpdateHandler(UserInfoUpdateHandler userInfoUpdateHandler){
		this.userInfoUpdateHandler = userInfoUpdateHandler;
	}
	public UserInfoUpdateHandler userInfoUpdateHandler(){
		return userInfoUpdateHandler;
	}
	
	private long last_update_time = 0;
	public long last_update_time(){
		return last_update_time;
	}
	
	private boolean doUpdate = true;
	public void doUpdate(boolean doUpdate){
		this.doUpdate = doUpdate;
	}
	public boolean doUpdate(){
		return doUpdate;
	}
	
	private boolean pauseUpdate = false;
	public void pauseUpdate(boolean pauseUpdate){
		this.pauseUpdate = pauseUpdate;
	}
	public boolean pauseUpdate(){
		return pauseUpdate;
	}
	
	public void pause(){
		pauseUpdate = true;
	}
	public void unpause(){
		if(pauseUpdate){
			pauseUpdate = false;
			this.interrupt();
		}
	}
	
	private boolean udpateAvailable = false;
	public boolean udpateAvailable(){
		return udpateAvailable;
	}
	
	private int update_delay_seconds = 5;
	public void update_delay_seconds(int update_delay_seconds){
		this.update_delay_seconds = update_delay_seconds;
	}
	public int update_delay_seconds(){
		return update_delay_seconds;
	}
	
	@Override
	public void run(){
		while(doUpdate){
			while(pauseUpdate){
				Log.v(TAG,"run: inside pauseUpdate");
				try {
					Log.v(TAG,"run: tread is paused");
					Thread.sleep(500*1000);
				} catch (InterruptedException e) {
					Log.v(TAG,"run: received interrupt from wait, continuing");
					pauseUpdate = false;
					
					if(!doUpdate){
						Log.v(TAG,"run: should not update again, exiting");
						return;
					}
					
					continue;
				}
			}
			
			// check for network connection
			if(!NetworkStatusChecker.isConnected(ParentActivity)){
				Log.v("ISInfoUpdaterThread","is not connected send message");
				// send a message to print no connection
				Message msg = new Message();
				msg.arg1 = NO_CONNECTION;
				userInfoUpdateHandler.sendMessage(msg);
				pauseUpdate = true;
				continue;
			}
			else{
//				Log.v(TAG,"run: network connected");
				// so we have a connection, but lets make sure we have not switched from 
				// one network type to another
//				if(connectionType != NetworkStatusChecker.getNetworkType(ParentActivity)){
//					Message msg = new Message();
//					msg.arg1 = CONNECTION_TYPE_CHANGE;
//					userInfoUpdateHandler.sendMessage(msg);
//					pauseUpdate = true;
//					continue;
//				}
				
				for(int i=0;i<ISInfoList.size();++i){
//					Log.v(TAG,"run: inside loop over is info list");
					ISInfo tmp = (ISInfo) ISInfoList.values().toArray()[i];
					try {
						Log.v(TAG,"run: updating is info");
						int returnValue = tmp.update();
						if(returnValue == ISInfo.XML_EMPTY){
							Log.w(TAG,"run: ISInfo returned empty xml");
							continue;
						}
						else if(returnValue == ISInfo.XML_IS_SSO){
//							Log.w(TAG,"run: ISInfo returned xml from the SSO, new cookie needed");
							// send a message to get new cookie
							Message msg = new Message();
							msg.arg1 = XML_IS_SSO;
							userInfoUpdateHandler.sendMessage(msg);
							pauseUpdate = true;
							break;
						}
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				// catch pause from new cookie
				if(pauseUpdate){
					continue;
				}
				
				last_update_time = System.currentTimeMillis();
		        
				udpateAvailable = true;
				
				Log.v(TAG,"run: send message to update views");
				// send a message to update the views
				Message msg = new Message();
				msg.arg1 = UPDATE_VIEWS;
				msg.obj = ISInfoList;
				userInfoUpdateHandler.sendMessage(msg);
			}
			
			try {
				Thread.sleep(1000*(update_delay_seconds()-1));
			} catch (InterruptedException e) {
				continue;
			}
		}
		
		//Log.v(TAG,"run: exiting");
	}
	
	public void updateCookie(CernCookie cookie){
		
		for(int i = 0; i<ISInfoList.size(); ++i){
			ISInfo info = (ISInfo) ISInfoList.values().toArray()[i];
			info.updateCookie(cookie);
		}
	}
	
	private void AddISInfoObjects(){
		ISInfo partition_status = new ISInfo(MainActivity.TDAQ_PARTITION,"RunCtrl","RootController",ParentActivity.cern_cookie());
        addISInfo("partition_status", partition_status);
        ISInfo run_parameters = new ISInfo(MainActivity.TDAQ_PARTITION,"RunParams","RunParams",ParentActivity.cern_cookie());
        addISInfo("run_parameters", run_parameters);
        ISInfo run_ready4physics = new ISInfo(MainActivity.TDAQ_PARTITION,"RunParams","Ready4Physics",ParentActivity.cern_cookie());
        addISInfo("run_ready4physics", run_ready4physics);
        ISInfo lhc_page_one_status = new ISInfo(MainActivity.INITIAL_PARTITION,"LHC","LHCPage1Msg",ParentActivity.cern_cookie());
        addISInfo("lhc_page_one_status", lhc_page_one_status);
        ISInfo lhc_stable_beams_flag = new ISInfo(MainActivity.INITIAL_PARTITION,"LHC","StableBeamsFlag",ParentActivity.cern_cookie());
        addISInfo("lhc_stable_beams_flag", lhc_stable_beams_flag);
        ISInfo lhc_beam_mode = new ISInfo(MainActivity.INITIAL_PARTITION,"LHC","BeamMode",ParentActivity.cern_cookie());
        addISInfo("lhc_beam_mode", lhc_beam_mode);
        ISInfo l1ct_busy_status = new ISInfo(MainActivity.TDAQ_PARTITION,"L1CT-History","ISCTPBUSY",ParentActivity.cern_cookie());
        addISInfo("l1ct_busy_status", l1ct_busy_status);
        ISInfo l1ct_ctpout12_busy_status = new ISInfo(MainActivity.TDAQ_PARTITION,"L1CT","ISCTPOUT_12",ParentActivity.cern_cookie());
        addISInfo("l1ct_ctpout12_busy_status", l1ct_ctpout12_busy_status);
        ISInfo l1ct_ctpout13_busy_status = new ISInfo(MainActivity.TDAQ_PARTITION,"L1CT","ISCTPOUT_13",ParentActivity.cern_cookie());
        addISInfo("l1ct_ctpout13_busy_status", l1ct_ctpout13_busy_status);
        ISInfo l1ct_ctpout14_busy_status = new ISInfo(MainActivity.TDAQ_PARTITION,"L1CT","ISCTPOUT_14",ParentActivity.cern_cookie());
        addISInfo("l1ct_ctpout14_busy_status", l1ct_ctpout14_busy_status);
        ISInfo l1ct_ctpout15_busy_status = new ISInfo(MainActivity.TDAQ_PARTITION,"L1CT","ISCTPOUT_15",ParentActivity.cern_cookie());
        addISInfo("l1ct_ctpout15_busy_status", l1ct_ctpout15_busy_status);
	}
	
	public void UpdateSyncFrequency(){
		if(ParentActivity != null){
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ParentActivity);
			String str_update_delay_seconds = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_FREQ,"15");
			int update_delay_seconds = Integer.decode(str_update_delay_seconds);
	        //Log.v(TAG,"pref value: "+update_delay_seconds);
	        update_delay_seconds(update_delay_seconds);
		}
	}
	
	private final String TAG = "ISInforUpdaterThread";
}
