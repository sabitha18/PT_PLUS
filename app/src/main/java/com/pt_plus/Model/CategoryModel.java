package com.pt_plus.Model;

import java.io.Serializable;
import java.util.List;

public class CategoryModel implements Serializable {
    public  int drwableId;
    public String name;
    public String thumbnail_img;

    @Override
    public String toString() {
        return "CategoryModel{" +
                "drwableId=" + drwableId +
                ", name='" + name + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }

    public String categoryId;

}
