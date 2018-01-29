package com.parton.atlasStatus;

import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;

public class ssoWebViewClient extends WebViewClient {
    boolean firstLoad = true;
    
    private String cookie = "";
    public final String cookieUrl = "atlasop.cern.ch";
    public LoginActivity ParentActivity;
    
    private final String COOKIE_VETO = "_shibstate_";
    private final String COOKIE_REQUIREMENT_A = "_shibsession_";
    private final String COOKIE_REQUIREMENT_B = "_saml_idp";
	
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
		
		if(cookie.length() == 0){
//			Log.v(TAG,"shouldOverrideUrlLoading: Cookie Empty Continue Loading");
			return false;
		}
		else{
//			Log.v(TAG,"shouldOverrideUrlLoading: Cookie not empty: "+cookie);
			if(cookie.contains(COOKIE_VETO) && !cookie.contains(COOKIE_REQUIREMENT_A) && !cookie.contains(COOKIE_REQUIREMENT_B)){
//				Log.v(TAG,"shouldOverrideUrlLoading: vetoing cookie: "+cookie);
				return false;
			}
			else if(cookie.contains(COOKIE_REQUIREMENT_A) && cookie.contains(COOKIE_REQUIREMENT_B)){
//				Log.v(TAG,"shouldOverrideUrlLoading: keep cookie: "+cookie);
				Intent intent = new Intent();
				intent.putExtra(LoginActivity.CookieReturnData,cookie);
				ParentActivity.setResult(Activity.RESULT_OK,intent);
				ParentActivity.finish();
				return true;
			}
//			else
//				Log.v(TAG,"shouldOverrideUrlLoading: Cookie didn't get vetoed or kept.");
		}
		
		return false;
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) 
	{
	     handler.proceed();
	}
	
//	private final String TAG = "ssoWebViewClient";
}
