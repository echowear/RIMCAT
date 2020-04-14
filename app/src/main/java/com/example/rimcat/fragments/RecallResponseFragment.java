package com.example.rimcat.fragments;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;

public class RecallResponseFragment extends QuestionFragment {
    private static final String     TAG = "RecallResponseFragment";
    private Context                 mContext;
    private Vibrator                mVibrator;
    private EditText                responseText;
    private Button                  addBtn, doneRecallingBtn;
    private ArrayList<String>       responses;
    private boolean                 firstWordRecalled, firstFinish;
    private CountDownTimer          doneRecallingTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recall_response, container, false);
        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize views
        responseText = view.findViewById(R.id.vresponse_recall_word);
        addBtn = view.findViewById(R.id.vresponse_addBtn);
        doneRecallingBtn = view.findViewById(R.id.done_recalling_btn);
        responses = new ArrayList<String>();

        // Set add button on click
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!responseText.getText().toString().equals("")) {
                    responses.add(responseText.getText().toString());
                    responseText.setText("");
                    vibrateAndExecuteSound();
                    if (!firstWordRecalled || doneRecallingBtn.getVisibility() == View.INVISIBLE) {
                        firstWordRecalled = true;
                        doneRecallingTimer.start();
                    } else {
                        doneRecallingTimer.cancel();
                        doneRecallingTimer.start();
                    }
                }
            }
        });

        doneRecallingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doneRecallingBtn.getVisibility() == View.VISIBLE) {
                    if (!firstFinish) {
                        ((MainActivity)getActivity()).showRetryDialog();
                    } else {
                        ((MainActivity)getActivity()).showRecallFinishDialog();
                    }
                }
            }
        });

        doneRecallingBtn.setVisibility(View.INVISIBLE);
        // Creates the timer that counts down the verbal recall section
        doneRecallingTimer = new CountDownTimer(15000, 15000) {
            int count = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Timer ticked" + ++count);
            }

            @Override
            public void onFinish() {
                doneRecallingBtn.setVisibility(View.VISIBLE);
            }
        };

        cardView = view.findViewById(R.id.vresponse_layout);
        startAnimation(true);
        logStartTime();

        return view;
    }

    public void executePostMessageSetup() {
        firstFinish = true;
        doneRecallingBtn.setVisibility(View.INVISIBLE);
        doneRecallingTimer.start();
    }

    private void vibrateAndExecuteSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mVibrator != null) {
            mVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else if (mVibrator != null) {
            //deprecated in API 26
            mVibrator.vibrate(500);
        }
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean loadDataModel() {
        int currentView = ((MainActivity)getActivity()).getViewNumber();
        for (int i = 0; i < responses.size(); i++) {
            if (currentView == DataLogModel.RECALL_RESPONSE_SCREEN_1)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_1," + responses.get(i));
            else if (currentView == DataLogModel.RECALL_RESPONSE_SCREEN_2)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_2," + responses.get(i));
        }

        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
