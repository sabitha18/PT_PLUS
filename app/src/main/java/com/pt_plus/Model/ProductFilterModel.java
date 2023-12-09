package com.pt_plus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductFilterModel implements Serializable {



  public List<CommonCardCategoryModel> commonCardCategoryModel  = new ArrayList<>();

  public String sort_price;
  public int newest = 0;
  public int featured = 0;

  @Override
  public String toString() {
    return "ProductFilterModel{" +
            "commonCardCategoryModel=" + commonCardCategoryModel +
            ", sort_price='" + sort_price + '\'' +
            ", newest=" + newest +
            ", featured=" + featured +
            '}';
  }
}
