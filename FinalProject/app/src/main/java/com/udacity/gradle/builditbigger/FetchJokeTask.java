package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.model.JokeBean;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.gradle.builditbigger.model.JokeData;

import java.io.IOException;

public class FetchJokeTask extends AsyncTask<Void, Void, JokeData> {
    private static MyApi myApiService = null;
    private int statusCode;
    private final MutableLiveData<JokeData> mJokeData = new MutableLiveData<>();

    @Override
    protected JokeData doInBackground(Void... voids){
        if (myApiService == null) {
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
        .setRootUrl("http://" + "0.0.0.0:8080" + "/_ah/api/")
        .setGoogleClientRequestInitializer(abstractGoogleClientRequest -> abstractGoogleClientRequest.setDisableGZipContent(true));
        myApiService = builder.build();
        }

        try {
        JokeBean bean = myApiService.getMyJoke().execute();
        statusCode = myApiService.getMyJoke().getLastStatusCode();
        return new JokeData(bean.getJokeBody(),
        bean.getJokeFollowUp(),
        bean.getJokeStatus());
        } catch (IOException e) {
        e.printStackTrace();
        return new JokeData(null, null, JokeData.STATUS_ERROR);
        }
    }

    @Override
    protected void onPostExecute(JokeData jokeData) {
        Log.v(getClass().getSimpleName(), "Joke data received, status code = " + jokeData.statusCode);
        mJokeData.setValue(jokeData);
        if (IdlingResourceSingleton.isActive())
        IdlingResourceSingleton.getInstance().decrementCounter();
    }
}
