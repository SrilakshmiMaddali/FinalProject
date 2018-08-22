package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.udacity.gradle.builditbigger.model.JokeData;

public class MainViewModel extends AndroidViewModel implements MainViewModelInterface {

    MainViewData mMainViewData;
    private final MutableLiveData<JokeData> mJokeData;
    private final Observer<JokeData> mRepositoryJokeObserver;

    @Override
    public void getNextJoke() {
        mMainViewData.getNextJoke();
    }

    @Override
    public int getJokeStatus() {
        if (mJokeData.getValue() != null) {
            return mJokeData.getValue().statusCode;
        }
        else {
            return JokeData.STATUS_EMPTY;
        }
    }

    @Override
    public void addMainViewOwner(LifecycleOwner owner, Observer<JokeData> observer) {
        mJokeData.observe(owner, observer);
    }

    @Override
    public void removeMainViewOwner(Observer<JokeData> observer) {
        mJokeData.removeObserver(observer);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mMainViewData.removeObserver(mRepositoryJokeObserver);
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.mMainViewData = MainViewData.getInstance();
        mJokeData = new MutableLiveData<>();
        mRepositoryJokeObserver = mJokeData::setValue;
        mMainViewData.addObserver(mRepositoryJokeObserver);
    }
}
