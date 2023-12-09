package com.pt_plus.Model;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    public String orderDate;
    public List<ProductModel> productModelList;

    public AddressModel addressModel;
    public String orderId;
    public double amount;
    public String  currency;
    public String  status;
    public String  ordered_on;

    @Override
    public String toString() {
        return "OrderModel{" +
                "orderDate='" + orderDate + '\'' +
                ", productModelList=" + productModelList +
                ", addressModel=" + addressModel +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", ordered_on='" + ordered_on + '\'' +
                ", id='" + id + '\'' +
                ", isSingleProductOrder=" + isSingleProductOrder +
                '}';
    }

    public String id;


    public boolean isSingleProductOrder = false;

}
