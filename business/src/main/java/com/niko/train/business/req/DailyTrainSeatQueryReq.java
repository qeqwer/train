package com.niko.train.business.req;

import com.niko.train.common.req.PageReq;

public class DailyTrainSeatQueryReq extends PageReq {

    private String trainCode;


    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "DailyTrainSeatQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}