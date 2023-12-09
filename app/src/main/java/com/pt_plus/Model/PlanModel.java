package com.pt_plus.Model;

import java.io.Serializable;

public class PlanModel implements Serializable {
    public  int drwableId;
    public String title;
    public String id;
    public String price;

    @Override
    public String toString() {
        return "PlanModel{" +
                "drwableId=" + drwableId +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", num_of_trainers='" + num_of_trainers + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                '}';
    }

    public String currency;

    public String plan_id;
    public String sub_title;

    public String num_of_trainers;

    public String thumbnail_img;

}
