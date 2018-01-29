package com.parton.atlasStatus;

import java.util.ArrayList;


public class L1CT_ISCTPOUT extends ISInfo {
private MainActivity ParentActivity = null;
	
	private static final String provider = "L1CT";
	private static final String info_name_base = "ISCTPOUT_";
	private static final String is_attr_connector_prefix = "connector";
	private static final String is_attr_connector_name_postfix = "_name";
	private static final String is_attr_connector_busy_postfix = "_busy";
	private static final String CONNECTOR_ON = "ON";
	private static final int ATTR_INDEX = 0;
//	private static final String CONNECTOR_OFF = "OFF";
	
	
	private int boardNumber = -1;
	public int boardNumber(){
		return boardNumber;
	}

	public L1CT_ISCTPOUT(String partition, MainActivity ParentActivity,int boardNumber) {
		super(partition, provider, info_name_base+boardNumber, ParentActivity.cern_cookie());
		this.ParentActivity = ParentActivity;
		this.boardNumber = boardNumber;
//		Log.v(TAG,"constructor: inside for "+boardNumber);
	}
	


	private ArrayList<ProgressBarLink> progressBars = new ArrayList<ProgressBarLink>();
	public void addProgressBar(ProgressBarLink id){
		progressBars.add(id);
	}
	public void addProgressBar(int id,int connector){
		ProgressBarLink link = new ProgressBarLink();
		link.id = id;
		link.connector = connector;
		progressBars.add(link);
	}
	
	@Override
	protected void updateViews(){
//		Log.v(TAG,"updateViews: inside");
		for(int i=0;i<progressBars.size();++i){
			ProgressBarLink link = progressBars.get(i);
			BusyStatusBar bar = (BusyStatusBar)ParentActivity.findViewById(link.id);
			if(bar != null){
//				Log.v(TAG,"updateViews: inside bar");
				// set name
				String is_attr_name = is_attr_connector_prefix+link.connector+is_attr_connector_name_postfix;
				ISObjectAttr attr = super.attributes.get(is_attr_name);
				if(attr != null){
					String busy_name = attr.values(ATTR_INDEX);
//					Log.v(TAG,"updateViews: busy_name "+busy_name);
					bar.name(busy_name);
				}
//				else
//					Log.v(TAG,"updateViews: attr is null for "+is_attr_name);
				
				// set enabled
				is_attr_name = is_attr_connector_prefix+link.connector+is_attr_connector_busy_postfix;
				attr = super.attributes.get(is_attr_name);
				if(attr != null){
					String busy_enabled = attr.values(ATTR_INDEX);
//					Log.v(TAG,"updateViews: busy_enabled "+busy_enabled);
					if(busy_enabled.contains(CONNECTOR_ON))
						bar.enabled(true);
					else
						bar.enabled(false);
				}
//				else
//					Log.v(TAG,"updateViews: attr is null for "+is_attr_name);
			}
//			else
//				Log.v(TAG,"updateViews: bar is null");
		}
	}
	
	
	public class ProgressBarLink {
		int id;
		int connector;
	}
	
	
//	private static final String TAG = "L1CT_ISCTPOUT";
}
