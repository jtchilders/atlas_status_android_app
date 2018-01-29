package com.parton.atlasStatus;

import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
//import android.util.Log;

public class ssoWebViewClient extends WebViewClient {
    boolean firstLoad = true;
    
    private String cookie = "";
    public final String cookieUrl = "atlasop.cern.ch";
    public LoginActivity ParentActivity;
    
    private final String cookieVeto = "_shibstate_";
	
    public String GetCookie(){
    	return cookie;
    }
    
	@SuppressLint({ "NewApi" })
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url){
//		Log.v(TAG,"shouldOverrideUrlLoading: "+url);
		//String stopOnUrl = "https://atlasop.cern.ch/operation.php";
		
		CookieManager cm = CookieManager.getInstance();
		if(cm.hasCookies()){
			String tmp;
			tmp = cm.getCookie(cookieUrl);
			if(!tmp.isEmpty()){
				cookie = tmp;
			}
		}
		
		if(cookie.isEmpty()){
//			Log.v(TAG,"shouldOverrideUrlLoading: Cookie Empty Continue Loading");
			return false;
		}
		else{
			if(cookie.contains(cookieVeto)){
//				Log.v(TAG,"shouldOverrideUrlLoading: vetoing cookie: "+cookie);
				return false;
			}
			else{
//				Log.v(TAG,"shouldOverrideUrlLoading: keep cookie: "+cookie);
				Intent intent = new Intent();
				intent.putExtra(LoginActivity.CookieReturnData,cookie);
				ParentActivity.setResult(Activity.RESULT_OK,intent);
				ParentActivity.finish();
				return true;
			}
		}
		
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) 
	{
	     handler.proceed();
	}
	
//	private final String TAG = "ssoWebViewClient";
}
