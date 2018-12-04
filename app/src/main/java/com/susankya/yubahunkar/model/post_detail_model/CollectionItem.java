package com.susankya.yubahunkar.model.post_detail_model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class CollectionItem{

	@SerializedName("attributes")
	private List<Object> attributes;

	@SerializedName("href")
	private String href;

	public void setAttributes(List<Object> attributes){
		this.attributes = attributes;
	}

	public List<Object> getAttributes(){
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
			"CollectionItem{" + 
			"attributes = '" + attributes + '\'' + 
			",href = '" + href + '\'' + 
			"}";
		}
}