package com.parton.atlasStatus; 

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import com.parton.atlasStatus.R;


public class SettingsActivity extends PreferenceActivity
                              implements OnSharedPreferenceChangeListener {
    public static final String KEY_PREF_SYNC_FREQ = "pref_syncFrequency";
    private final String syncSummaryBase = "Update information every ";
    public static final String SyncFreqReturnData = "SYNC_FREQ_RETURNED";
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume: inside");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause: inside");
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate: inside");
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences shared = this.getPreferenceScreen().getSharedPreferences();
        String syncSetting = shared.getString(KEY_PREF_SYNC_FREQ,"10");
        setSyncFreqSummary(syncSetting);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	Log.v(TAG,"onSharedPreferenceChanged: inside");
        if (key.equals(KEY_PREF_SYNC_FREQ)) {
        	Log.v(TAG,"onSharedPreferenceChanged: In Sync Frequency Setting");
            // Set summary to be the user-description for the selected value
            String syncSetting = sharedPreferences.getString(KEY_PREF_SYNC_FREQ,"10");
            setSyncFreqSummary(syncSetting);
        }
    }
    
    private void setSyncFreqSummary(String syncSetting){
    	Log.v(TAG,"setSyncFreqSummary String: inside");
    	int setting = Integer.decode(syncSetting);
    	setSyncFreqSummary(setting);
    }
    
    @SuppressWarnings("deprecation")
	private void setSyncFreqSummary(int syncSetting){
    	Log.v(TAG,"setSyncFreqSummary: inside");
    	String units = "sec";
        if(syncSetting >= 60){
        	syncSetting = syncSetting/60;
        	units = "min";
        }
        Preference syncPref = findPreference(KEY_PREF_SYNC_FREQ);
        String syncSummary = syncSummaryBase + syncSetting + " " + units;
        syncPref.setSummary(syncSummary);
    }
    
    private final String TAG = "SettingsActivity";
}