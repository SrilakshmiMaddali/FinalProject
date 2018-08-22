package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.Observer;

import com.udacity.gradle.builditbigger.model.JokeData;


public interface MainViewDataInterface {

    void addObserver(Observer<JokeData> observer);

    void removeObserver(Observer<JokeData> observer);

    void getNextJoke();
}
