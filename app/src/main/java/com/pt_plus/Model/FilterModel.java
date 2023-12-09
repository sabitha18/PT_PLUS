package com.pt_plus.Model;

import java.io.Serializable;
import java.util.Objects;

public class FilterModel implements Serializable {


    public String group;
    public String title;
    public String id;
    public boolean isSelected = false;

    @Override
    public String toString() {
        return "FilterModel{" +
                "group='" + group + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterModel that = (FilterModel) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
