package com.example.rolldice;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Dice implements Serializable {
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