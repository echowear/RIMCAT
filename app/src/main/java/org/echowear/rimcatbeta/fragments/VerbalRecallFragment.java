package org.echowear.rimcatbeta.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.echowear.rimcatbeta.ActivitiesModel;
import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;
import org.echowear.rimcatbeta.data_log.CorrectAnswerDictionary;

public class VerbalRecallFragment extends QuestionFragment {
    private EditText                responseText;
    private Button                  addBtn;
    private boolean micPressedLast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_verbal_recall, container, false);
        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize views
        responseText = view.findViewById(R.id.vresponse_recall_word);
        FloatingActionButton audioBtn = view.findViewById(R.id.recall_audio_btn);
        addBtn = view.findViewById(R.id.vresponse_addBtn);
        Button doneRecallingBtn = view.findViewById(R.id.done_recalling_btn);
        doneRecallingBtn.setVisibility(View.VISIBLE);

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

        responseText.setOnClickListener(v -> micPressedLast = false);

        // Initialize speech to text button
        audioBtn.setOnClickListener(v -> {
            micPressedLast = true;
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

            try {
                startActivityForResult(intent, RESULT_SPEECH);
                responseText.setText("");
            } catch (ActivityNotFoundException a) {
                toastAtTopOfScreen("Oops! Your device doesn't support Speech to Text", Toast.LENGTH_LONG);
            }
        });

        // Initialize submit word button
        addBtn.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
        addBtn.setOnClickListener(v -> {
            if (!responseText.getText().toString().equals("")) {
                String submitText = responseText.getText().toString();
                logResponse(submitText);
                responseText.setText("");
                vibrateToastAndExecuteSound(submitText);
                logStartTime();
            }
        });

        // Initialize done recalling button
        doneRecallingBtn.setOnClickListener(v -> {
            if (!responseText.getText().toString().equals("")) {
                toastAtTopOfScreen("Please submit the word you entered before finishing.", Toast.LENGTH_SHORT);
            } else {
                ((MainActivity) requireActivity()).showRecallFinishDialog();
            }
        });

        cardView = view.findViewById(R.id.vresponse_layout);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void logResponse(String response) {
        int currentView = ((MainActivity) requireActivity()).getViewNumber();
        if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_1)
            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_1," + response);
        else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_2)
            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_2," + response);
        else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_3)
            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_3," + response);
//        else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_4)
//            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_4," + response);
//        else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_5)
//            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_5," + response);
        else if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_6)
            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_6," + response);
    }

    public void executePostMessageSetup() {
    }

    public void setResponseTextToSpeechText(String speechText) {
        responseText.setText(speechText);
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) requireActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
//       int currentView = ((MainActivity)getActivity()).getViewNumber();
//       if (currentView == ActivitiesModel.VERBAL_RECALL_SCREEN_4)
//           return TextUtils.join(" ", CorrectAnswerDictionary.TRIAL_LIST_TWO);
//       else
//           return TextUtils.join(" ", CorrectAnswerDictionary.TRIAL_LIST_ONE);
        return TextUtils.join(" ", CorrectAnswerDictionary.TRIAL_LIST_ONE);
    }

    @Override
    public String getTriedMicrophone() {
        return "" + micPressedLast;
    }
}
