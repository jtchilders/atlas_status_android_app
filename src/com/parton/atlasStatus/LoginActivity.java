package com.parton.atlasStatus;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;

@SuppressLint("SetJavaScriptEnabled")
public class LoginActivity extends Activity {
	public static final String CookieReturnData = "COOKIE_RETURNED";
	public static final String ALREADY_RUNNING = "ALREADY_RUNNING";
	
	public static boolean isActive;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	Log.v(TAG,"onCreate: inside");
	    super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        
//	    Log.v(TAG,"onCreate: create webview ");
	    WebView myWebView = (WebView) findViewById(R.id.webview);
	    WebSettings webSettings = myWebView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    ssoWebViewClient myClient = new ssoWebViewClient();
	    myWebView.setWebViewClient(myClient);
	    myClient.ParentActivity = this;
	    
//	    Log.v(TAG,"onCreate: Load URL");
	    CookieManager.getInstance().removeAllCookie();
	    myWebView.clearCache(true);
	    myWebView.loadUrl("https://atlasop.cern.ch");
	    
	    
	    // First need to load the page and have the person login
	    //Log.v(TAG,"onCreate: Get Cookie");
//	    CookieManager cm = CookieManager.getInstance();
//	    final String cookie = cm.getCookie("atlasop.cern.ch");
//	    Log.v(TAG,"onCreate: hasCookie: " + cm.hasCookies() + " cookie: " + cookie);
        
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
//    	Log.v(TAG,"onDestroy: inside");
//    	Log.v(TAG,"onDestroy: setting inactive "+isActive);
    	isActive = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public static void ShowLoginActivity(MainActivity parent){
    	if(!isActive){
    		isActive = true;
//    		Log.v(TAG,"ShowLoginActivity: starting Login");
			
	        Intent loginIntent = new Intent(parent,LoginActivity.class);
	        parent.startActivityForResult(loginIntent,MainActivity.GET_COOKIE);
		}
    }
    
    
//    private final static String TAG = "LoginActivity";
}
