package com.parton.atlasStatus;

import android.util.Log;
import com.parton.atlasStatus.R;

public class IS_XML_Attr {
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

	private String value = "";
	public void value(String value){
		this.value = value;
	}
	public String value(){
		return value;
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
		else if(var_name.contains("value"))
			this.value = var_value;
		else
			Log.e(TAG,"setValue: attribute name not found: "+var_name+"="+var_value);
	}
	
	public String toString(){
		String out = "name = "+name()+", descr = "+descr()+", type = "+type()+", range = "+range()+", multi = "+multi()+", value = "+value();
		return out;
	}
	
	private final String TAG = "IS_XML_Attr";
}
