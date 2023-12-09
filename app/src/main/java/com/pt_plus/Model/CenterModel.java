package com.pt_plus.Model;

import java.io.Serializable;

public class CenterModel implements Serializable {
    public  int drwableId;
    public String name;
    public String id;
    public String type;

    @Override
    public String toString() {
        return "CenterModel{" +
                "drwableId=" + drwableId +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", center_id='" + center_id + '\'' +
                ", about='" + about + '\'' +
                ", num_of_trainers='" + num_of_trainers + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                '}';
    }

    public String center_id;
    public String about;

    public String num_of_trainers;

    public String thumbnail_img;

}
