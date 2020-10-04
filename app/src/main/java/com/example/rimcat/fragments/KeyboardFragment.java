package com.example.rimcat.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;

public class KeyboardFragment extends QuestionFragment {

    private static final String     TAG = "RecallResponseFragment";
    private Context mContext;
    private Vibrator mVibrator;
    private TextView promptText;
    private EditText responseText;
    private Button submitBtn;
    private FloatingActionButton audioBtn;
    int currentScreen = 0;
    boolean wasMicPressed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        responseText = view.findViewById(R.id.keyboard_recall_word);
        audioBtn = view.findViewById(R.id.keyboard_audio_btn);
        submitBtn = view.findViewById(R.id.keyboard_addBtn);
        promptText = view.findViewById(R.id.keyboard_text);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseText.getText().toString() != null && responseText.getText().toString().toLowerCase().equals("hello"))
                    moveToNextTask();
                else {
                    Toast t = Toast.makeText(mContext, "Make sure that the text box has the word 'hello' in it.", Toast.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) t.getView();
                    TextView toastTV = (TextView) group.getChildAt(0);
                    toastTV.setTextSize(20);
                    t.show();
                }

            }
        });

        // Initialize speech to text button
        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasMicPressed = true;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    responseText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(mContext,
                            "Oops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    ViewGroup group = (ViewGroup) t.getView();
                    TextView toastTV = (TextView) group.getChildAt(0);
                    toastTV.setTextSize(20);
                    t.show();
                }
            }
        });
        audioBtn.hide();

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void moveToNextTask() {
        if (currentScreen == 0) {
            currentScreen++;
            promptText.setText(R.string.keyboard_prompt2);
            ((MainActivity)getActivity()).hideSoftKeyboard();
            responseText.setText("");
            audioBtn.show();
            vibrateToastAndExecuteSound("hello");
        } else if (wasMicPressed) {
            ((MainActivity)getActivity()).getFragmentData(null);
        } else {
            Toast t = Toast.makeText(mContext, "Try using the microphone button at least once.", Toast.LENGTH_LONG);
            ViewGroup group = (ViewGroup) t.getView();
            TextView toastTV = (TextView) group.getChildAt(0);
            toastTV.setTextSize(20);
            t.show();
        }
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
        Toast t = Toast.makeText(getActivity(), "'" + submitText + "' submitted! Keep going!", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) t.getView();
        TextView toastTV = (TextView) group.getChildAt(0);
        toastTV.setTextSize(20);
//        t.setGravity(Gravity.TOP, 0, 5);
        t.show();

        // Execute sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
