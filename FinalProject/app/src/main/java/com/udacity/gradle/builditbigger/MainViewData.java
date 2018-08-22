package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.udacity.gradle.builditbigger.model.JokeData;

public class MainViewData implements MainViewDataInterface {

    private static MainViewData mInstance;
    private static final MutableLiveData<JokeData> mJokeData = new MutableLiveData<>();
    @Override
    public void addObserver(Observer<JokeData> observer) {
        mJokeData.observeForever(observer);
    }

    @Override
    public void removeObserver(Observer<JokeData> observer) {
        mJokeData.removeObserver(observer);
    }

    @Override
    public void getNextJoke() {
        new FetchJokeTask().execute();
        if (IdlingResourceSingleton.isActive())
            IdlingResourceSingleton.getInstance().incrementCounter();
    }

    private MainViewData() {

    }

    public static MainViewData getInstance() {
        if (mInstance == null) {
            mInstance = new MainViewData();
        }
        return mInstance;
    }
}
