package com.pt_plus.Model;

import java.io.Serializable;
import java.util.Objects;

public class LocationModel implements Serializable {



    public String name;
    public String id;
    public boolean isSelected = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationModel that = (LocationModel) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
