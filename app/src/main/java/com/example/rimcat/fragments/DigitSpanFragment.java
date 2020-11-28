package com.example.rimcat.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
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

public class DigitSpanFragment extends QuestionFragment {

    private static final String TAG = "DigitSpanFragment";
    private static final int[][] FULL_NUMBER_LIST = {
            { 5, 2, 3, 8 },
            { 9, 7, 1, 3 },
            { 1, 5, 7, 3, 9 },
            { 6, 2, 8, 4, 7 },
            { 3, 7, 1, 2, 0, 5 },
            { 4, 8, 1, 3, 9, 2 },
            { 8, 4, 3, 1, 7, 9, 2 },
            { 2, 8, 5, 1, 0, 6, 4 },
            { 9, 5 },
            { 5, 3 },
            { 9, 7, 1 },
            { 5, 2, 4 },
            { 8, 1, 9, 2 },
            { 2, 8, 0, 6 },
            { 6, 3, 4, 1, 9 },
            { 2, 6, 8, 5, 1 }
    };
    private static final int NUMS_PER_LIST = 8;
    private static final String[] COUNTDOWN_TEXT = { "Ready", "Set", "Go!" };
    private HashMap<Integer, String> numberToTextMap;
    private int[] currentNumberList;
    private int timerIndex = 0, currentDigitPlace, currentNumber = 0;
    private boolean movingToNextActivity = false, inNumberRead = false, inCountdownTimer = false;
    private CountDownTimer countDownTimer, trialListCounter;
    private CardView countdownCard, numRecallCard;
    private Button readyBtn, nextBtn;
    private TextView dsNumText, dsRecallText;
    private EditText dsEditText;
    private MediaPlayer storyMedia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_digit_span, container, false);

        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        // Creates hashmap for text to speech module
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

        // Initialize countdown card views
        countdownCard = view.findViewById(R.id.countdown_card);
        dsNumText = view.findViewById(R.id.ds_number_text);
        readyBtn = view.findViewById(R.id.ds_ready_btn);
        currentNumberList = FULL_NUMBER_LIST[currentNumber];

        // Initialize number recall card views
        numRecallCard = view.findViewById(R.id.number_recall_card);
        dsEditText = view.findViewById(R.id.ds_edit_txt);
        nextBtn = view.findViewById(R.id.ds_next_btn);
        dsRecallText = view.findViewById(R.id.ds_recall_text);

        countdownCard.setVisibility(View.VISIBLE);
        numRecallCard.setVisibility(View.INVISIBLE);

        // Starts countdown timer and prepares views to show countdown
        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginCountdown();
            }
        });

        // Sets up countdown timer. Starts trial list counter on finish
        countDownTimer = new CountDownTimer(3000, 999) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
                dsNumText.setText(COUNTDOWN_TEXT[timerIndex]);
                timerIndex++;
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                trialListCounter.start();
                inNumberRead = true;
                inCountdownTimer = false;
            }
        };

        // Sets up trial list counter. Reads numbers, changes them on screen, then moves to next card
        trialListCounter = new CountDownTimer(currentNumberList.length * 2000, 1999) {
            @Override
            public void onTick(long millisUntilFinished) {
                trialListOnTick();
            }

            @Override
            public void onFinish() {
                trialListOnFinish();
            }
        };

        // Sets up text to speech to read numbers
        setUpTextToSpeech();

        nextBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movingToNextActivity) {
                    if (!dsEditText.getText().toString().equals("")) {
                        logEndTimeAndData(getActivity().getApplicationContext(), "digit_span_" + (currentNumber + 1) + "," + dsEditText.getText().toString());
                        vibrateToastAndExecuteSound(dsEditText.getText().toString(), true);
                        ((MainActivity)getActivity()).hideSoftKeyboard();
                        moveToNextNumber();
                    }
                }
            }
        });

        dsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (dsEditText.getText().toString().equals("")) {
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

        changeCardText();
        cardView = view.findViewById(R.id.ds_page);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void beginCountdown() {
        readyBtn.setVisibility(View.INVISIBLE);
        dsNumText.setText("");
        dsNumText.setGravity(Gravity.CENTER);
        dsNumText.setTextSize(55);
        countDownTimer.start();
        inCountdownTimer = true;
    }

    private void trialListOnTick() {
        if (currentDigitPlace < currentNumberList.length) {
            Log.d(TAG, "onTick: Changing text --- " + currentNumberList[timerIndex]);
            dsNumText.setText("" + currentNumberList[currentDigitPlace]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isTTSInitialized) {
                textToSpeech.speak(numberToTextMap.get(currentNumberList[currentDigitPlace]), TextToSpeech.QUEUE_FLUSH, null, null);
            } else if (isTTSInitialized) {
                textToSpeech.speak(numberToTextMap.get(currentNumberList[currentDigitPlace]), TextToSpeech.QUEUE_FLUSH, null);
            }
            currentDigitPlace++;
        } else {
            trialListCounter.cancel();
            trialListOnFinish();
        }
    }

    private void trialListOnFinish() {
        inNumberRead = false;
        currentDigitPlace = 0;
        countdownCard.setVisibility(View.INVISIBLE);
        numRecallCard.setVisibility(View.VISIBLE);
        logStartTime();
    }

    private void moveToNextNumber() {
        currentNumber++;
        if (currentNumber < FULL_NUMBER_LIST.length) {
            numRecallCard.setVisibility(View.INVISIBLE);
            dsEditText.setText("");
            currentNumberList = FULL_NUMBER_LIST[currentNumber];
            trialListCounter = new CountDownTimer(currentNumberList.length * 2000, 1999) {
                @Override
                public void onTick(long millisUntilFinished) {
                    trialListOnTick();
                }

                @Override
                public void onFinish() {
                    trialListOnFinish();
                }
            };
            dsNumText.setTextSize(35);
            readyBtn.setVisibility(View.VISIBLE);
            changeCardText();
            countdownCard.setVisibility(View.VISIBLE);

        } else {
            movingToNextActivity = true;
            ((MainActivity)getActivity()).getFragmentData(null);
        }
    }

    private void changeCardText() {
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        String recallText = "";
        int highlightTextLength = 0;
        if (currentNumber < NUMS_PER_LIST) {
            recallText = getResources().getString(R.string.ds_inorder_text);
            highlightTextLength = "same order.".length();
        } else {
            recallText = getResources().getString(R.string.ds_reverse_text);
            highlightTextLength = "reverse order.".length();
        }
        SpannableString recallTextSS = new SpannableString(recallText);
        recallTextSS.setSpan(fcs, recallText.length() - highlightTextLength, recallText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        dsNumText.setText(getResources().getString(R.string.verbal_readyPrompt));
        dsRecallText.setText(recallTextSS);

        if (currentNumber == NUMS_PER_LIST) {
            String numText = getResources().getString(R.string.instructions2_digit_span);
            highlightTextLength = "backwards.".length();
            SpannableString numTextSS = new SpannableString(numText);
            numTextSS.setSpan(fcs, numText.length() - highlightTextLength, numText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dsNumText.setText(numTextSS);
            dsNumText.setGravity(Gravity.LEFT);

            readyBtn.setVisibility(View.INVISIBLE);
            storyMedia = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.reverse);
            storyMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    readyBtn.setVisibility(View.VISIBLE);
                    stopStoryMedia();
                }
            });
            storyMedia.start();
        }
    }

    private void stopActivity() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            isTTSInitialized = false;
        }
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (countDownTimer != null)
            trialListCounter.cancel();
    }

    private void stopStoryMedia() {
        if (storyMedia != null) {
            if (storyMedia.isPlaying() || storyMedia.isLooping()) {
                storyMedia.stop();
            }
            storyMedia.reset();
            storyMedia.release();
            storyMedia = null;
        }
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return CorrectAnswerDictionary.DIGIT_SPAN_ANSWERS.get(currentNumber);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public void onStart() {
        setUpTextToSpeech();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (inCountdownTimer || inNumberRead) {
            beginCountdown();
        }
        if (storyMedia != null) {
            storyMedia.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timerIndex = 0;
        if (inCountdownTimer) {
            countDownTimer.cancel();
        }
        if (inNumberRead) {
            trialListCounter.cancel();
        }
        if (storyMedia != null && storyMedia.isPlaying()) {
            storyMedia.pause();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        stopActivity();
        stopStoryMedia();
        super.onDestroy();
    }
}
