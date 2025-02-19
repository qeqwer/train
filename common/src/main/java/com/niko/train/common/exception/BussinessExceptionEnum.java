package com.niko.train.common.exception;

public enum BussinessExceptionEnum {
    MEMBER_MOBILE_EXIST("手机号已注册");

    private String desc;

    BussinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "BussinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                "} " + super.toString();
    }
}
