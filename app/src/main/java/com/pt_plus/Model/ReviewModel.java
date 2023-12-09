package com.pt_plus.Model;

import java.io.Serializable;

public class ReviewModel implements Serializable {

    public String user_dp;
    public String user_name;
    public long totalCount;


    public String review;

    @Override
    public String toString() {
        return "ReviewModel{" +
                "user_dp='" + user_dp + '\'' +
                ", user_name='" + user_name + '\'' +
                ", totalCount=" + totalCount +
                ", review='" + review + '\'' +
                '}';
    }
}
