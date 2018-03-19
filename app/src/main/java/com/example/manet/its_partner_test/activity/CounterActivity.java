package com.example.manet.its_partner_test.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.manet.its_partner_test.R;
import com.example.manet.its_partner_test.activity.fragment.FinishTrainDialog;
import com.example.manet.its_partner_test.util.KeyString;
import com.example.manet.its_partner_test.util.StateMarker;

public class CounterActivity extends AppCompatActivity implements SensorEventListener {

    private static final int TURN_NUMBER = 15;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int counter;
    private int previousState;
    private int trainState;
    private long start;
    private boolean isItGoodTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_counter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (savedInstanceState != null){
            recover(savedInstanceState);
        }else {
            firstInit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (trainState == StateMarker.TRAIN_START){
            startAccelerometer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAccelerometer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        saver(outState);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] coords = sensorEvent.values;
        double x = coords[0];
        double y = coords[1];
        double z = coords[2];
        if (z > -3.5 && z < 3.5) {
            if (y > - 1 && y < 1){
                if (x < 0){
                    if (previousState == StateMarker.RIGHT_STATE && isItGoodTurn) {
                        counter++;
                    }
                    previousState = StateMarker.LEFT_STATE;
                }
                if (x > 0){
                    if (previousState == StateMarker.LEFT_STATE && isItGoodTurn){
                        counter++;
                    }
                    previousState = StateMarker.RIGHT_STATE;
                }
                isItGoodTurn = true;
            }
        }else{
            isItGoodTurn = false;
        }
        if (counter == TURN_NUMBER){
            finishTrain();
        }
        updateTextView();
    }

    public void changeTrainState(View button){
        Button trainButton = (Button) button;
        if (trainState == StateMarker.TRAIN_STOP){
            trainState = StateMarker.TRAIN_START;
            trainButton.setText(R.string.finish);
            startAccelerometer();
            start = System.currentTimeMillis();
        }else{
            finishTrain();
        }
    }

    private void recover(@NonNull Bundle savedInstanceState){
        counter = savedInstanceState.getInt(KeyString.COUNTER);
        previousState = savedInstanceState.getInt(KeyString.PREVIOUS_STATE);
        trainState = savedInstanceState.getInt(KeyString.TRAIN_STATE);
        isItGoodTurn = savedInstanceState.getBoolean(KeyString.IS_GOOD_TURN);
    }

    private void saver(Bundle outState){
        outState.putInt(KeyString.COUNTER, counter);
        outState.putInt(KeyString.PREVIOUS_STATE, previousState);
        outState.putInt(KeyString.TRAIN_STATE, trainState);
        outState.putBoolean(KeyString.IS_GOOD_TURN, isItGoodTurn);
    }

    private void startAccelerometer(){
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopAccelerometer(){
        mSensorManager.unregisterListener(this);
    }

    private void firstInit(){
        counter = 0;
        isItGoodTurn = true;
        Button trainButton = this.findViewById(R.id.train_button);
        trainButton.setText(R.string.start);
        updateTextView();
        previousState = StateMarker.START_STATE;
        trainState = StateMarker.TRAIN_STOP;
    }

    private void writeTrainResult(){
        SharedPreferences sharedPref = getSharedPreferences(KeyString.STATISTIC,MODE_PRIVATE);
        int trainNumber = sharedPref.getInt(KeyString.TRAINS_NUMBER, 0);
        trainNumber++;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KeyString.TRAINS_NUMBER, trainNumber);
        editor.putLong(KeyString.START_TIME+trainNumber, start);
        editor.putLong(KeyString.TRAIN+trainNumber, getTime());
        editor.putInt(KeyString.COUNTER+trainNumber, counter);
        editor.apply();
    }

    private long getTime(){
        return System.currentTimeMillis() - start;
    }

    private void askGoToStatistic(){
        FinishTrainDialog finishTrainDialog = new FinishTrainDialog();
        finishTrainDialog.show(getFragmentManager(), KeyString.DIALOG);
    }

    private void finishTrain(){
        writeTrainResult();
        stopAccelerometer();
        firstInit();
        askGoToStatistic();
    }

    private void updateTextView(){
        TextView textView = this.findViewById(R.id.counter_view);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(counter);
        stringBuilder.append("  :  ");
        stringBuilder.append(TURN_NUMBER);
        textView.setText(stringBuilder.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
    @Override
    public void onBackPressed() {}
}
