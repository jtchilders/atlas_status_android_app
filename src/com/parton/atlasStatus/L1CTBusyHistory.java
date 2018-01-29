package com.parton.atlasStatus;

import java.util.ArrayList;


public class L1CTBusyHistory extends ISInfo {
	private MainActivity ParentActivity = null;
	
	private static final String provider = "L1CT-History";
	private static final String info_name = "ISCTPBUSY";

	public L1CTBusyHistory(String partition, MainActivity ParentActivity) {
		super(partition, provider, info_name, ParentActivity.cern_cookie());
		this.ParentActivity = ParentActivity;
//		Log.v(TAG,"constructor: inside");
	}
	
	private ArrayList<ProgressBarLink> progressBars = new ArrayList<ProgressBarLink>();
	public void addProgressBar(ProgressBarLink link){
		progressBars.add(link);
	}
	public void addProgressBar(int id, String is_attr_name, int valueArrayIndex){
		ProgressBarLink link = new ProgressBarLink();
		link.id = id;
		link.is_attr_name = is_attr_name;
		link.valueArrayIndex = valueArrayIndex;
		addProgressBar(link);
	}
	
	@Override
	protected void updateViews(){
//		Log.v(TAG,"updateViews: inside");
		for(int i=0;i<progressBars.size();++i){
			ProgressBarLink link = progressBars.get(i);
			BusyStatusBar bar = (BusyStatusBar)ParentActivity.findViewById(link.id);
			if(bar != null){
				ISObjectAttr attr = super.attributes.get(link.is_attr_name);
				if(attr != null){
					float busy_percent = Float.parseFloat(attr.values(link.valueArrayIndex));
					bar.setPercent(busy_percent);
				}
//				else
//					Log.v(TAG,"updateViews: attr is null for "+link.is_attr_name);
			}
//			else
//				Log.v(TAG,"updateViews: bar is null for "+link.is_attr_name);
		}
	}
	
	
	public class ProgressBarLink {
		int id;
		String is_attr_name;
		int valueArrayIndex;
	}
	
//	private static final String TAG = "L1CTBusyHistory";
}
