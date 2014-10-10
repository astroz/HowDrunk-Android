package com.example.test;

import android.text.format.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Omatic on 3/3/14.
 */
public class DrinkStats {
    private ArrayList<Drink> drinks;

    public DrinkStats (){
        drinks = new ArrayList<Drink>();
    }

    public void addDrink(int id){
        drinks.add(new Drink(id));
    }

    public void addHalfDrink(int id){
        drinks.add(new Drink(id,Portion.HALF));
    }

    public void addQuarterDrink(int id){
        drinks.add(new Drink(id,Portion.QUARTER));
    }

    // returns earliest drank drink, or null if the list is empty.
    public Time getEarliestDrinkTime() {
        if (drinks.size() == 0) return null;
        Time earliest = drinks.get(0).getTimeDrank();
        for (Drink d : drinks)
            if (d.getTimeDrank().before(earliest))
                earliest = d.getTimeDrank();

        return earliest;
    }


    public double getTotalDrinksOfID(int id){
        double total = 0;

        for (Drink d: drinks)
            if (d.getDrinkId() == id)
                total += d.getPortion().toDouble();

        return total;
    }

    public double getTotalStandardDrinks(){
        double total = 0;
        // tally up num drinks per type, then multiply each by num standard drinks in one serving.

        HashMap<Integer, Double> totals = new HashMap<Integer,Double>();
        int drinkID = 0;
        double portion = 0;

        for (Drink d: drinks) {
            drinkID = d.getDrinkId();

            if (totals.containsKey(drinkID)) // increment the key val
                totals.put(drinkID, totals.get(drinkID) + d.getPortion().toDouble());
            else // add a new key val
                totals.put(drinkID, d.getPortion().toDouble());
        }

        for (Integer key : totals.keySet())
            total += (totals.get(key) * DrinkUtil.getStandardDrinks(key)); // TODO: Re-evalutate how db access will be had. are we loading each time?

        return total;
    }


    public void reset() {
        drinks = new ArrayList<Drink>();
    }

    public List getDrinks(){
        return drinks;
    }

}
