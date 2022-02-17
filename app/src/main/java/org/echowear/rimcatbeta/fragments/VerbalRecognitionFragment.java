package org.echowear.rimcatbeta.fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;
import org.echowear.rimcatbeta.data_log.CorrectAnswerDictionary;

import java.util.ArrayList;

public class VerbalRecognitionFragment extends QuestionFragment {

    private static final String TAG = "VerbalRecognitionFragment";
    private ArrayList<String>   choiceList;
    private Button              nextButton;
    private Button[]            choiceButtons;
    private TextView            verbalRecReminder;
    private View.OnClickListener choiceListener;
    private String[][]            wordList;
    private int                 pageCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verbal_rec, container, false);
        // Layout initialization
        cardView = view.findViewById(R.id.vr_page);

        // Bold prompt text
        verbalRecReminder = view.findViewById(R.id.verbal_rec_reminder);
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        String reminderText = getResources().getString(R.string.verbal_rec_reminder);
        int highlightTextLength = "remember.".length();
        SpannableString reminderTextSS = new SpannableString(reminderText);
        reminderTextSS.setSpan(fcs, reminderText.length() - highlightTextLength, reminderText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        verbalRecReminder.setText(reminderTextSS);

        // Set up grid
        choiceList = new ArrayList<>();
        choiceButtons = new Button[] {
                view.findViewById(R.id.vrb1), view.findViewById(R.id.vrb2),
                view.findViewById(R.id.vrb3), view.findViewById(R.id.vrb4),
                view.findViewById(R.id.vrb5), view.findViewById(R.id.vrb6),
                view.findViewById(R.id.vrb7), view.findViewById(R.id.vrb8),
                view.findViewById(R.id.vrb9), view.findViewById(R.id.vrb10),
                view.findViewById(R.id.vrb11), view.findViewById(R.id.vrb12)
        };
        wordList = new String[][] {
                getResources().getStringArray(R.array.verbal_recognition_words_1),
                getResources().getStringArray(R.array.verbal_recognition_words_2),
                getResources().getStringArray(R.array.verbal_recognition_words_3)
        };
        initializeGrid();

        // Set up next button
        nextButton = view.findViewById(R.id.vr_next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareNextGrid();
            }
        });

        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void initializeGrid() {
        choiceListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AppCompatButton b = (AppCompatButton) v;
                String currentText = (String) b.getText();
                    if (currentText.contains("\u2611")) {
                        choiceList.remove(currentText.replace("\u2611   ",""));
                        b.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
                        b.setText(currentText.replace("\u2611", "\u2610"));
                        Log.d(TAG, String.valueOf(choiceList));
                    } else {
                        choiceList.add(currentText.replace("\u2610   ", ""));
                        b.getBackground().setTint(getResources().getColor(R.color.colorAccent));
                        b.setText(currentText.replace("\u2610","\u2611"));
                        Log.d(TAG, String.valueOf(choiceList));
                    }
            }
        };
        changeButtonText();
    }

    private void changeButtonText() {
        String[] currentChoices = wordList[pageCount];
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setText("\u2610   " + currentChoices[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                choiceButtons[i].getBackground().setTint(getResources().getColor(R.color.backgroundColor));
            }
            if (choiceListener != null)
                choiceButtons[i].setOnClickListener(choiceListener);
        }
    }

    private void prepareNextGrid() {
        pageCount++;
        for (String choice : choiceList) {
            logEndTimeAndData(getActivity().getApplicationContext(), "verbal_recognition_" + pageCount + "," + choice);
        }
        if (pageCount < wordList.length) {
            choiceList.clear();
            changeButtonText();
            logStartTime();
        } else {
            ((MainActivity)getActivity()).getFragmentData(null);
        }
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
        return TextUtils.join(" ", CorrectAnswerDictionary.TRIAL_LIST_ONE);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
