package com.pt_plus.Model;

import java.io.Serializable;
import java.util.Objects;

public class SelectBoxModel implements Serializable {
    public long id ;
    public String title ;
    public String type ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectBoxModel that = (SelectBoxModel) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }



    @Override
    public String toString() {
        return "SelectBoxModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public boolean isSelected = false ;
}
