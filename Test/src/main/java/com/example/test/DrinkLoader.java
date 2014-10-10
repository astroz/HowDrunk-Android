package com.example.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Loader;
import android.os.Build;

/**
 * Created by Omatic on 3/5/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DrinkLoader extends Loader {
    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public DrinkLoader(Context context) {
        super(context);
    }
}
