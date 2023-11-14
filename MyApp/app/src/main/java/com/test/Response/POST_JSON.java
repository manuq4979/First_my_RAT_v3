package com.test.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POST_JSON {

    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("body")
    @Expose
    private String body;


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
}
