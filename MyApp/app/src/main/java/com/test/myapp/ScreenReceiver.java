// РЕСИВЕР НЕ ИСПОЛЬЗУЕТСЯ!
package com.test.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            wasScreenOn = false;
            System.out.println("Экран: выключен");
            // Включить блокировку спящего режима для своего приложения:
            // MyService.wakeLock.acquire();
            // System.out.println("Включить блокировку спящего режима для своего приложения");

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;
            System.out.println("Экран: включен");
            // Выключить блокировку спящего режима для своего приложения:
            // MyService.wakeLock.release();
            // System.out.println("Выключить блокировку спящего режима для своего приложения");
        }
    }

}