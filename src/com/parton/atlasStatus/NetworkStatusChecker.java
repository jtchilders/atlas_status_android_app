package com.parton.atlasStatus;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

public class NetworkStatusChecker {
	
	public static boolean isConnected(Context context){
		// check for network connection
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork == null){
//			Log.w(TAG,"isConnected: activeNetwork is null");
			return false;
		}
		return activeNetwork.isConnected();
	}

	public static boolean isWifi(Context context){
		// check for network connection
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork == null){
			return false;
		}

		boolean isConnected = activeNetwork.isConnectedOrConnecting();
		if(!isConnected) return false;

		boolean isWifi = false;
		if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
			isWifi = true;

		return isWifi;
	}

	public static boolean isMobile(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork == null){
			return false;
		}

		boolean isConnected = activeNetwork.isConnectedOrConnecting();
		if(!isConnected) return false;

		boolean isMobile = false;
		if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			isMobile = true;

		return isMobile;
	}

	public static int getNetworkType(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork == null){
			return 0;
		}

		return activeNetwork.getType();
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ip = inetAddress.getHostAddress().toString();
						if(!ip.contains("::"))
							return ip;
					}
				}
			}
		} catch (SocketException e) {
//			Log.e(TAG, ex.toString());
			e.printStackTrace();
		}
		return null;
	}
	public static void ShowNoNetworkDialogue(Context context){
		final Context ctx = context;
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setCancelable(true);
		builder.setMessage(R.string.no_network_connection);
		builder.setTitle(R.string.no_network_connection_title);
		builder.setPositiveButton(R.string.no_network_connection_settings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ctx.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton(R.string.no_network_connection_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				return;
			}
		});

		builder.show();
	}

	public static void ShowToast(Context context,String text){
		Toast.makeText(context,text,Toast.LENGTH_LONG).show();
	}

//	private final static String TAG = "NetworkStatusChecker";
}
