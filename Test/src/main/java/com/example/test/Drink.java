package com.example.test;

import android.text.format.Time;
import android.util.Log;

/**
 * Created by Omatic on 3/5/14.
 */
public class Drink {
    // Loader should load these objects into memory
    // name, description and volume are all static and should be referenced from memory based on
    private int drinkID;
    private Portion portion = Portion.FULL; // full drink unless otherwise stipulated.
    private Time timeDrank;

    public Drink (int drinkID) {
        this.drinkID = drinkID;
    }

    public Drink(int drinkID, Portion p) {
        this.drinkID = drinkID;
        portion = p;
    }

    public Drink(int drinkID, Portion p, Time t) {
        this.drinkID = drinkID;
        portion = p;
        timeDrank = t;
    }

    public int getDrinkId() {
        return drinkID;
    }

    public void setDrinkId(int id) {
        this.drinkID = id;
    }

    public Portion getPortion() {
        return portion;
    }

    public Time getTimeDrank() {
        return timeDrank;
    }

}

enum Portion {
    FULL, HALF, QUARTER;

    public double toDouble() {
        switch (this) {
            case FULL: return 1;
            case HALF: return .5;
            case QUARTER: return .25;
        }

        Log.e("Drink", "Portion is not valid."); // if we've gotten this far we have a problem.
        return 0;
    }
}
