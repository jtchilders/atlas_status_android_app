package com.parton.atlasStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class WebIsRetriever {
	public WebIsRetriever(String partition){
		partition(partition);
	}
	public WebIsRetriever(String partition,CernCookie cookie){
		partition(partition);
		cookie(cookie);
	}
	
	private final String basePath = "/info/current";  
	private final int    port     = 443;
	private final String protocol = "https://";
	private final String server   = "atlasop.cern.ch";
//	private final String TAG      = "WebIsRetriever";
	
	private CernCookie cookie = null;
	public void cookie(CernCookie cookie){
		this.cookie = cookie;
	}
	public CernCookie cookie(){
		return cookie;
	}
	
	private String partition = "ATLAS";
	public void partition(String partition){
		this.partition = partition;
	}
	public String partition(){
		return partition;
	}
	
	public String getXml(String path){
		String content = "";
		
//		Log.v(TAG,"getXml: cookie: "+cookie);
		
		if(cookie == null){
//			Log.w(TAG,"getXml: cookie is not set");
			return "";
		}
		else if(!cookie.isValid()){
//			Log.w(TAG,"getXml: cookie is not valid.");
			return "";
		}
		
		try {
//			Log.v(TAG,"getXml: setup SSLContext");
			// create SSLContext and import instance for SSL
			SSLContext sslContext = SSLContext.getInstance("TLS"); // was "SSL"
			// use dummy trust manager which accepts all certificates (unsecure!!!)
			sslContext.init(null, new javax.net.ssl.TrustManager[] { new TrustAllTrustManager() }, new  java.security.SecureRandom());
			sslContext.getSocketFactory().createSocket(server,port);
			
			String fullPath;
			if(path.startsWith("/"))
				fullPath = protocol + server + basePath + "/" + partition + path;
			else
				fullPath = protocol + server + basePath + "/" + partition + "/" + path;
			
//			Log.v(TAG,"getXml: connect to "+fullPath);
			// create connection
			URL atlasop = new URL(fullPath);
			HttpsURLConnection connection = (HttpsURLConnection) atlasop.openConnection();
			// add socket factory to accept certificate
			connection.setSSLSocketFactory(sslContext.getSocketFactory());
			// add cookies for Sign on authentication
			connection.setRequestProperty("Cookie", cookie.cookie());
			
//			Log.v(TAG,"getXml: get xml as string");
			// retrieve the input stream from the URL
			InputStream dataStream = connection.getInputStream();
			if(dataStream == null){
//				Log.e(TAG,"getXml: failed to get input stream.");
				return "";
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(dataStream));
			
			String line = null;
			String newline = System.getProperty("line.separator");
			while( (line = in.readLine()) != null){
				content += line + newline;
			}
			
			dataStream.close();

//			Log.v(TAG,"getXml: done, content = "+content);
			
		} catch (NoSuchAlgorithmException e) {
//			Log.e(TAG,"getXml: caught NoSuchAlgorithmException: "+e.toString());
			e.printStackTrace();
			return "";
		} catch (KeyManagementException e) {
//			Log.e(TAG,"getXml: caught KeyManagementException: "+e.toString());
			e.printStackTrace();
			return "";
		} catch (UnknownHostException e) {
//			Log.e(TAG,"getXml: caught KeyManagementException: "+e.toString());
			e.printStackTrace();
			return "";
		} catch (IOException e) {
//			Log.e(TAG,"getXml: caught KeyManagementException: "+e.toString());
			e.printStackTrace();
			return "";
		}
		
		return content;
	}
	
}
