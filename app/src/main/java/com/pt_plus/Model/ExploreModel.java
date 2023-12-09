package com.pt_plus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExploreModel implements Serializable {


    public List<TrainersModel> trainersModelList = new ArrayList<>();
    public List<ProductModel> productModelList = new ArrayList<>();
    public List<CommonCardCategoryModel> centerModelList = new ArrayList<>();

    @Override
    public String toString() {
        return "ExploreModel{" +
                "trainersModelList=" + trainersModelList +
                ", productModelList=" + productModelList +
                ", centerModelList=" + centerModelList +
                ", topList=" + topList +
                '}';
    }

    public List<TrainersModel> topList = new ArrayList<>();

}
