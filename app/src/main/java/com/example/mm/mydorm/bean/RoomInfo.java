package com.example.mm.mydorm.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mm on 2017/12/18.
 */

public class RoomInfo {
    private String errcode;
    private Map<String, Integer> data = new LinkedHashMap<String, Integer>();
    public RoomInfo() {
        data.put("5", 0);
        data.put("13", 0);
        data.put("14", 0);
        data.put("8", 0);
        data.put("9", 0);
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }
}
