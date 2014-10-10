package com.example.test;

/**
 * Created by Omatic on 3/4/14.
 */
import android.test.ActivityTestCase;

import junit.framework.Assert;


public class ActivityTest extends ActivityTestCase {
    User u;


    public void testHappy(){
        Assert.assertTrue(true);
    } // if you're happy and you know it, pass this test.

    public void setUp(){
        u = new User();
        u.setWeight(250.00);
        u.setGender(Gender.MALE);
    }

    public void testAddDrinks() {
        for (int i = 0; i < 5; i++) {
            addFullDrinkTest(i);
            addHalfDrinkTest(i);
        }

        assertEquals(10, u.getDrinks().size());
    }

    private void addHalfDrinkTest (int i) {
        int beforeCount = u.getDrinks().size();

        u.addDrink(i,Portion.HALF);

        assertEquals(beforeCount + 1, u.getDrinks().size());

    }

    private void addFullDrinkTest (int i) {
        int beforeCount = u.getDrinks().size();

        u.addDrink(i,Portion.FULL);

        assertEquals(beforeCount + 1, u.getDrinks().size());
    }


}