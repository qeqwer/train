package com.niko.train.member.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum PassengerTypeEnums {

    ADULT("1", "成人"),
    CHILD("2", "儿童"),
    STUDENT("3", "学生");

    private String code;

    private String desc;

    PassengerTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static List<HashMap<String,String>> getEnumList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        for (PassengerTypeEnums anEnum : EnumSet.allOf(PassengerTypeEnums.class)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code",anEnum.code);
            map.put("desc",anEnum.desc);
            list.add(map);
        }
        return list;
    }
}
