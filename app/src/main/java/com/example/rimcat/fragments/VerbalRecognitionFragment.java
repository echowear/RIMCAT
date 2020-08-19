package com.example.rimcat.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;

public class VerbalRecognitionFragment extends QuestionFragment {

    private static final String TAG = "VerbalRecognitionFragment";
    private ArrayList<String>   choiceList;
    private Button              nextButton;
    private Button[]            choiceButtons;
    private View.OnClickListener choiceListener;
    private String[][]            wordList;
    private int                 pageCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verbal_rec, container, false);
        // Layout initialization
        cardView = view.findViewById(R.id.vr_page);

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
                    if (choiceList.contains(b.getText().toString())) {
                        choiceList.remove(b.getText().toString());
                        b.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
                    } else {
                        choiceList.add(b.getText().toString());
                        b.getBackground().setTint(getResources().getColor(R.color.colorAccent));
                    }
            }
        };
        changeButtonText();
    }

    private void changeButtonText() {
        String[] currentChoices = wordList[pageCount];
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setText(currentChoices[i]);
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
            logEndTimeAndData(getActivity().getApplicationContext(), "verbal_recognition_page" + pageCount + "," + choice);
        }
        if (pageCount < wordList.length) {
            changeButtonText();
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
}
