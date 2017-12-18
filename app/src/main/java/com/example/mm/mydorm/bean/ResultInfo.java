package com.example.mm.mydorm.bean;

/**
 * Created by mm on 2017/12/18.
 */

public class ResultInfo {
    private String errcode;
    public ResultInfo() { errcode = "1";}

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    @Override
    public String toString() {
        return "这里的errcode:" + errcode;
    }
}
