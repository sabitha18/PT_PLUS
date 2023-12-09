package com.pt_plus.Model;

import java.io.Serializable;

public class ChatDetailsModel implements Serializable {

    public  boolean isSelf ;
    public String message;

    @Override
    public String toString() {
        return "ChatDetailsModel{" +
                "isSelf=" + isSelf +
                ", message='" + message + '\'' +
                '}';
    }
}
