package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

// http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRetainInstance(true);


        // How Drunk
            // Check with user if user details are correct.
            // When was your first drink? (x hours ago)
            // What was it? (show popular choices, allow for custom entry)
            // Loop drink questions until user has indicated that's all they had.
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}


