package com.parton.atlasStatus;

//import android.util.Log;

public class TrustAllTrustManager implements javax.net.ssl.X509TrustManager {

//	private static final String TAG = "TrustAllTrustManager";

	public java.security.cert.X509Certificate[] getAcceptedIssuers() {

		return new java.security.cert.X509Certificate[] {};

	}

	public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)  {

	}

	public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {

//		for(int index=0; certs!=null && index<certs.length; index++) {
//
//			Log.v(TAG,"Certificate ["+index+"]: " + certs[0].getSubjectDN().getName());
//
//		}
		;
	}

}
