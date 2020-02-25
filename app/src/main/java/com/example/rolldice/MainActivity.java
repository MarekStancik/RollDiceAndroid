package com.example.rolldice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    private final static int ACTIVITY_HISTORY_ID = 123;
    private Button btnIncrement,btnDecrement,/*btnRoll,*/btnHistory;
    private LinearLayout layDices;
    private TextView txtDiceCount,/*txtResult,*/txtBigResult;
    private ArrayList<Dice> dices;
    private ArrayList<Roll> results;
    private boolean isRolling = false;

    //https://stackoverflow.com/questions/2317428/how-to-refresh-app-upon-shaking-the-device
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 12 && !isRolling) {
                isRolling = true;
                txtBigResult.setText(getResources().getString(R.string.rolling));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isRolling = false;
                        rollDices();
                    }
                }, 2000);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private DiceView createDiceView(Dice dice)
    {
        DiceView dw = new DiceView(getWindow().getContext(),dice);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120,120);
        params.setMargins(5,5,5,5);
        dw.setLayoutParams(params);
        return dw;
    }

    private void rollDices()
    {
        int result = 0;
        for(Dice dice: dices){
            int newValue = new Random().nextInt(6) + 1;
            dice.setValue(newValue);
            result += newValue;
        }

        //Update Dices
        for (int i = 0; i < layDices.getChildCount(); ++i){
            View child = layDices.getChildAt(i);
            child.invalidate();
        }

        //Update Result
        //txtResult.setText(Integer.toString(result));
        txtBigResult.setText(Integer.toString(result));

        ArrayList<Dice> copyDices = new ArrayList<>(dices.size());

        copyDices.addAll(dices);

        results.add(new Roll(result,copyDices));
    }

    private void updateCountText()
    {
        txtDiceCount.setText(Integer.toString(dices.size()));
    }

    private void updateDices()
    {
        updateCountText();
        updateButtons();
    }

    private void updateButtons()
    {
        btnIncrement.setEnabled(dices.size() < 8);
        btnDecrement.setEnabled(dices.size() > 1);
    }

    private void addDice()
    {
        if(dices.size() < 8){
            Dice newDice = new Dice(1);
            dices.add(newDice);
            layDices.addView(createDiceView(newDice));
            updateDices();
        }
    }

    private void removeDice()
    {
        if(dices.size() > 1)
        {
            dices.remove(dices.size()-1);
            layDices.removeView(layDices.getChildAt(dices.size()));
            updateDices();
        }
    }

    private void openHistory()
    {
        Intent x = new Intent(this, HistoryListActivity.class);
        x.putExtra("rolls",this.results);
        startActivityForResult(x, ACTIVITY_HISTORY_ID);
    }

    private void setupButtons()
    {
        //Setting up references
        this.btnIncrement = findViewById(R.id.btnIncrementDice);
        this.btnDecrement = findViewById(R.id.bntDecrementDice);
      //  this.btnRoll = findViewById(R.id.btnRoll);
        this.btnHistory = findViewById(R.id.btnHistory);

        //Setting up actions
        btnIncrement.setOnClickListener((View v)->addDice());
        btnDecrement.setOnClickListener((View v)->removeDice());
   //     btnRoll.setOnClickListener((View v)->rollDices());
        btnHistory.setOnClickListener((View v)->openHistory());
    }

    private void setupLayouts() {
        layDices = findViewById(R.id.layDices);
    }

    private void setupTexts() {
        txtDiceCount = findViewById(R.id.txtDicesCount);
     //   txtResult = findViewById(R.id.txtResult);
        txtBigResult = findViewById(R.id.txtBigResult);
    }

    private void setupSensorManager()
    {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //View initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Member attributes initialization
        dices = new ArrayList<>();
        results = new ArrayList<>();
        setupSensorManager();

        //Setup references to the view elements
        setupLayouts();
        setupButtons();
        setupTexts();

        addDice();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTIVITY_HISTORY_ID)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    Bundle bundle = data.getExtras();
                    this.results = (ArrayList<Roll>) bundle.getSerializable("rolls");
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }

    //To register listener on resume
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Unregister listener on pause to save resources and not trigger in second activity
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}