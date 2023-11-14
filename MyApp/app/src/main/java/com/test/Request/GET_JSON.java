package com.test.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GET_JSON {

    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("msg")
    @Expose
    private String msg;



    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
