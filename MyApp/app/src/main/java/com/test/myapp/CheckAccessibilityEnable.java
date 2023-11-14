package com.test.myapp;

import android.content.Context;
import android.content.Intent;

public class CheckAccessibilityEnable {

    private Context context;

    public CheckAccessibilityEnable(Context context) {
        this.context = context;
    }

    public void startCheckAccess() {
        System.out.println("1 --------------------------------------------------");
        if (!OptionAccessibilityClass.checkAccessibilityEnable(context.getApplicationContext(), MyAccessibilityService.class)) {
            System.out.println("0 --------------------------------------------------");

            context.startActivity(new Intent(context, AccessAskActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}

