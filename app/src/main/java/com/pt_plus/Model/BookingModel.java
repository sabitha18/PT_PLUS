package com.pt_plus.Model;

import java.io.Serializable;

public class BookingModel implements Serializable {


    public PlanModel planModel;

    public String bookingId;

    public String bookingStatus;

    public String booked_on;
    public TrainersModel trainersModel;

    @Override
    public String toString() {
        return "BookingModel{" +
                "planModel=" + planModel +
                ", bookingId='" + bookingId + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", booked_on='" + booked_on + '\'' +
                ", trainersModel=" + trainersModel +
                ", centerModel=" + centerModel +
                ", isFromCenter=" + isFromCenter +
                '}';
    }

    public CenterModel centerModel;
    public boolean isFromCenter = false;

}
