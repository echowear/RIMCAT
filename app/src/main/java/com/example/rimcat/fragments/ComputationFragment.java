package com.example.rimcat.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.Locale;

public class ComputationFragment extends QuestionFragment {
    private static final String TAG = "ComputationFragment";
    private static final int MILLIS_TO_SHOW_COMPUTATION = 2500;
    private static final String[] COMPUTATION_LIST = {
            "6 + 11", "37 + 6", "41 - 5", "34 - 8", "9 x 4", "8 x 6", "32 ÷ 8", "68 ÷ 2"
    };
    private HashMap<String, String> numberToTextMap;
    private TextToSpeech textToSpeech;
    private CountDownTimer showComputationTimer;
    private TextView computationText;
    private EditText compEditText;
    private Button nextBtn, repeatBtn;
    private FloatingActionButton audioBtn;
    private int currentCompNum = 0;
    private String currentCompText;
    private boolean movingToNextActivity = false;
    private boolean hasRepeated = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_computation, container, false);
        compEditText = view.findViewById(R.id.comp_answer);
        audioBtn = view.findViewById(R.id.comp_audio_btn);
        nextBtn = view.findViewById(R.id.comp_next_btn);
        repeatBtn = view.findViewById(R.id.comp_repeat_btn);
        computationText = view.findViewById(R.id.comp_text);

        numberToTextMap = new HashMap<>();
        numberToTextMap.put("-", "Minus");
        numberToTextMap.put("x", "Times");

        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

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

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    compEditText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(view.getContext(),
                            "Oops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void moveToNextComputation() {
        if (!movingToNextActivity && !compEditText.getText().toString().equals("")) {
            logEndTimeAndData(getActivity().getApplicationContext(), "computation," + compEditText.getText().toString());
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void repeatComputationRead() {
        if (!hasRepeated) {
            hasRepeated = true;
            repeatBtn.setVisibility(View.INVISIBLE);
            computationText.setVisibility(View.VISIBLE);
            readCurrentComputation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
    }

    public void setResponseTextToSpeechText(String speechText) {
        compEditText.setText(speechText);
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
