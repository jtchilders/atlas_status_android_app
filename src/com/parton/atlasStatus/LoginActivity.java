package com.parton.atlasStatus;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;
import com.parton.atlasStatus.R;

@SuppressLint("SetJavaScriptEnabled")
public class LoginActivity extends Activity {
	public static final String CookieReturnData = "COOKIE_RETURNED";
	
	public static boolean isActive = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	Log.v(TAG,"onCreate: inside");
	    super.onCreate(savedInstanceState);
	    isActive = true;
        setContentView(R.layout.activity_login);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
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
    
//    private final String TAG = "LoginActivity";
}
