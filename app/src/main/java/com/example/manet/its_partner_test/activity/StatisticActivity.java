package com.example.manet.its_partner_test.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.manet.its_partner_test.R;
import com.example.manet.its_partner_test.util.KeyString;
import com.example.manet.its_partner_test.util.StringViewFormat;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_statistic);
        SharedPreferences sharedPref = getSharedPreferences(KeyString.STATISTIC,MODE_PRIVATE);
        int trainNumber = sharedPref.getInt(KeyString.TRAINS_NUMBER, 0);
        String[] statistics = new String[trainNumber];
        for (int i = trainNumber; i >= 1; i--){
            StringViewFormat stringViewFormat = new StringViewFormat();
            long startTime = sharedPref.getLong(KeyString.START_TIME+i, -1);
            int counter = sharedPref.getInt(KeyString.COUNTER+i, -1);
            long time = sharedPref.getLong(KeyString.TRAIN+i, -1);
            String info = stringViewFormat.stringGenerate(i, startTime, time, counter);
            statistics[trainNumber - i] = info;
        }
        ListView statisticList = (ListView) findViewById(R.id.statistic_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,statistics);
        statisticList.setAdapter(adapter);
    }

    public void back(View button){
        Intent intent = new Intent(this, CounterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

}
