package com.example.rolldice;

import java.util.ArrayList;

public class Roll {

    private int score;
    private ArrayList<Dice> dices;

    public Roll(int score,ArrayList<Dice> dices){
        setScore(score);
        setDices(dices);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setDices(ArrayList<Dice> dices) {
        this.dices = dices;
    }

    public ArrayList<Dice> getDices() {
        return dices;
    }
}
