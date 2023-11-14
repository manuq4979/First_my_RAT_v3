package com.test.Modules;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.test.Models.MySms;
import com.test.myapp.MyHandlers;
import com.test.myapp.TcpClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GPSModule extends Service {
    // может содержать причину по которой не может запуститься:
    public static String GPSModule_start = "stopped"; // на сервер идет сразу после попытки запустить модуль

    public static long minTimeMs = 3000; // минимальное время через которое обнавлять данные gps
    public static long minDistanceM = 0; // минимальное растояние которое учитывать при обнавлении данных gps
    public static double latitude = 0.0; // на сервер первым идет
    public static double longting = 0.0; // на сервер идет вторым
    public static float letliudid = 0.0f; // на сервер идет третьем
    public static float speed = 0.0f;
    public static LocationListener locationListener;
    public static LocationManager locationManager;




    public static ArrayList<String> getResponse() {

        // Чтобы пустые данные не отправлять каждую милисекунду, буду отправлять как только LocationListener их обновит:
        // SystemClock.sleep(minTimeMs); - не лучшая идея..

        ArrayList<String> response = new ArrayList<>();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        String str_latitude = String.valueOf(latitude);
        String str_longting = String.valueOf(longting);
        String str_letliudid = String.valueOf(letliudid);


        response.add(str_latitude);
        response.add(str_longting);
        response.add(str_letliudid);

        response.add(dateText);
        response.add(timeText);

        return response;
    }

    public static String getStatusGPSModule() {
        return GPSModule_start;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Доходит до запуска сервиса -------------------------- !!!");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps_enable) {
            GPSModule_start = "GPSModule started successfully!";
            startModule();
        } else {
            GPSModule_start = "GPSModule not started. GPS disable!";
            stopModule();
        }

        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startModule() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 1. Создание слушателя локации:
        locationListener = new LocationListener() {
            public void onLocationChanged(Location L) {
                if (L != null) {
                    longting = L.getLongitude(); // Получите долготу в градусах.
                    latitude = L.getLatitude(); // Получите широту в градусах
                    letliudid = L.getAccuracy(); // Получите процентильный уровень достоверности
                    speed = L.getSpeed(); // Возвращает скорость в момент расположения в метрах в секунду
                    // sendToServer(); // отправка данных на сервер
                    System.out.println("onLocationChange: "+"Ширина: " + latitude + " Долгота: " + longting + "\n");
                    boolean gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (gps_enable) {
                        try {
                            locationManager.removeUpdates(locationListener); // я так понял, это данные ширины, долготы актуальности и прч удаляется
                        } catch (Exception e) {
                        }

                        // тут по идеи должна быть релаизована проверка разрешений, что тут собствнно и сделано
                        // это требуется для метода  locationManager.requestLocationUpdates(...);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // здесь, чтобы запросить недостающие разрешения, а затем переопределить
                            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                        int[] grantResults)
                            // для обработки случая, когда пользователь предоставляет разрешение. См. документацию
                            // для ActivityCompat#requestPermissions для более подробной информации.
                            //
                            //Переведено с помощью www.DeepL.com/Translator (бесплатная версия)

                            // но я этого делать не буду, разрешения уже выданы )
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMs, minDistanceM, locationListener); // Зарегистрируйтесь для получения обновлений местоположения от данного поставщика с заданными аргументами и Обратный вызов вызывающего потока. Looper
                    }

                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Код ниже у меня не сработал, ну это не удивительно, ведь код выше зацикливается - Вероятно автор оставил код ниже как запуску
        // или он работает на Android ниже 13 версии.
        // 2. Создание условия, по которому будет определено по GPS или интернету будет определяться местоположение

        boolean net_enable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enable && !net_enable) {
            startModule();
        }
        else {
            if (net_enable) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    longting = location.getLongitude();
                    latitude = location.getLatitude();
                    letliudid = location.getAccuracy();
                    speed = location.getSpeed();
                    // s(latitude, longting, letliudid);
                    System.out.println("Internet: "+"Ширина: " + latitude + " Долгота: " + longting + "\n");


                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeMs, minDistanceM, locationListener);
            } else {
                if (gps_enable) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        longting = location.getLongitude();
                        latitude = location.getLatitude();
                        letliudid = location.getAccuracy();
                        speed = location.getSpeed();
                        // s(latitude, longting, letliudid);
                        System.out.println("GPS: "+"Ширина: " + latitude + " Долгота: " + longting + "\n");
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMs, minDistanceM, locationListener);
                }
            }
        }

    }

    private void getLocationForInternet() {
        // это сейчас не главное, реализую позже
    }


    private void stopModule() {
        if (locationListener != null) {
            Intent i = new Intent(this, GPSModule.class);
            this.stopService(i);
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }

        GPSModule_start = "stopped";
    }


}