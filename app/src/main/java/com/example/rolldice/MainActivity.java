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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class Dice{
    private int value;

    public Dice(int value){
        setValue(value);
    }

    public void setValue(int value) {
        if(value > 0 && value < 7)
            this.value = value;
        else
            this.value = 1;
    }

    public int getValue() {
        return value;
    }
}

class DiceView extends View{

    private static final float DotRadius = 10f;
    private static final int [] colors = { Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW,Color.CYAN};
    private Paint paint;
    private Dice dice;

    private void init()
    {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public DiceView(Context context,Dice dice) {
        super(context);
        this.dice = dice;
        init();
    }

    private void drawOne(Canvas canvas)
    {
        float x = getWidth();
        float y = getHeight();
        canvas.drawCircle(x/2,y/2,DotRadius,paint);
    }

    private void drawTwo(Canvas canvas)
    {
        float x = getWidth();
        float y = getHeight();
        canvas.drawCircle(x/4,y/2,DotRadius,paint);
        canvas.drawCircle(x/4 * 3,y/2,DotRadius,paint);
    }

    private void drawThree(Canvas canvas)
    {
        float x = getWidth();
        float y = getHeight();
        float thirdX = x * 0.8f /3f;
        float thirdY = y * 0.8f /3f;
        canvas.drawCircle(thirdX,thirdY,DotRadius,paint);
        canvas.drawCircle(thirdX*2,thirdY*2,DotRadius,paint);
        canvas.drawCircle(thirdX*3,thirdY*3,DotRadius,paint);
    }

    private void drawFour(Canvas canvas)
    {
        float x = getWidth();
        float y = getHeight();
        float quarterX = x/4;
        float quarterY = y/4;
        canvas.drawCircle(quarterX,quarterY,DotRadius,paint);
        canvas.drawCircle(quarterX*3,quarterY,DotRadius,paint);
        canvas.drawCircle(quarterX,quarterY*3,DotRadius,paint);
        canvas.drawCircle(quarterX*3,quarterY*3,DotRadius,paint);
    }

    private void drawFive(Canvas canvas)
    {
        float x = getWidth();
        float y = getHeight();
        drawThree(canvas);
        float thirdX = x * 0.8f /3f;
        float thirdY = y * 0.8f /3f;
        canvas.drawCircle(thirdX *3,thirdY,DotRadius,paint);
        canvas.drawCircle(thirdX,thirdY*3,DotRadius,paint);
    }

    private void drawSix(Canvas canvas)
    {
        float x = getWidth();
        float y = getHeight();
        float quarterX = x/4;
        float sixthY = y/6;
        canvas.drawCircle(quarterX,sixthY,DotRadius,paint);
        canvas.drawCircle(quarterX*3,sixthY,DotRadius,paint);
        canvas.drawCircle(quarterX,sixthY*3,DotRadius,paint);
        canvas.drawCircle(quarterX*3,sixthY*3,DotRadius,paint);
        canvas.drawCircle(quarterX,sixthY*5,DotRadius,paint);
        canvas.drawCircle(quarterX*3,sixthY*5,DotRadius,paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(colors[new Random().nextInt(colors.length)]);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);


        switch (dice.getValue()) {
            case 1:
                drawOne(canvas);
                break;
            case 2:
                drawTwo(canvas);
                break;
            case 3:
                drawThree(canvas);
                break;
            case 4:
                drawFour(canvas);
                break;
            case 5:
                drawFive(canvas);
                break;
            case 6:
                drawSix(canvas);
                break;
        }
    }
}

public class MainActivity extends AppCompatActivity {

    private Button btnIncrement,btnDecrement,btnRoll;
    private LinearLayout layDices;
    private TextView txtDiceCount,txtResult;
    private List<Dice> dices;

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
        for(Dice dice: dices){
            dice.setValue(new Random().nextInt(6) + 1);
        }
        updateDices();
    }

    private void updateTexts()
    {
        txtDiceCount.setText("" + dices.size());

        int result = 0;
        for(Dice dice: dices){
            result += dice.getValue();
        }
        txtResult.setText("RESULT: " + result);
    }

    private void updateDices()
    {
        int childCount = layDices.getChildCount();
        Dice lastDice = dices.get(dices.size()-1);

        if(dices.size() > childCount){
            layDices.addView(createDiceView(lastDice));
        }
        else if(dices.size() < childCount){
            layDices.removeView(layDices.getChildAt( childCount- 1));
        }
        else{
            //Size is the same so roll occurred
            for (int i = 0; i < childCount; ++i){
                View child = layDices.getChildAt(i);
                child.invalidate();
            }
        }

        updateTexts();
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
            dices.add(new Dice(1));
            updateDices();
        }
    }

    private void removeDice()
    {
        if(dices.size() > 1)
        {
            dices.remove(dices.size()-1);
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

        //Setup references to the view elements
        setupLayouts();
        setupButtons();
        setupTexts();

        addDice();
    }
}