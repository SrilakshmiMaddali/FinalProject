package com.udacity.gradle.builditbigger;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gradle.builditbigger.model.JokeData;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.com.jokerdisplay.JokeDisplay;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int DISPLAY_REQUEST_CODE = 1001;
    @BindView(R.id.main_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.button_tell_joke)
    Button mButtonTellJoke;
    @BindView(R.id.tv_statusmessage)
    TextView mTVStatusMessage;

    private MainViewModel mViewModel;
    private Observer<JokeData> mJokeObserver;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //Initialize the ViewModel
        if (getActivity() != null) {
            if (mViewModel == null)
                mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        } else
            displayErrorSnackbar(getString(R.string.somethingWrong));

        //Set the click listener to tell the ViewModel to retrieve a new joke.
        mButtonTellJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorMessageVisible(false);
                setProgressBarVisible(true);

                if (mViewModel.getJokeStatus() == JokeData.STATUS_ERROR) mViewModel.getNextJoke();

                //Clear observer
                if (mJokeObserver != null) {
                    mViewModel.removeMainViewOwner(mJokeObserver);
                    mJokeObserver = null;
                }

                //Ensure that network is available.
                try {
                    if (getContext() == null) throw new NullPointerException();
                    boolean networkAvailable = utils.isNetworkAvailable(getContext());
                    if (!networkAvailable) {
                        displayErrorSnackbar(getString(R.string.networkerror));
                        return;
                    }
                } catch (NullPointerException e) {
                    displayErrorSnackbar(getString(R.string.somethingWrong));
                    return;
                }

                //Call tellJoke when the current joke changes.
               // mJokeObserver = this::tellJoke;

                //Add observer to the view model.
                mViewModel.addMainViewOwner(getActivity(), mJokeObserver);

                if (IdlingResourceSingleton.isActive())
                    IdlingResourceSingleton.getInstance().incrementCounter();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setProgressBarVisible(false);
        setErrorMessageVisible(false);

        //Initialize joke.
        if (getContext() != null) {
            if (utils.isNetworkAvailable(getContext())) {
                mViewModel.getNextJoke();
            }
        } else displayErrorSnackbar(getString(R.string.somethingWrong));

        if (IdlingResourceSingleton.isActive())
            IdlingResourceSingleton.getInstance().decrementCounter();
    }


    @Override
    public void onPause() {
        super.onPause();

        //Clear the observer.
        mViewModel.removeMainViewOwner(mJokeObserver);
        mJokeObserver = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Make sure the Activity successfully received a joke.
        if (requestCode == DISPLAY_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED)
            displayErrorSnackbar(getString(R.string.error_displaying_joke));
    }

    /**
     * Determines whether to tell joke or not and then displays it.
     *
     * @param jokeData The data for the joke.
     */
    public void tellJoke(JokeData jokeData) {
        if (IdlingResourceSingleton.isActive())
            IdlingResourceSingleton.getInstance().decrementCounter();

        //Check if data is valid.
        if (jokeData == null || jokeData.statusCode == JokeData.STATUS_EMPTY) return;
        if (jokeData.statusCode == JokeData.STATUS_ERROR) {
            handleJokeError();
            return;
        }
        //Build the message to pass to the Activity.
        StringBuilder sb = new StringBuilder(jokeData.jokeBody);
        if (jokeData.optFollowup != null && jokeData.optFollowup.length() > 0)
            sb.append("\n\n").append(jokeData.optFollowup);

        //Create and launch the intent.
        Intent displayActivity = new Intent(getContext(), JokeDisplay.class);
        displayActivity.putExtra(MainActivity.JOKE_KEY, sb.toString());

        startActivityForResult(displayActivity, DISPLAY_REQUEST_CODE);
    }


    /**
     * Sets the error message visbility.
     *
     * @param visible whether should be visible or not.
     */
    public void setErrorMessageVisible(boolean visible) {
        if (visible) mTVStatusMessage.setVisibility(View.VISIBLE);
        else mTVStatusMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets the progress bar visbility.
     *
     * @param visible whether should be visible or not.
     */
    public void setProgressBarVisible(boolean visible) {
        if (visible) mProgressBar.setVisibility(View.VISIBLE);
        else mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Handle if an error joke was received and determine what actions to take.
     */
    public void handleJokeError() {
        try {
            if (getContext() == null) throw new NullPointerException();
            boolean networkAvailable = utils.isNetworkAvailable(getContext());
            if (!networkAvailable) {
                displayErrorSnackbar(getString(R.string.networkerror));
                return;
            }
        } catch (NullPointerException e) {
            displayErrorSnackbar(getString(R.string.somethingWrong));
            return;
        }
        displayErrorSnackbar(getString(R.string.error_retrieving_joke));
    }

    /**
     * Display a snackbar with an error message.
     *
     * @param message The message to display.
     */
    private void displayErrorSnackbar(String message) {
        setProgressBarVisible(false);
        setErrorMessageVisible(true);
       // utils.summonSnackbarSelfClosing(getView(), message);
    }

}
