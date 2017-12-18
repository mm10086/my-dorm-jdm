package com.example.mm.mydorm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mm on 2017/12/16.
 */

public class LoginInfo {
    private String errcode;
    private Map<String, String> data = new LinkedHashMap<String, String>();
    public LoginInfo() {
       data.put("errmsg", "");
    }


    public String toString() {
        return "{ \"errcode\":" + errcode + ", \"data\": { \"errmsg\": \"" + data.get("errmsg") + "\"}}";
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
