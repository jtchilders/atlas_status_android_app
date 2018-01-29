package com.parton.atlasStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class CernCookie {
	
	public static final long MAX_LIFETIME_SEC = 1000*60*60*2; // 2 hour lifetime for cookies
	public static final String COOKIE_FILENAME = "atlas_cookie.txt";
	private static final int COOKIE_TEXT = 0;
	private static final int COOKIE_TIME = 1;
	private static final int COOKIE_IP   = 2;
	private static final int NUM_COOKIE_INFO = 3;
	
	private Activity ParentActivity = null;
	
	public CernCookie(Activity ParentActivity, String cookie,String ip){
		this.ParentActivity = ParentActivity;
		cookie(cookie);
		ip(ip);
		setTime();
	}
	
	public CernCookie(Activity ParentActivity){
		this.ParentActivity = ParentActivity;
		cookie("");
		ip("");
		time(0);
	}
	
	// cookie text
	private String cookie = "";
	public void cookie(String cookie){
		this.cookie = cookie;
	}
	public String cookie(){
		return cookie;
	}
	
	// time when cookie was created
	private long time = 0;
	public void time(long time){
		this.time = time;
	}
	public long time(){
		return time;
	}
	public void setTime(){
		time = System.currentTimeMillis();
	}
	
	// ip on which cookie was retrieved
	private String ip = "";
	public void ip(String ip){
		this.ip = ip;
	}
	public String ip(){
		return ip;
	}
	public void setIp(){
		ip(NetworkStatusChecker.getLocalIpAddress());
	}
	
	public boolean isValid(){
		long currentTime = System.currentTimeMillis();
		if((currentTime - time()) < MAX_LIFETIME_SEC){
			return true;
		}
		return false;
	}
	
	
	// read cookie information from file
	public void readCookie(){
		//Log.v(TAG,"readCookie: inside");
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(ParentActivity.openFileInput(COOKIE_FILENAME)));
			String line = null;
			
			int lineNumber = 0;
			for(lineNumber = 0;(line = reader.readLine()) != null && lineNumber < NUM_COOKIE_INFO;++lineNumber){
				switch(lineNumber){
				case COOKIE_TEXT: 
					cookie(line);
					break;
				case COOKIE_TIME: 
					time(Long.decode(line));
					break;
				case COOKIE_IP: 
					ip(line);
					break;
				default:
					break;
				}
			}
			
			
			if(lineNumber < NUM_COOKIE_INFO-1){
				Log.v(TAG,"readCookie: file not complete");
				cookie("");
				ip("");
				time(0);
			}
			else if(!isValid()){
				Log.v(TAG,"readCookie: cookie in file is not valid");
				cookie("");
				ip("");
				time(0);
			}
			
			
		} catch (Exception e) {
			Log.e(TAG,"readCookie: Exception when reading "+COOKIE_FILENAME);
			cookie("");
			ip("");
			time(0);
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e){
					Log.e(TAG,"readCookie: Exception when closing "+COOKIE_FILENAME);
					e.printStackTrace();
				}
			}
		}
	}
	
	public void writeCookie(){
		//Log.v(TAG,"writeCookie: inside");
		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		try{ 
			writer = new BufferedWriter(new OutputStreamWriter(ParentActivity.openFileOutput(COOKIE_FILENAME,Context.MODE_PRIVATE)));
			writer.write(cookie() + eol);
			writer.write(time() + eol);
			writer.write(ip() + eol);
		} catch (Exception e) {
			Log.e(TAG,"writeCookie: Exception when writing cookie");
			e.printStackTrace();
		} finally {
			if (writer != null){
				try {
					writer.close();
				} catch (IOException e){
					Log.e(TAG,"writeCookie: Exception when closing "+COOKIE_FILENAME);
					e.printStackTrace();
				}
			}
		}
	}
	
	private static final String TAG = "CernCookie";
}
