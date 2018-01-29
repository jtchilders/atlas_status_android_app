package com.parton.atlasStatus;

import java.util.ArrayList;

public class ISObjectAttr {
	private String name = "";
	public void name(String name){
		this.name = name;
	}
	public String name(){
		return name;
	}
	
	private String descr = "";
	public void descr(String descr){
		this.descr = descr;
	}
	public String descr(){
		return descr;
	}

	private String type = "";
	public void type(String type){
		this.type = type;
	}
	public String type(){
		return type;
	}

	private String range = "";
	public void range(String range){
		this.range = range;
	}
	public String range(){
		return range;
	}

	private String multi = "";
	public void multi(String multi){
		this.multi = multi;
	}
	public String multi(){
		return multi;
	}

	
	ArrayList<String> values = new ArrayList<String>();
	public void addValue(String value){
		values.add(value);
	}
	public String values(int i){
		return values.get(i);
	}
	public int numValues(){
		return values.size();
	}
	
	public void setValue(String var_name,String var_value){
		if(var_name.contains("name"))
			this.name = var_value;
		else if(var_name.contains("descr"))
			this.descr = var_value;
		else if(var_name.contains("type"))
			this.type = var_value;
		else if(var_name.contains("range"))
			this.range = var_value;
		else if(var_name.contains("multi"))
			this.multi = var_value;
//		else
//			Log.e(TAG,"setValue: attribute name not found: "+var_name+"="+var_value);
	}
	
	public String toString(){
		String out = "name = "+name()+", descr = "+descr()+", type = "+type()+", range = "+range()+", multi = "+multi()+", number of values = "+numValues();
		return out;
	}
	
	
	
//	private final String TAG = "ISObjectAttr";
}
