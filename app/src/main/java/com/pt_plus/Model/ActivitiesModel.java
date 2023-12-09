package com.pt_plus.Model;

import java.io.Serializable;

public class ActivitiesModel implements Serializable {
    public  int drwableId;
    public String title;
    public String id;
    public String imgUrl;

    @Override
    public String toString() {
        return "ActivitiesModel{" +
                "drwableId=" + drwableId +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
