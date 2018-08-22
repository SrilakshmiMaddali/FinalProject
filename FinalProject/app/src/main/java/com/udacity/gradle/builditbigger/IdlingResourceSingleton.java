package com.udacity.gradle.builditbigger;

import android.support.test.espresso.idling.CountingIdlingResource;

public class IdlingResourceSingleton {

    private static IdlingResourceSingleton mInstance;
    private final CountingIdlingResource mCountingIdlingResource;

    private IdlingResourceSingleton() {
        mCountingIdlingResource = new CountingIdlingResource("IdlingResourceSingleton");
    }

    public static IdlingResourceSingleton getInstance() {
        if (mInstance == null) mInstance = new IdlingResourceSingleton();
        return mInstance;
    }

    public static boolean isActive() {
        return mInstance == null;
    }

    public void incrementCounter() {
        mCountingIdlingResource.increment();
    }

    public void decrementCounter() {
        mCountingIdlingResource.decrement();
    }

    public CountingIdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
