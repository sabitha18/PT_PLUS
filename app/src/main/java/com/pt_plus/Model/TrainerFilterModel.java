package com.pt_plus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrainerFilterModel implements Serializable {

  public List<FilterModel> serviceType  = new ArrayList<>();

  public List<CommonCardCategoryModel> commonCardCategoryModel  = new ArrayList<>();

  public List<FilterModel> activities  = new ArrayList<>();
  public List<FilterModel> availability  = new ArrayList<>();
  public List<FilterModel> genter  = new ArrayList<>();
  public LocationModel locationModel;

  @Override
  public String toString() {
    return "TrainerFilterModel{" +
            "serviceType=" + serviceType +
            ", commonCardCategoryModel=" + commonCardCategoryModel +
            ", activities=" + activities +
            ", availability=" + availability +
            ", genter=" + genter +
            ", locationModel=" + locationModel +
            ", experience=" + experience +
            ", price=" + price +
            ", rating='" + rating + '\'' +
            ", latitude='" + latitude + '\'' +
            ", longitude='" + longitude + '\'' +
            '}';
  }

  public List<String> experience  = new ArrayList<>();
  public List<String> price  = new ArrayList<>();
  public String rating;
  public String latitude;
  public String longitude;

}
