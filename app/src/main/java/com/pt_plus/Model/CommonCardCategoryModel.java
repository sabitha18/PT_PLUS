package com.pt_plus.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CommonCardCategoryModel implements Serializable {
    public  int drwableId;
    public String name;
    public String thumbnail_img;
    public String id;
    public boolean isSelected = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonCardCategoryModel that = (CommonCardCategoryModel) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "CommonCardCategoryModel{" +
                "drwableId=" + drwableId +
                ", name='" + name + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                ", id='" + id + '\'' +
                ", isSelected=" + isSelected +
                ", subCat=" + subCat +
                ", centerModelList=" + centerModelList +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }

    public List<CommonCardCategoryModel> subCat ;
    public List<CenterModel> centerModelList ;

    public String categoryId;

}
