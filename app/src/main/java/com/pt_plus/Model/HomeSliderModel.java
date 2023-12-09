package com.pt_plus.Model;

import java.io.Serializable;

public class HomeSliderModel implements Serializable {
    public String imgUrl;
    public String title;
public int totalCount = 0;
public String btnText;

    @Override
    public String toString() {
        return "HomeSliderModel{" +
                "imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                ", totalCount=" + totalCount +
                ", btnText='" + btnText + '\'' +
                ", related_product_id='" + related_product_id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String related_product_id;

    public String description;

}
