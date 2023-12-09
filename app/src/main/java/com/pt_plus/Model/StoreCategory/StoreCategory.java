package com.pt_plus.Model.StoreCategory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StoreCategory{

	@SerializedName("StoreCategory")
	private List<StoreCategoryItem> storeCategory;

	public List<StoreCategoryItem> getStoreCategory(){
		return storeCategory;
	}
}