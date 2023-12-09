package com.pt_plus.Model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    public  int drwableId;
    public String name;
    public String thumbnail_img;
    public String price;
    public String productId;
    public String id;
    public boolean wishlisted = false;

    @Override
    public String toString() {
        return "ProductModel{" +
                "drwableId=" + drwableId +
                ", name='" + name + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                ", price='" + price + '\'' +
                ", productId='" + productId + '\'' +
                ", id='" + id + '\'' +
                ", wishlisted=" + wishlisted +
                ", cartId='" + cartId + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", qty=" + qty +
                ", currency='" + currency + '\'' +
                '}';
    }

    public String cartId;
    public String brand;

    public String description;

    public long stock;

    public long qty;

    public String currency;


}
