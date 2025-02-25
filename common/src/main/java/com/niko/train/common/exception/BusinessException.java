package com.niko.train.common.exception;

public class BusinessException extends RuntimeException{
    private BussinessExceptionEnum e;

    public BusinessException(BussinessExceptionEnum e) {
        this.e = e;
    }
    public BussinessExceptionEnum getE() {
        return e;
    }
    public void setE(BussinessExceptionEnum e) {
        this.e = e;
    }


    //不写入堆栈信息，提高性能
    @Override
    public  Throwable fillInStackTrace(){ return this; }
}
