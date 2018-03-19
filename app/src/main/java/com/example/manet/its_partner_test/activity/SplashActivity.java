package com.example.manet.its_partner_test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.manet.its_partner_test.R;
import com.example.manet.its_partner_test.activity.CounterActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent (this, CounterActivity.class);
        startActivity(intent);
        finish();
    }
}
