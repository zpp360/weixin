package com.shuheng.weixin.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ApiData implements Serializable {

    private static final long serialVersionUID = 8689409753312953532L;


    private String errorCode;
    private String errorMsg;
    private Map<String, String> data = null;
    private List<Map<String,String>> datas = null;


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public List<Map<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "ApiData{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                ", datas=" + datas +
                '}';
    }
}
