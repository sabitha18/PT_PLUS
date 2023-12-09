package com.pt_plus.Model;

import java.io.Serializable;

public class GalleryModel implements Serializable {
    public  int bgId;
    public String title;
    public String id;
    public String thumbnail_img;

    @Override
    public String toString() {
        return "GalleryModel{" +
                "bgId=" + bgId +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                ", isVideo=" + isVideo +
                ", video='" + video + '\'' +
                ", type='" + type + '\'' +
                ", iconId=" + iconId +
                '}';
    }

    public boolean isVideo = false;
    public String video;

    public String type;
    public int iconId;

}
