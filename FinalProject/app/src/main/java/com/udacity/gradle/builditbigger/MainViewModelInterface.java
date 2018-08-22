package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;

import com.udacity.gradle.builditbigger.model.JokeData;

public interface MainViewModelInterface {

    void getNextJoke();

    int getJokeStatus();

    void addMainViewOwner(LifecycleOwner owner, Observer<JokeData> observer);

    void removeMainViewOwner(Observer<JokeData> observer);
}
