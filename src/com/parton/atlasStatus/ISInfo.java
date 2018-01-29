package com.parton.atlasStatus;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;





public class ISInfo {
	
	public final static int XML_EMPTY = -1;
	public final static int XML_IS_SSO = -2;
	public final static int PARTITION_DOWN = -3;
	
	private WebIsRetriever webis = null;
	protected HashMap<String, ISObjectAttr> attributes = new HashMap<String, ISObjectAttr>();
	public ISObjectAttr getAttr(String key){
		return attributes.get(key);
	}
	
	private boolean doUpdate = true;
	public void doUpdate(boolean doUpdate){
		this.doUpdate = doUpdate;
	}
	public boolean doUpdate(){
		return doUpdate;
	}
	
	private String partition;
	public void partition(String partition){
		this.partition = partition;
	}
	public String partition(){
		return partition;
	}
	private String provider;
	public void provider(String provider){
		this.provider = provider;
	}
	public String provider(){
		return provider;
	}

	private String info_name;
	public void info_name(String info_name){
		this.info_name = info_name;
	}
	public String info_name(){
		return info_name;
	}
	
	

	public ISInfo(String partition, String provider, String info_name, CernCookie cookie){ 
		provider(provider);
		info_name(info_name);
		partition(partition);
		webis = new WebIsRetriever(partition,cookie);
		
//		Log.v(TAG,"ISProvider: setup for partition = "+partition()+", provider = "+provider()+", info_name = "+info_name());
	}
	
	public int update() throws XmlPullParserException, IOException {
//		Log.v(TAG,"update: inside "+provider()+" "+info_name());

		String path = "/is/" + provider() + "/" + provider() + "." + info_name();

		String xml = webis.getXml(path);
		if(xml.length() == 0){
//			Log.w(TAG,"update: xml is empty: "+xml);
			return XML_EMPTY;
		}
		else if(xml.contains("CERN Authentication")){
//			Log.w(TAG,"update: xml is CERN SSO page");
			return XML_IS_SSO;
		}
		else if(xml.contains(WebIsRetriever.IS_OBJECT_NOT_FOUND)){
			return PARTITION_DOWN;
		}

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput(new StringReader(xml));

		int eventType = xpp.getEventType();
		ISObjectAttr attr = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
//			if(eventType == XmlPullParser.START_DOCUMENT) {
//				Log.v(TAG,"update: Start document");
//			} 
			if(eventType == XmlPullParser.START_TAG) {
//				Log.v(TAG,"update: Start tag "+xpp.getName());
				if(xpp.getName().contains("attr")){
//					Log.v(TAG,"update: In tag attr");
					attr = new ISObjectAttr();
					for(int i=0;i<xpp.getAttributeCount();++i){
//						Log.v(TAG,"update: Setting attribute "+xpp.getAttributeName(i)+"="+xpp.getAttributeValue(i));
						attr.setValue(xpp.getAttributeName(i), xpp.getAttributeValue(i));
					}
				}
				else if(xpp.getName().contains("v")){
					eventType = xpp.next();
					if(eventType == XmlPullParser.TEXT){
						String text = xpp.getText();
//						Log.v(TAG,"update: Getting value text: "+ text);
						if(attr != null){ // if == null, something strange is going on
							
							if(attr.name().contains("value") && provider().contains("LHC") && info_name().contains("LHCPage1Msg")){
								String tmp = text.replace("\n", "; ");
								text = tmp;
								tmp = text.replace(";  ;", ";");
								text = tmp;
								tmp = text.replace(";  ;", ";");
								text = tmp;
								tmp = text.replace(";  ;", ";");
								text = tmp;
								tmp = text.replace("; ;", ";");
								text = tmp;
								tmp = text.replace("; ;", ";");
								text = tmp;
								tmp = text.replace("; ;", ";");
								text = tmp;
								if(text.startsWith(";")){
									tmp = text.replaceFirst(";","");
									text = tmp;
								}
								else if(text.startsWith(" ;")){
									tmp = text.replaceFirst(" ;","");
									text = tmp;
								}
								if(text.endsWith(";")){
									tmp = text.substring(0,text.length()-1);
									text = tmp;
								}
								else if(text.endsWith("; ")){
									tmp = text.substring(0,text.length()-2);
									text = tmp;
								}
								else if(text.endsWith(";  ")){
									tmp = text.substring(0,text.length()-3);
									text = tmp;
								}
							}
							
							attr.addValue(text);
						}
					}
				}

			} 
			else if(eventType == XmlPullParser.END_TAG) {
//				Log.v(TAG,"update: End tag "+xpp.getName());
				if(xpp.getName().contains("attr")){
					attributes.put(attr.name(),attr);
					attr = null;
				}
			} 
//			else if(eventType == XmlPullParser.TEXT) {
//				Log.v(TAG,"update: Text "+xpp.getText());
//			} 
//			else{
//				Log.v(TAG,"update: eventType not recognized.");
//			}
			eventType = xpp.next();
		}
		
//		for( int i=0 ; i < attributes.size(); ++i ){
//			String name = (String) attributes.keySet().toArray()[i];
//			attr = (IS_XML_Attr) attributes.values().toArray()[i];
//			Log.v(TAG,"update: Loop found: "+name);
//			Log.v(TAG,"update:     attr: "+attr.toString());
//		}
		
		updateViews();
		
		return 0;
	}
	
	public void updateCookie(CernCookie cookie){
		webis.cookie(cookie);
	}
	
	protected void updateViews(){
//		Log.v(TAG,"updateViews: inside");
	}


//	private final String TAG = "ISInfo";
}
