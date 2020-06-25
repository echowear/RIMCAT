package com.example.rimcat.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DigitSpanFragment extends QuestionFragment {

    private static final String TAG = "DigitSpanFragment";
    private static final int[][] FORWARD_NUMBER_LIST = {
            { 5, 2, 3, 8 },
            { 9, 7, 1, 3 },
            { 1, 5, 7, 3, 9 },
            { 6, 2, 8, 4, 7 },
            { 3, 7, 1, 2, 0, 5 },
            { 4, 8, 1, 3, 9, 2 },
            { 8, 4, 3, 1, 7, 9, 2 },
            { 2, 8, 5, 1, 0, 6, 4 }
    };
    private static final int[][] BACKWARD_NUMBER_LIST = {
            { 5, 9 },
            { 3, 5 },
            { 1, 7, 9 },
            { 4, 2, 5 },
            { 2, 9, 1, 8 },
            { 6, 0, 8, 2 },
            { 9, 1, 4, 3, 6 },
            { 1, 5, 8, 6, 2 }
    };
    private static final String[] COUNTDOWN_TEXT = { "Ready", "Set", "Go!" };
    private HashMap<Integer, String> numberToTextMap;
    private int[] currentNumberList;
    private int timerIndex = 0, currentWord = 0;
    private TextToSpeech textToSpeech;
    private CountDownTimer countDownTimer, trialListCounter;
    private CardView countdownCard, numRecallCard;
    private Button readyBtn, submitNumberBtn, nextBtn;
    private FloatingActionButton audioBtn;
    private TextView dsText;
    private EditText dsEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digit_span, container, false);

        numberToTextMap = new HashMap<>();
        numberToTextMap.put(0, "Zero");
        numberToTextMap.put(1, "One");
        numberToTextMap.put(2, "Two");
        numberToTextMap.put(3, "Three");
        numberToTextMap.put(4, "Four");
        numberToTextMap.put(5, "Five");
        numberToTextMap.put(6, "Six");
        numberToTextMap.put(7, "Seven");
        numberToTextMap.put(8, "Eight");
        numberToTextMap.put(9, "Nine");

        countdownCard = view.findViewById(R.id.countdown_card);
        dsText = view.findViewById(R.id.ds_number_text);
        readyBtn = view.findViewById(R.id.ds_ready_btn);
        currentNumberList = FORWARD_NUMBER_LIST[currentWord];

        numRecallCard = view.findViewById(R.id.number_recall_card);
        dsEditText = view.findViewById(R.id.ds_edit_txt);
        audioBtn = view.findViewById(R.id.ds_audio_btn);
        submitNumberBtn = view.findViewById(R.id.ds_add_btn);
        nextBtn = view.findViewById(R.id.ds_next_btn);

        countdownCard.setVisibility(View.VISIBLE);
        numRecallCard.setVisibility(View.INVISIBLE);

        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyBtn.setVisibility(View.INVISIBLE);
                dsText.setText("");
                dsText.setTextSize(55);
                countDownTimer.start();
            }
        });

        countDownTimer = new CountDownTimer(3000, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
                dsText.setText(COUNTDOWN_TEXT[timerIndex]);
                timerIndex++;
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                trialListCounter.start();
            }
        };

        trialListCounter = new CountDownTimer(currentNumberList.length * 2000, 1999) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (timerIndex < currentNumberList.length) {
                    Log.d(TAG, "onTick: Changing text --- " + currentNumberList[timerIndex]);
                    dsText.setText("" + currentNumberList[timerIndex]);
                    textToSpeech.speak(numberToTextMap.get(currentNumberList[timerIndex]), TextToSpeech.QUEUE_FLUSH, null);
                    timerIndex++;
                }
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                // TODO: Change screens to the answer screen
                countdownCard.setVisibility(View.INVISIBLE);
                readyBtn.setVisibility(View.INVISIBLE);
                dsText.setText(getResources().getString(R.string.verbal_readyPrompt));
                dsText.setTextSize(35);
                numRecallCard.setVisibility(View.VISIBLE);
            }
        };

        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        cardView = view.findViewById(R.id.ds_page);
        startAnimation(true);
        logStartTime();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
