package com.example.rimcat.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;
import com.example.rimcat.data_log.CorrectAnswerDictionary;

import java.util.HashMap;
import java.util.Locale;

public class ComputationFragment extends QuestionFragment {
    private static final String TAG = "ComputationFragment";
    private static final int MILLIS_TO_SHOW_COMPUTATION = 3000;
    private static final String[] COMPUTATION_LIST = {
            "6 + 11", "37 + 6", "41 - 5", "34 - 8", "9 x 4", "8 x 6", "32 ÷ 8", "68 ÷ 2"
    };
    private HashMap<String, String> numberToTextMap;
    private CountDownTimer showComputationTimer;
    private TextView computationText;
    private EditText compEditText;
    private Button nextBtn, repeatBtn;
    private int currentCompNum = 0;
    private String currentCompText;
    private boolean movingToNextActivity = false;
    private boolean hasRepeated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_computation, container, false);
        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        compEditText = view.findViewById(R.id.comp_answer);
        nextBtn = view.findViewById(R.id.comp_next_btn);
        repeatBtn = view.findViewById(R.id.comp_repeat_btn);
        computationText = view.findViewById(R.id.comp_text);

        numberToTextMap = new HashMap<>();
        numberToTextMap.put("-", "Minus");
        numberToTextMap.put("x", "Times");
        numberToTextMap.put("÷", "Divided by");

        compEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (compEditText.getText().toString().equals("")) {
                    nextBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
                } else {
                    nextBtn.getBackground().setTint(getResources().getColor(R.color.colorAccent));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        nextBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextComputation();
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatComputationRead();
            }
        });

        showComputationTimer = new CountDownTimer(MILLIS_TO_SHOW_COMPUTATION, MILLIS_TO_SHOW_COMPUTATION) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                computationText.setVisibility(View.INVISIBLE);
                repeatBtn.setVisibility(View.VISIBLE);
            }
        };

        CountDownTimer waitToReadFirstCompTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                readCurrentComputation();
            }
        };
        waitToReadFirstCompTimer.start();

        // Set properties for first screen
        currentCompText = COMPUTATION_LIST[currentCompNum];
        computationText.setText(currentCompText);
        computationText.setVisibility(View.VISIBLE);
        repeatBtn.setVisibility(View.INVISIBLE);

        cardView = view.findViewById(R.id.comp_card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void moveToNextComputation() {
        if (!movingToNextActivity && !compEditText.getText().toString().equals("")) {
            logEndTimeAndData(getActivity().getApplicationContext(), "computation_" + (currentCompNum + 1) + "," + compEditText.getText().toString());
            vibrateToastAndExecuteSound(compEditText.getText().toString(), false);
            currentCompNum++;
            if (currentCompNum < COMPUTATION_LIST.length) {
                hasRepeated = false;
                showComputationTimer.cancel();
                ((MainActivity)getActivity()).pauseTextToSpeech();
                compEditText.setText("");
                currentCompText = COMPUTATION_LIST[currentCompNum];
                computationText.setText(currentCompText);
                computationText.setVisibility(View.VISIBLE);
                repeatBtn.setVisibility(View.INVISIBLE);
                logStartTime();
                readCurrentComputation();
            } else {
                movingToNextActivity = true;
                ((MainActivity)getActivity()).getFragmentData(null);
            }
        }
    }

    private void readCurrentComputation() {
        // Replace mispronounced words, then read the sentence
        String parsedText = currentCompText.replace("-", "Minus").replace("x", "Times").replace("÷", "Divided by");
        ((MainActivity)getActivity()).useTextToSpeech(parsedText);

        // Grey out or keep repeat button color
        if (hasRepeated) {
            repeatBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
        } else {
            repeatBtn.getBackground().setTint(getResources().getColor(R.color.colorAccent));
        }

        // Set the views visibilities
        showComputationTimer.start();

    }

    private void repeatComputationRead() {
        if (!hasRepeated) {
            hasRepeated = true;
            repeatBtn.setVisibility(View.INVISIBLE);
            computationText.setVisibility(View.VISIBLE);
            readCurrentComputation();
        }
    }

    private void stopActivity() {
        if (showComputationTimer != null)
            showComputationTimer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return CorrectAnswerDictionary.COMPUTATION_ANSWERS.get(currentCompNum);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: called");
        stopActivity();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        computationText.setVisibility(View.INVISIBLE);
        repeatBtn.setVisibility(View.VISIBLE);
    }
}
