package com.test.myapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.test.Modules.BatteryModule;

public class MyService extends Service {

    static final int notification_id = 342323232;
    public static TcpClient tcpClient;
    public static PowerManager mgr = null;
    private BroadcastReceiver screenReceiver = null;

    PowerManager.WakeLock wakeLock = null;




    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;
            BatteryModule.BATTERY_LEVEL = String.valueOf(batteryPct) + "%";
        }
    };

    // msg - это body POST_JSON класса
    // под вопросом, планирую отображать результат на сервере
    public void sendMessageToActivity(String msg) {
        Intent intent = new Intent("ACTION_VIEW");
        // You can also include some extra data.
        intent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {

        MyAccessibilityService.SERVICE_STARTED = true;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }

        Log.i("Test", "Service: onCreate");

        // появился в 11 Android
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID").setSmallIcon(R.drawable.ic_launcher_background);

        // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //    builder = new NotificationCompat.Builder(this, "CHANNEL_ID").setSmallIcon(R.drawable.ic_launcher_background);
        // }

        Notification notification;

        notification = builder.build();
        startForeground(notification_id, notification);


        // регистрирую ресивер
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        // initialize receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        screenReceiver = new ScreenReceiver();
        registerReceiver(screenReceiver, filter);

        // создаю Wake Lock для своего приложения:
        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        if (wakeLock == null)
        {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PeriSecure:MyWakeLock");
        }
        if (wakeLock != null)
        {
            if (wakeLock.isHeld() == false)
            {
                wakeLock.acquire();
            }
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // http://4.tcp.eu.ngrok.io:19120 80

        tcpClient = new TcpClient(this); // клиент так запускается
        tcpClient.runThread();


        // чтобы сервис автоматически запустился, если был отключен системой
        return START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        if (wakeLock != null)
        {
            if (wakeLock.isHeld() == true)
            {
                wakeLock.release();
            }
        }
    }

    @Override
    public void onDestroy() {
        // отменяю регистрацию ресивера при уничтожении данного сервиса
        unregisterReceiver(mBatInfoReceiver);

        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
            screenReceiver = null;
        }
        super.onDestroy();

        if (wakeLock != null)
        {
            if (wakeLock.isHeld() == true)
            {
                wakeLock.release();
            }
        }
    }
}