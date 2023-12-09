package com.pt_plus.Model;

import java.io.Serializable;

public class AddressModel implements Serializable {
    public String id;
    public String fullName;
    public String email;
    public String country;
    public long countryId;
    public String mobile;
    public String mobilePrefix;
    public String gender;
    public String zipCOde;
    public String streetName;
    public String Area;
    public long areaId;
    public String block;

    @Override
    public String toString() {
        return "AddressModel{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", countryId=" + countryId +
                ", mobile='" + mobile + '\'' +
                ", mobilePrefix='" + mobilePrefix + '\'' +
                ", gender='" + gender + '\'' +
                ", zipCOde='" + zipCOde + '\'' +
                ", streetName='" + streetName + '\'' +
                ", Area='" + Area + '\'' +
                ", areaId=" + areaId +
                ", block='" + block + '\'' +
                ", building='" + building + '\'' +
                ", titile='" + titile + '\'' +
                ", blockId=" + blockId +
                ", AddressType='" + AddressType + '\'' +
                '}';
    }

    public String building;

    public String titile;
    public long blockId;

    public String  AddressType;
}
