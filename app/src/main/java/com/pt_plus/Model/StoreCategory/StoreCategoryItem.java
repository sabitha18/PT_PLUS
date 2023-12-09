package com.pt_plus.Model.StoreCategory;

import com.google.gson.annotations.SerializedName;

public class StoreCategoryItem{

	@SerializedName("thumbnail_img")
	private String thumbnailImg;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	public String getThumbnailImg(){
		return thumbnailImg;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}
}