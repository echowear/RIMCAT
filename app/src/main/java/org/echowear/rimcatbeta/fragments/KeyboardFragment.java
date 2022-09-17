package org.echowear.rimcatbeta.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

public class KeyboardFragment extends QuestionFragment {

    private static final String     TAG = "RecallResponseFragment";
    private Context mContext;
    private TextView promptText;
    private EditText responseText;
    private FloatingActionButton audioBtn;
    int currentScreen = 0;
    int counter = 0;
    boolean wasMicPressed, micPressedLast;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        mContext = view.getContext();
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        responseText = view.findViewById(R.id.keyboard_recall_word);
        audioBtn = view.findViewById(R.id.keyboard_audio_btn);
        Button submitBtn = view.findViewById(R.id.keyboard_addBtn);
        promptText = view.findViewById(R.id.keyboard_text);

        mediaPlayer = MediaPlayer.create(requireActivity().getApplicationContext(), R.raw.inst30);

        responseText.setOnClickListener(v -> micPressedLast = false);

        submitBtn.setOnClickListener(v -> {
            responseText.getText().toString();
            if (responseText.getText().toString().equalsIgnoreCase("hello")) {
                    moveToNextTask();
            } else {
                    Toast.makeText(mContext, "Make sure that the text box has the word 'hello' in it.", Toast.LENGTH_LONG).show();
//                    ViewGroup group = (ViewGroup) t.getView();
//                    TextView toastTV = (TextView) group.getChildAt(0);
//                    toastTV.setTextSize(20);
//                    t.show();
            }

        });

        // Initialize speech to text button
        audioBtn.setOnClickListener(v -> {
            micPressedLast = true;
            wasMicPressed = true;
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

            try {
                startActivityForResult(intent, RESULT_SPEECH);
                responseText.setText("");
            } catch (ActivityNotFoundException a) {
                Toast.makeText(mContext,
                        "Oops! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT).show();
//                ViewGroup group = (ViewGroup) t.getView();
//                assert group != null;
//                TextView toastTV = (TextView) group.getChildAt(0);
//                toastTV.setTextSize(20);
//                t.show();
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
            Log.d(TAG, "moveToNextTask: " + counter);
            promptText.setText(R.string.keyboard_prompt2);
            ((MainActivity) requireActivity()).hideSoftKeyboard();
            responseText.setText("");
            audioBtn.show();
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception NullPointerException) {
                Log.d(TAG, "moveToNextTask: audio finished ~ null exception ");
            }
            if (counter == 0) {
                mediaPlayer = MediaPlayer.create(requireActivity().getApplicationContext(), R.raw.inst31);
                Log.d(TAG, "moveToNextTask: MEDIAPLAYER ?? ");
                mediaPlayer.start();
                mediaPlayer = null;
                counter++;
            } else {
                mediaPlayer = null;
            }
            vibrateToastAndExecuteSound("hello");
        } else if (wasMicPressed) {
            logEndTimeAndData(requireActivity().getApplicationContext(), "keyboard_test,null");
            ((MainActivity) requireActivity()).getFragmentData(null);
        } else {
            Toast.makeText(mContext, "Try using the microphone button at least once.", Toast.LENGTH_LONG).show();
//            ViewGroup group = (ViewGroup) t.getView();
//            assert group != null;
//            TextView toastTV = (TextView) group.getChildAt(0);
//            toastTV.setTextSize(20);
//            t.show();
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
        ((MainActivity) requireActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return "N/A";
    }

    @Override
    public String getTriedMicrophone() {
        return "" + micPressedLast;
    }
}
