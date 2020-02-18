package com.example.rolldice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

public class DiceView extends View {

    private static final float DotRadius = 10f;
    private static final int [] colors = { Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW,Color.CYAN};
    private Paint paint;
    private Dice dice;

    private void init()
    {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public DiceView(Context context, Dice dice) {
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
