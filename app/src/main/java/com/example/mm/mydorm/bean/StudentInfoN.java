package com.example.mm.mydorm.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mm on 2017/12/17.
 */

public class StudentInfoN {
    private String errcode;
    private Map<String, String> data = new LinkedHashMap<String, String>();

    public StudentInfoN() {
        data.put("studentid", "");
        data.put("name", "");
        data.put("gender", "");
        data.put("vcode", "");
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
}
