package com.susankya.yubahunkar.model.post_detail_model;


import com.google.gson.annotations.SerializedName;


public class RepliesItem{

	@SerializedName("attributes")
	private Attributes attributes;

	@SerializedName("href")
	private String href;

	public void setAttributes(Attributes attributes){
		this.attributes = attributes;
	}

	public Attributes getAttributes(){
		return attributes;
	}

	public void setHref(String href){
		this.href = href;
	}

	public String getHref(){
		return href;
	}

	@Override
 	public String toString(){
		return 
			"RepliesItem{" + 
			"attributes = '" + attributes + '\'' + 
			",href = '" + href + '\'' + 
			"}";
		}
}