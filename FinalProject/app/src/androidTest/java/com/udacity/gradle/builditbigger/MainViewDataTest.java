package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import com.udacity.gradle.builditbigger.model.JokeData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainViewDataTest {

    private final IdlingResource mIdlingResource = IdlingResourceSingleton.getInstance().getIdlingResource();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void testAsyncTask_fetchesData() {
        MutableLiveData<JokeData> jokeDataMutableLiveData = new MutableLiveData<>();
        Observer<JokeData> jokeDataObserver = jokeData -> jokeDataMutableLiveData.setValue(jokeData);
        MainViewData.getInstance().addObserver(jokeDataObserver);
        MainViewData.getInstance().getNextJoke();
        assert (jokeDataMutableLiveData.getValue() != null &&
                jokeDataMutableLiveData.getValue().statusCode == JokeData.STATUS_OK);
    }
}
