package com.example.rimcat.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class VerbalRecallFragment extends QuestionFragment {
    private static final String TAG = "VerbalRecallFragment";
    private static final String[] TRIAL_LIST_ONE = new String[] {
            "Drum", "Curtain", "Bell", "Coffee", "School",
            "Parent", "Moon", "Garden", "Hat", "Farmer",
            "Nose", "Turkey"
    };
    private static final String[] TRIAL_LIST_TWO = new String[] {
            "Desk", "Ranger", "Bird", "Shoe", "Mountain",
            "Glasses", "Towel", "Cloud", "Boat", "Lamb", "Gum"
    };

    private TextView verbalText;
    private Button readyBtn;
    private CountDownTimer countDownTimer, trialListCounter;
    private int timerIndex = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verbal_recall, container, false);

        cardView = view.findViewById(R.id.card);
        verbalText = view.findViewById(R.id.verbalText);
        readyBtn = view.findViewById(R.id.figure_readyBtn);
        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyBtn.setVisibility(View.INVISIBLE);
                countDownTimer.start();
            }
        });

        // Creates the timer that counts down the verbal recall section
        countDownTimer = new CountDownTimer(3000, 980) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
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
        trialListCounter = new CountDownTimer(TRIAL_LIST_ONE.length * 2000, 1980) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (timerIndex < TRIAL_LIST_ONE.length) {
                    Log.d(TAG, "onTick: Changing text --- " + TRIAL_LIST_ONE[timerIndex]);
                    verbalText.setText("" + TRIAL_LIST_ONE[timerIndex]);
                    timerIndex++;
                }
            }

            @Override
            public void onFinish() {
                ((MainActivity)getActivity()).getFragmentData(null);
            }
        };

        startAnimation(true);

        return view;
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new RecallResponseFragment(), "RecallResponseFragment");
    }
}
