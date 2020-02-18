package com.example.rolldice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<Dice> dices;
    private RollAdapter arrayAdapter;
    private ArrayList<Roll> results;
    private ListView listView;

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
        txtResult.setText(Integer.toString(result));

        ArrayList<Dice> copyDices = new ArrayList<>(dices.size());

        for (Dice dice: dices) {
            copyDices.add(new Dice(dice.getValue()));
        }

        results.add(new Roll(result,copyDices));
        arrayAdapter.notifyDataSetChanged();
        listView.setSelection(arrayAdapter.getCount() -1);
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

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Roll roll = results.get(position);
            String text;
            if(roll.getScore() / roll.getDices().size() > 3){
                text = "That was Super Amazing Roll";
            }
            else
                text = "That was pretty lame Roll";

            Toast.makeText(this, text,
                    Toast.LENGTH_SHORT).show();
        });

        arrayAdapter = new RollAdapter(this,results);

        listView.setAdapter(arrayAdapter);

        addDice();
    }
}

class RollAdapter extends ArrayAdapter<Roll>{

    private ArrayList<Roll> rolls;

    public RollAdapter(@NonNull Context context, @NonNull ArrayList<Roll> rolls) {
        super(context, 0, rolls);
        this.rolls = rolls;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if(view == null)
        {
            //Obtain inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_view_record,null);
        }

        Roll roll = rolls.get(position);

        TextView txtScore = view.findViewById(R.id.txtScore);
        txtScore.setText("Score: " + roll.getScore());


        LinearLayout dicesLayout = view.findViewById(R.id.layDicesListItem);
        List<Dice> dices = roll.getDices();
        dicesLayout.removeAllViews();
        for (Dice dice :dices) {
            DiceView dw = new DiceView(getContext(),dice);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(5,5,5,5);
            dw.setLayoutParams(params);
            dicesLayout.addView(dw);
        }

        return view;
    }
}