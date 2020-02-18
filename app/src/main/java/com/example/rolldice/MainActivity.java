package com.example.rolldice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private Button btnIncrement,btnDecrement,btnRoll;
    private LinearLayout layDices;
    private TextView txtDiceCount,txtResult;
    private ListView listView;
    private List<Dice> dices;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> results;

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
        txtResult.setText("RESULT: " + result);
        results.add(Integer.toString(result));
        arrayAdapter.notifyDataSetChanged();
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

    private void setupButtons()
    {
        //Setting up references
        this.btnIncrement = findViewById(R.id.btnIncrementDice);
        this.btnDecrement = findViewById(R.id.bntDecrementDice);
        this.btnRoll = findViewById(R.id.btnRoll);

        //Setting up actions
        btnIncrement.setOnClickListener((View v)->addDice());
        btnDecrement.setOnClickListener((View v)->removeDice());
        btnRoll.setOnClickListener((View v)->rollDices());
    }

    private void setupLayouts() {
        layDices = findViewById(R.id.layDices);
    }

    private void setupTexts() {
        txtDiceCount = findViewById(R.id.txtDicesCount);
        txtResult = findViewById(R.id.txtResult);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //View initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Member attributes initialization
        dices = new ArrayList<>();
        results = new ArrayList<>();

        //Setup references to the view elements
        setupLayouts();
        setupButtons();
        setupTexts();

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                results);

        listView.setAdapter(arrayAdapter);

        addDice();
    }
}