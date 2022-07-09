package com.example.smsproject;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        getApplicationContext();
        registerReceiver(new SmsReceiver(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
}
