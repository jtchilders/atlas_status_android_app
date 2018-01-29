package com.parton.atlasStatus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MyFragmentAdapter extends FragmentPagerAdapter {
	
	private static enum TABS { GLOBAL_STATUS, BUSY_STATUS, LASTDAY_LUMI };
	
	

	public MyFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		TABS tab = TABS.values()[i];
		switch(tab){
		case GLOBAL_STATUS:
			return GlobalStatusFragment.newInstance();
		case BUSY_STATUS:
			return BusyStatusFragment.newInstance();
		case LASTDAY_LUMI:
			return LastDayLumiFragment.newInstance();
		default:
			return null;
		}
		
	}

	@Override
	public int getCount() {
		return TABS.values().length;
	}

}
