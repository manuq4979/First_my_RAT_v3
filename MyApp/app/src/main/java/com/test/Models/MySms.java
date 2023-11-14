package com.test.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MySms {

    @SerializedName("header")
    @Expose
    private String header;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("body")
    @Expose
    private String body;


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class GPSData_JSON {
        @SerializedName("header")
        @Expose
        private String header;
        // @SerializedName("body")
        @Expose
        private ArrayList<String> body;


        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }
        public ArrayList<String> getBody() {
            return body;
        }

        public void setBody(ArrayList<String> body) {
            this.body = body;
        }
    }
}
