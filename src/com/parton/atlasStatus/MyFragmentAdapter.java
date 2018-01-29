package com.parton.atlasStatus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MyFragmentAdapter extends FragmentPagerAdapter {
	
	private static final int GLOBAL_STATUS_TAB = 0;
	private static final int BUSY_STATUS_TAB   = 1;
	private static final int NUM_TABS          = 2;
	

	public MyFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		
		switch(i){
		case GLOBAL_STATUS_TAB:
			return GlobalStatusFragment.newInstance();
		case BUSY_STATUS_TAB:
			return BusyStatusFragment.newInstance();
		default:
			return null;
		}
		
	}

	@Override
	public int getCount() {
		return NUM_TABS;
	}

}
