package com.example.rimcat.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ComputationFragment extends QuestionFragment {
    private static final String TAG = "ComputationFragment";
    private static final int MILLIS_TO_SHOW_COMPUTATION = 3000;
    private static final String[] COMPUTATION_LIST = {
            "6 + 11", "37 + 6", "41 - 5", "34 - 8", "9 x 4", "8 x 6", "32 ÷ 8", "68 ÷ 2"
    };
    private HashMap<String, String> numberToTextMap;
    private TextToSpeech textToSpeech;
    private CountDownTimer showComputationTimer;
    private TextView computationText;
    private EditText compEditText;
    private Button nextBtn, repeatBtn;
    private int currentCompNum = 0;
    private String currentCompText;
    private boolean movingToNextActivity = false;
    private boolean hasRepeated = false;
    private Context mContext;
    private Vibrator mVibrator;

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

        // Sets up text to speech to read numbers
        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    Set<String> a = new HashSet<>();
                    a.add("male");//here you can give male if you want to select male voice.
                    //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                    Voice v = new Voice("en-us-x-sfg#male_1-local",new Locale("en","US"),400,200,true,a);
                    textToSpeech.setVoice(v);
                    textToSpeech.setSpeechRate(0.7f);

                    // int result = T2S.setLanguage(Locale.US);
                    int result = textToSpeech.setVoice(v);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        }, "com.google.android.tts");

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

        // Buttons

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

        // Timers

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
        return view;
    }

    private void moveToNextComputation() {
        if (!movingToNextActivity && !compEditText.getText().toString().equals("")) {
            logEndTimeAndData(getActivity().getApplicationContext(), "computation," + compEditText.getText().toString());
            vibrateToastAndExecuteSound(compEditText.getText().toString());
            currentCompNum++;
            if (currentCompNum < COMPUTATION_LIST.length) {
                hasRepeated = false;
                showComputationTimer.cancel();
                if (textToSpeech.isSpeaking()) {
                    textToSpeech.stop();
                }
                compEditText.setText("");
                currentCompText = COMPUTATION_LIST[currentCompNum];
                computationText.setText(currentCompText);
                computationText.setVisibility(View.VISIBLE);
                repeatBtn.setVisibility(View.INVISIBLE);
                readCurrentComputation();
            } else {
                movingToNextActivity = true;
                ((MainActivity)getActivity()).getFragmentData(null);
            }
        }
    }

    private void readCurrentComputation() {
        // Split the numbers into strings
        String[] numbersToRead = currentCompText.split(" ");

        // Read each part of computation
        for (String numberOrSymbol : numbersToRead) {
            String textSpeech = numberOrSymbol;
            if (numberToTextMap.get(numberOrSymbol) != null)
                textSpeech = numberToTextMap.get(numberOrSymbol);
            textToSpeech.speak(textSpeech, TextToSpeech.QUEUE_ADD, null);
        }

        // Grey out or keep repeat button color
        if (hasRepeated) {
            repeatBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
        } else {
            repeatBtn.getBackground().setTint(getResources().getColor(R.color.colorAccent));
        }

        // Set the views visibilities
        showComputationTimer.start();

    }

    private void vibrateToastAndExecuteSound(String submitText) {
        // Vibrate the device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mVibrator != null) {
            mVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else if (mVibrator != null) {
            //deprecated in API 26
            mVibrator.vibrate(500);
        }

        // Toast affirmative message
        Toast t = Toast.makeText(getActivity(), "'" + submitText + "' submitted!", Toast.LENGTH_LONG);
        t.setGravity(Gravity.TOP, 0, 5);
        t.show();
    }

//    public void setResponseTextToSpeechText(String speechText) {
//        compEditText.setText(speechText);
//    }

    private void repeatComputationRead() {
        if (!hasRepeated) {
            hasRepeated = true;
            repeatBtn.setVisibility(View.INVISIBLE);
            computationText.setVisibility(View.VISIBLE);
            readCurrentComputation();
        }
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
}
