package com.test.Modules;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.Date;

public class CallModule {

    // Простой для чтения, но использоваться не будет, он не группирует звонки:
    public void getAllCall(Context context) {
        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };
        String where = "";

        // Получаем таблицу в Cursor через URI:
        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                where,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                long _id = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls._ID));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
    }


    // получить контакты:
    public static String getContactList(Context context) {
        StringBuffer sb = new StringBuffer();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        sb.append("Contacts: \n");
        if ((cur != null ? cur.getCount() : 0) >= 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                if (0 <= cur.getInt(cur.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("Name: " + name+ "\n" + "Phone Number: " + phoneNo + "\n");
                        sb.append("\n----------------------------------");
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        return sb.toString();
    }

    // получит все звонки:
    public static String getCallDetails(Context context) {
        StringBuffer sb = new StringBuffer();
        ContentResolver cr = context.getContentResolver();
        Cursor managedCursor = cr.query(CallLog.Calls.CONTENT_URI,null, null,null, null);

        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.parseLong(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            sb.append("\n----------------------------------\n");
        }
        managedCursor.close();
        // call.setText(sb); // Текстом будет StringBuffer на главном экране
        return sb.toString();
    }
}
