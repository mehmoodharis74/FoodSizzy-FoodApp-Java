package com.harismehmood.finalproject.activities.Messaging;

import android.app.Application;

import com.onesignal.OneSignal;
//import oneSignal.OneSignal;

public class FCM_Messaging extends Application {
    private static final String ONESIGNAL_APP_ID = "bc07306f-3b14-48fc-83da-fa7cbef1b428";
    @Override
    public void onCreate() {
        super.onCreate();
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}



