package com.test.Modules;

import java.util.ArrayList;

public class SystemProperty {

    public static ArrayList<String> getSystemProperty() {
        ArrayList<String> systemProperty = new ArrayList<>();
        systemProperty.add(System.getProperty("os.version"));
        systemProperty.add(String.valueOf(android.os.Build.VERSION.SDK_INT));
        systemProperty.add(android.os.Build.DEVICE);
        systemProperty.add(android.os.Build.MODEL);
        systemProperty.add(android.os.Build.PRODUCT);

        return systemProperty;
    }
}
