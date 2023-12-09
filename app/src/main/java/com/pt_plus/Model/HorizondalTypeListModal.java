package com.pt_plus.Model;

import java.io.Serializable;

public class HorizondalTypeListModal implements Serializable {
    @Override
    public String toString() {
        return "HorizondalTypeListModal{" +
                "isSelected=" + isSelected +
                ", title='" + title + '\'' +
                '}';
    }

    public boolean isSelected;
    public String title;

}
