package com.parton.atlasStatus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class LastDayLumiFragment extends Fragment {
	
	private static final String COOKIE_URL = "atlasop.cern.ch";
	private static final String LASTDAY_LUMI_URL = "https://atlas.web.cern.ch/Atlas/GROUPS/DATAPREPARATION/DataSummary/2012/daydata/lastday/lastday_lumi.png";
	private static final String ACR_WEBCAM_URL = "https://atlasop.cern.ch/ATLASview/webpic/ACR01HD.jpg";
	
	public static LastDayLumiFragment newInstance(){
		return new LastDayLumiFragment();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.v(TAG,"onCreateView: inside");
        // Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.lastday_lumi, container, false);
		
//		Log.v(TAG,"onCreateView: view = "+view.toString());
		

		CookieManager cm = CookieManager.getInstance();
//		Log.v(TAG,"onCreateView: cm = "+cm+" cookie = "+MainActivity.cern_cookie);
		cm.setCookie(COOKIE_URL, MainActivity.cern_cookie.cookie());
		
	    GenericWebViewClient myClient = new GenericWebViewClient();
		
		
		WebView lastDayLumi = (WebView) view.findViewById(R.id.webview_lastday_lumi);
		WebSettings webSettings = lastDayLumi.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setLoadWithOverviewMode(true);
	    webSettings.setUseWideViewPort(true);
	    webSettings.setBuiltInZoomControls(true);
	    webSettings.setSupportZoom(true);
	    lastDayLumi.setWebViewClient(myClient);
	    
	    
	    lastDayLumi.loadUrl(LASTDAY_LUMI_URL);
		
	    WebView webcam = (WebView) view.findViewById(R.id.webview_webcam);
		webSettings = webcam.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setLoadWithOverviewMode(true);
	    webSettings.setUseWideViewPort(true);
	    webSettings.setBuiltInZoomControls(true);
	    webSettings.setSupportZoom(true);
	    webcam.setWebViewClient(myClient);
	    webcam.loadUrl(ACR_WEBCAM_URL);
		
		return view;
    }
	

//	private static final String TAG = "LastDayLumiFragment";
}
