package com.pt_plus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CenterFilterModel implements Serializable {

  public FilterModel main_category  = new FilterModel();
  public FilterModel category  = new FilterModel();

  @Override
  public String toString() {
    return "CenterFilterModel{" +
            "main_category=" + main_category +
            ", category=" + category +
            ", type=" + type +
            '}';
  }

  public FilterModel type  = new FilterModel();



}
