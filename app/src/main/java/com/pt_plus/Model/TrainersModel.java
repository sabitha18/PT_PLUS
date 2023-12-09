package com.pt_plus.Model;

import java.io.Serializable;

public class TrainersModel implements Serializable {
    public  int drwableId;
    public String name;
    public String id;

    public String trainer_id;
    public String profile_picture;

    public String category;
    public String speclization;
    public String description;

    public String distance;

    public String experiance;

    @Override
    public String toString() {
        return "TrainersModel{" +
                "drwableId=" + drwableId +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", trainer_id='" + trainer_id + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", category='" + category + '\'' +
                ", speclization='" + speclization + '\'' +
                ", description='" + description + '\'' +
                ", distance='" + distance + '\'' +
                ", experiance='" + experiance + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }

    public String rating;

}
