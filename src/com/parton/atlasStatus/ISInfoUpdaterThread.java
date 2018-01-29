package com.parton.atlasStatus;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;

public class ISInfoUpdaterThread extends Thread {
	
	public final static int UPDATE_VIEWS = 1;
	public final static int NO_CONNECTION = 2;
	public final static int CONNECTION_TYPE_CHANGE = 3;
	public final static int XML_IS_SSO = 4;
	public final static int PARTITION_DOWN =5;

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
//				Log.v(TAG,"run: inside pauseUpdate");
				try {
//					Log.v(TAG,"run: tread is paused");
					Thread.sleep(500*1000);
				} catch (InterruptedException e) {
//					Log.v(TAG,"run: received interrupt from wait, continuing");
					pauseUpdate = false;
					
					if(!doUpdate){
//						Log.v(TAG,"run: should not update again, exiting");
						return;
					}
					
					continue;
				}
			}
			
			// check for network connection
			if(!NetworkStatusChecker.isConnected(ParentActivity)){
//				Log.v("ISInfoUpdaterThread","is not connected send message");
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
//						Log.v(TAG,"run: updating is info");
						int returnValue = tmp.update();
						if(returnValue == ISInfo.XML_EMPTY){
//							Log.w(TAG,"run: ISInfo returned empty xml");
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
						else if(returnValue == ISInfo.PARTITION_DOWN && tmp.partition().contains(MainActivity.TDAQ_PARTITION)){
							
							Message msg = new Message();
							msg.arg1 = PARTITION_DOWN;
							userInfoUpdateHandler.sendMessage(msg);
							
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
				
//				Log.v(TAG,"run: send message to update views");
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
        
//
        L1CT_ISCTPOUT l1ct_ctpout_12 = new L1CT_ISCTPOUT(MainActivity.TDAQ_PARTITION,ParentActivity,12);
        l1ct_ctpout_12.addProgressBar(R.id.busy_ctpout_12_0_rate,0);
        l1ct_ctpout_12.addProgressBar(R.id.busy_ctpout_12_1_rate,1);
        l1ct_ctpout_12.addProgressBar(R.id.busy_ctpout_12_2_rate,2);
        l1ct_ctpout_12.addProgressBar(R.id.busy_ctpout_12_3_rate,3);
        l1ct_ctpout_12.addProgressBar(R.id.busy_ctpout_12_4_rate,4);
        addISInfo("l1ct_ctpout_12",l1ct_ctpout_12);
        
        L1CT_ISCTPOUT l1ct_ctpout_13 = new L1CT_ISCTPOUT(MainActivity.TDAQ_PARTITION,ParentActivity,13);
        l1ct_ctpout_13.addProgressBar(R.id.busy_ctpout_13_0_rate,0);
        l1ct_ctpout_13.addProgressBar(R.id.busy_ctpout_13_1_rate,1);
        l1ct_ctpout_13.addProgressBar(R.id.busy_ctpout_13_2_rate,2);
        l1ct_ctpout_13.addProgressBar(R.id.busy_ctpout_13_3_rate,3);
        l1ct_ctpout_13.addProgressBar(R.id.busy_ctpout_13_4_rate,4);
        addISInfo("l1ct_ctpout_13",l1ct_ctpout_13);
        
        L1CT_ISCTPOUT l1ct_ctpout_14 = new L1CT_ISCTPOUT(MainActivity.TDAQ_PARTITION,ParentActivity,14);
        l1ct_ctpout_14.addProgressBar(R.id.busy_ctpout_14_0_rate,0);
        l1ct_ctpout_14.addProgressBar(R.id.busy_ctpout_14_1_rate,1);
        l1ct_ctpout_14.addProgressBar(R.id.busy_ctpout_14_2_rate,2);
        l1ct_ctpout_14.addProgressBar(R.id.busy_ctpout_14_3_rate,3);
        l1ct_ctpout_14.addProgressBar(R.id.busy_ctpout_14_4_rate,4);
        addISInfo("l1ct_ctpout_14",l1ct_ctpout_14);
        
        L1CT_ISCTPOUT l1ct_ctpout_15 = new L1CT_ISCTPOUT(MainActivity.TDAQ_PARTITION,ParentActivity,15);
        l1ct_ctpout_15.addProgressBar(R.id.busy_ctpout_15_0_rate,0);
        l1ct_ctpout_15.addProgressBar(R.id.busy_ctpout_15_1_rate,1);
        l1ct_ctpout_15.addProgressBar(R.id.busy_ctpout_15_2_rate,2);
        l1ct_ctpout_15.addProgressBar(R.id.busy_ctpout_15_3_rate,3);
        l1ct_ctpout_15.addProgressBar(R.id.busy_ctpout_15_4_rate,4);
        addISInfo("l1ct_ctpout_15",l1ct_ctpout_15);
        
        L1CTBusyHistory l1ct_busy_history = new L1CTBusyHistory(MainActivity.TDAQ_PARTITION,ParentActivity);
        l1ct_busy_history.addProgressBar(R.id.busy_summary_low, 		"ctpcore_moni0_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_summary_high, 		"ctpcore_moni1_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_moni0_rate, 	"ctpcore_moni0_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_moni1_rate, 	"ctpcore_moni1_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_moni2_rate, 	"ctpcore_moni2_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_moni3_rate, 	"ctpcore_moni3_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_rslt_rate, 	"ctpcore_rslt_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_bckp_rate, 	"ctpcore_bckp_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_rdt_rate, 	"ctpcore_rdt_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpcore_mon_rate, 	"ctpcore_mon_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpmi_bckp_rate,  	"ctpmi_bckp_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpmi_vme_rate,   	"ctpmi_vme_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpmi_ecr_rate,   	"ctpmi_ecr_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpmi_vto0_rate,  	"ctpmi_vto0_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpmi_vto1_rate,  	"ctpmi_vto1_rate", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_12_0_rate, 	"ctpout_12", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_12_1_rate, 	"ctpout_12", 1);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_12_2_rate, 	"ctpout_12", 2);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_12_3_rate, 	"ctpout_12", 3);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_12_4_rate, 	"ctpout_13", 4);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_13_0_rate, 	"ctpout_13", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_13_1_rate, 	"ctpout_13", 1);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_13_2_rate, 	"ctpout_13", 2);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_13_3_rate, 	"ctpout_13", 3);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_13_4_rate, 	"ctpout_13", 4);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_14_0_rate, 	"ctpout_14", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_14_1_rate, 	"ctpout_14", 1);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_14_2_rate, 	"ctpout_14", 2);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_14_3_rate, 	"ctpout_14", 3);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_14_4_rate, 	"ctpout_14", 4);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_15_0_rate, 	"ctpout_15", 0);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_15_1_rate, 	"ctpout_15", 1);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_15_2_rate, 	"ctpout_15", 2);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_15_3_rate, 	"ctpout_15", 3);
        l1ct_busy_history.addProgressBar(R.id.busy_ctpout_15_4_rate, 	"ctpout_15", 4);
        addISInfo("l1ct_busy_history",l1ct_busy_history);

        
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
	
//	private final String TAG = "ISInforUpdaterThread";
}
