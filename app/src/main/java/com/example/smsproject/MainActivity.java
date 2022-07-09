package com.example.smsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.smsproject.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean activated = prefs.getBoolean("status",false);
        Log.d("TAG",activated+" ||");
        if(!activated) {
            binding.button2.setBackgroundColor(getResources().getColor(R.color.purple_700,getTheme()));
            binding.button2.setText("Включить");
         }
        else {
            binding.button2.setBackgroundColor(getResources().getColor(R.color.teal_700,getTheme()));
            binding.button2.setText("Отключить");
         }
        requestPermissions(
                new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.GET_ACCOUNTS},
                0
        );
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        String phoneNumber = prefs.getString("phone","");
        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;
            Log.d("TAG",actype+" "+acname);
            // Take your time to look at all available accounts
            if(actype.equals("com.whatsapp")) {
                phoneNumber = ac.name;
            }
            if(actype.equals("org.telegram.messenger")){
                phoneNumber = ac.name;
            }
            if(actype.equals("com.viber.voip")){
                phoneNumber = ac.name;
            }
        }
        binding.edit.setText(phoneNumber);
        binding.button3.setOnClickListener(view -> prefs.edit().putString("phone",binding.edit.getText().toString()).apply());
        binding.button2.setOnClickListener(view -> {
            boolean activated1 = prefs.getBoolean("status",false);
            Log.d("TAG",activated1+"");
            if(activated1) {
                binding.button2.setBackgroundColor(getResources().getColor(R.color.purple_700,getTheme()));
                binding.button2.setText("Включить");
                prefs.edit().putBoolean("status",false).apply();
            }
            else {
                binding.button2.setBackgroundColor(getResources().getColor(R.color.teal_700,getTheme()));
                binding.button2.setText("Отключить");
                prefs.edit().putBoolean("status",true).apply();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}