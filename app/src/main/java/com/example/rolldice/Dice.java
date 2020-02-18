package com.example.rolldice;

import androidx.annotation.NonNull;

public class Dice{
    private int value;

    public Dice(int value) {
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