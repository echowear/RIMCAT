package com.example.rimcat.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class VerbalLearningFragment extends QuestionFragment {
    private static final String TAG = "VerbalRecallFragment";
    private static final String[] TRIAL_LIST_ONE = new String[] {
            "Drum", "Curtain", "Bell", "Coffee", "School",
            "Parent", "Moon", "Garden", "Hat", "Farmer",
            "Nose", "Turkey"
    };
    private static final String[] TRIAL_LIST_TWO = new String[] {
            "Desk", "Ranger", "Bird", "Shoe", "Mountain", "Stove",
            "Glasses", "Towel", "Cloud", "Boat", "Lamb", "Gum"
    };

    private TextView verbalText;
    private Button readyBtn;
    private TextToSpeech textToSpeech;
    private CountDownTimer countDownTimer, trialListCounter;
    private int timerIndex;
    private String[] currentWordList;
    private boolean isTTSInitialized, countdownStarted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verbal_learning, container, false);

        // Set the current word list to the correct trial list words
        int currentView = ((MainActivity)getActivity()).getViewNumber();
        if (currentView == DataLogModel.VERBAL_LEARNING_SCREEN_4)
            currentWordList = TRIAL_LIST_TWO;
        else
            currentWordList = TRIAL_LIST_ONE;

        // Initialize and prepare views
        cardView = view.findViewById(R.id.card);
        verbalText = view.findViewById(R.id.verbalText);
        readyBtn = view.findViewById(R.id.figure_readyBtn);
        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginCountdownTimer();
            }
        });

        // Creates the timer that counts down the verbal recall section
        countDownTimer = new CountDownTimer(3000, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
                if (timerIndex > 0)
                    verbalText.setText("" + timerIndex);
                timerIndex--;
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                trialListCounter.start();
            }
        };

        // Creates the timer that handles the word changing event during the verbal recall section
        trialListCounter = new CountDownTimer(currentWordList.length * 2000, 1999) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (timerIndex < currentWordList.length) {
                    Log.d(TAG, "onTick: Changing text --- " + currentWordList[timerIndex]);
                    verbalText.setText(currentWordList[timerIndex]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isTTSInitialized) {
                        textToSpeech.speak(currentWordList[timerIndex], TextToSpeech.QUEUE_FLUSH, null, null);
                    } else if (isTTSInitialized) {
                        textToSpeech.speak(currentWordList[timerIndex], TextToSpeech.QUEUE_FLUSH, null);
                    }
                    timerIndex++;
                }
            }

            @Override
            public void onFinish() {
                ((MainActivity)getActivity()).getFragmentData(null);
            }
        };

        startAnimation(true);
        nextButtonReady();
        return view;
    }

    private void beginCountdownTimer() {
        timerIndex = 3;
        countdownStarted = true;
        readyBtn.setVisibility(View.INVISIBLE);
        verbalText.setText("");
        verbalText.setTextSize(55);
        countDownTimer.start();
    }

    private void setUpTextToSpeech() {
        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    isTTSInitialized = true;
                } else {
                    Log.e(TAG, "TTS Initialization Failed!");
                    isTTSInitialized = false;
                }
            }
        });
    }

    private void stopActivity() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (countDownTimer != null)
            trialListCounter.cancel();
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void onStart() {
        setUpTextToSpeech();
        super.onStart();
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new VerbalRecallFragment(), "RecallResponseFragment");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: called");
        if (countdownStarted) {
            setUpTextToSpeech();
            beginCountdownTimer();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: called.");
        stopActivity();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: called.");
        stopActivity();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        stopActivity();
        super.onDestroy();
    }
}
