package com.susankya.yubahunkar.model.post_detail_model;


import com.google.gson.annotations.SerializedName;


public class Attributes{

	@SerializedName("embeddable")
	private boolean embeddable;

	public void setEmbeddable(boolean embeddable){
		this.embeddable = embeddable;
	}

	public boolean isEmbeddable(){
		return embeddable;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"embeddable = '" + embeddable + '\'' + 
			"}";
		}
}