package com.example.rimcat.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rimcat.ActivitiesModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;

public class VerbalRecallFragment extends QuestionFragment {
    private static final String     TAG = "RecallResponseFragment";
    private EditText                responseText;
    private Button                  addBtn, doneRecallingBtn;
    private FloatingActionButton    audioBtn;
    private ArrayList<String>       responses;
    private boolean                 firstFinish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_verbal_recall, container, false);
        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize views
        responseText = view.findViewById(R.id.vresponse_recall_word);
        audioBtn = view.findViewById(R.id.recall_audio_btn);
        addBtn = view.findViewById(R.id.vresponse_addBtn);
        doneRecallingBtn = view.findViewById(R.id.done_recalling_btn);
        doneRecallingBtn.setVisibility(View.VISIBLE);
        responses = new ArrayList<String>();

        // Initialize text listener
        responseText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (responseText.getText().toString().equals("")) {
                    addBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
                } else {
                    addBtn.getBackground().setTint(getResources().getColor(R.color.colorAccent));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Initialize speech to text button
        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    responseText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(view.getContext(),
                            "Oops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    ViewGroup group = (ViewGroup) t.getView();
                    TextView toastTV = (TextView) group.getChildAt(0);
                    toastTV.setTextSize(20);
                    t.show();
                }
            }
        });

        // Initialize submit word button
        addBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!responseText.getText().toString().equals("")) {
                    String submitText = responseText.getText().toString();
                    responses.add(submitText);
                    responseText.setText("");
                    vibrateToastAndExecuteSound(submitText, true);
                    logStartTime();
                }
            }
        });

        // Initialize done recalling button
        doneRecallingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!firstFinish) {
                    ((MainActivity)getActivity()).showRetryDialog();
                } else {
                    ((MainActivity)getActivity()).showRecallFinishDialog();
                }
            }
        });

        cardView = view.findViewById(R.id.vresponse_layout);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    public void executePostMessageSetup() {
        firstFinish = true;
    }

    public void setResponseTextToSpeechText(String speechText) {
        responseText.setText(speechText);
    }

    @Override
    public boolean loadDataModel() {
        int currentView = ((MainActivity)getActivity()).getViewNumber();
        for (int i = 0; i < responses.size(); i++) {
            if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_1)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_1," + responses.get(i));
            else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_2)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_2," + responses.get(i));
            else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_3)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_3," + responses.get(i));
            else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_4)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_4," + responses.get(i));
            else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_5)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_5," + responses.get(i));
            else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_6)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_6," + responses.get(i));
        }

        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
