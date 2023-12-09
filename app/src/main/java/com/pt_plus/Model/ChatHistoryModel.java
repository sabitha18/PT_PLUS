package com.pt_plus.Model;

import java.io.Serializable;

public class ChatHistoryModel implements Serializable {
    @Override
    public String toString() {
        return "ChatHistoryModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", last_message='" + last_message + '\'' +
                ", last_message_time='" + last_message_time + '\'' +
                ", chat_with_id='" + chat_with_id + '\'' +
                ", thumbnail_img='" + thumbnail_img + '\'' +
                '}';
    }

    public String name;
    public String id;
    public String last_message;
    public String last_message_time;
    public String chat_with_id;


    public String thumbnail_img;

}
