package com.example.test;

import android.text.format.Time;

import java.util.List;

/**
 * Created by Omatic on 3/7/14.
 */
public class User {
    private Gender gender;
    private Double weight;
    private DrinkStats ds;
    private int id; // generate new id when needed
    double bac;


    public User() {
        ds = new DrinkStats();
    }

    public void addDrink(int id, Portion portion){
        switch (portion) {
            case FULL:
                ds.addDrink(id);
                break;
            case HALF:
                ds.addHalfDrink(id);
                break;
        }
    }

    public void calcSimpleBAC(){
        bac = 0;
        // %BAC = (A x 5.14 / W x r) - .015 x H
        // A = ounces of alcohol ingested
        // W = weight of person
        // r = gender constant
        double standardDrinksTotal = ds.getTotalStandardDrinks();
        Time timeNow = new Time();
        timeNow.setToNow();

        double millisAgoDrinkingStarted = timeNow.toMillis(false) - ds.getEarliestDrinkTime().toMillis(false);


        bac = (standardDrinksTotal * .6) * 5.14; // 1 standard drink = .6 fl oz;
        bac /= ((/*(weightUnits == "kgs") ? weight * 2.20462 : */weight) * gender.getConstant()); //TODO: Setting for changing weight units
        bac -= (.015 * (millisAgoDrinkingStarted / (1000 * 60 * 60))); // .015 * hours ago started drinking

        if (bac < 0) bac = 0;
    }


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender g) {
        this.gender = g;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List getDrinks(){
        return ds.getDrinks();
    }


}

enum Gender {
    MALE, FEMALE;

    public double getConstant(){
        switch (this) { // TODO: Get correct values
            case MALE: return 1;
            case FEMALE: return .8;
        }

        return -1;
    }

}
