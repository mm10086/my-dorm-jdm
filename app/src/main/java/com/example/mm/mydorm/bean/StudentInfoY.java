package com.example.mm.mydorm.bean;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mm on 2017/12/17.
 */

public class StudentInfoY {
    private String errcode;
    private Map<String, String> data = new LinkedHashMap<String, String>();
    public StudentInfoY() {
        data.put("studentid", "");
        data.put("name", "");
        data.put("gender", "");
        data.put("vcode", "");
        data.put("room", "");
        data.put("building", "");
        data.put("location", "");
        data.put("grade", "");
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String toString() {
        String str = "name:"+ data.get("name") + "studentid:" + data.get("studentid") +
                        "gender:" + data.get("gender") + "vcode:" + data.get("vcode") +
                        "room:" + data.get("room") + "building" + data.get("building") +
                        "location" + data.get("location") + "grade" + data.get("grade");
        return str;
    }
}
