package com.parton.atlasStatus;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class ISInfoUpdaterThread extends Thread {
	
	public final static int UPDATE_VIEWS = 1;
	public final static int NO_CONNECTION = 2;
	public final static int CONNECTION_TYPE_CHANGE = 3;
	public final static int XML_IS_SSO = 4;

	private Context ParentActivity = null;
	
	public ISInfoUpdaterThread(UserInfoUpdateHandler handler,Context ParentActivity){
		userInfoUpdateHandler(handler);
		this.ParentActivity = ParentActivity;
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
				try {
					Thread.sleep(500*1000);
				} catch (InterruptedException e) {
					//Log.v(TAG,"run: received interrupt from wait, continuing");
					pauseUpdate = false;
					
					if(!doUpdate){
						//Log.v(TAG,"run: should not update again, exiting");
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
			}
			else{
				
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
					ISInfo tmp = (ISInfo) ISInfoList.values().toArray()[i];
					try {
						int returnValue = tmp.update();
						if(returnValue == ISInfo.XML_EMPTY){
							Log.w(TAG,"run: ISInfo returned empty xml");
						}
						else if(returnValue == ISInfo.XML_IS_SSO){
							Log.w(TAG,"run: ISInfo returned xml from the SSO, new cookie needed");
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
				
				// send a message to update the views
				Message msg = new Message();
				msg.arg1 = UPDATE_VIEWS;
				msg.obj = ISInfoList;
				userInfoUpdateHandler.sendMessage(msg);
			}
			
			try {
				super.sleep(1000*update_delay_seconds);
			} catch (InterruptedException e) {
				continue;
			}
		}
		
		//Log.v(TAG,"run: exiting");
	}
	
	public void updateCookie(String cookie){
		
		for(int i = 0; i<ISInfoList.size(); ++i){
			ISInfo info = (ISInfo) ISInfoList.values().toArray()[i];
			info.updateCookie(cookie);
		}
	}
	
	private final String TAG = "ISInforUpdaterThread";
}
