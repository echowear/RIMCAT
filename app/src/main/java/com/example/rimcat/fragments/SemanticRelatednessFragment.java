package com.example.rimcat.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;
import com.example.rimcat.data_log.CorrectAnswerDictionary;

public class SemanticRelatednessFragment extends QuestionFragment {
    private static final String TAG = "SemanticRelatedness";
    private TextView            semanticChoicePrompt;
    private String              wordChoice = "";
    private Button[]            choiceButtons;
    private View.OnClickListener choiceListener;
    private String[][]          semanticChoices;
    private String[]            semanticPrompts;
    private int                 pageCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semantic_relatedness, container, false);
        // Layout initialization
        cardView = view.findViewById(R.id.sr_page);

        choiceButtons = new Button[] {
                view.findViewById(R.id.srb1), view.findViewById(R.id.srb2),
                view.findViewById(R.id.srb3), view.findViewById(R.id.srb4)
        };
        semanticChoices = new String[][] {
                getResources().getStringArray(R.array.semantic_relatedness_1),
                getResources().getStringArray(R.array.semantic_relatedness_2),
                getResources().getStringArray(R.array.semantic_relatedness_3),
                getResources().getStringArray(R.array.semantic_relatedness_4),
                getResources().getStringArray(R.array.semantic_relatedness_5),
                getResources().getStringArray(R.array.semantic_relatedness_6),
                getResources().getStringArray(R.array.semantic_relatedness_7),
                getResources().getStringArray(R.array.semantic_relatedness_8),
                getResources().getStringArray(R.array.semantic_relatedness_9),
                getResources().getStringArray(R.array.semantic_relatedness_10),
                getResources().getStringArray(R.array.semantic_relatedness_11),
                getResources().getStringArray(R.array.semantic_relatedness_12),
                getResources().getStringArray(R.array.semantic_relatedness_13),
                getResources().getStringArray(R.array.semantic_relatedness_14),
                getResources().getStringArray(R.array.semantic_relatedness_15)
        };
        initializeGrid();

        semanticChoicePrompt = view.findViewById(R.id.sr_prompt);
        semanticPrompts = getResources().getStringArray(R.array.semantic_relatedness_headers);
        changeHeaderText();
        semanticChoicePrompt.setTypeface(null, Typeface.BOLD);

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
                if (!b.getText().toString().equals(wordChoice)) {
                    wordChoice = b.getText().toString();
                    prepareNextGrid();
                }
            }
        };
        changeButtonText();
    }

    private void changeButtonText() {
        String[] currentChoices = semanticChoices[pageCount];
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setText(currentChoices[i]);
            if (choiceListener != null)
                choiceButtons[i].setOnClickListener(choiceListener);
        }
    }

    private void prepareNextGrid() {
        pageCount++;
        logEndTimeAndData(getActivity(), "semantic_relatedness_page" + pageCount + "," + wordChoice);
        if (pageCount < semanticChoices.length) {
            changeButtonText();
            changeHeaderText();
            logStartTime();
        } else {
            ((MainActivity)getActivity()).getFragmentData(null);
        }
    }

    private void changeHeaderText() {
        String headerText = "Word: " + semanticPrompts[pageCount];
        SpannableString ss = new SpannableString(headerText);

        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        ss.setSpan(fcs, 5, headerText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        semanticChoicePrompt.setText(ss);
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new FinishFragment(), "FinishFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return CorrectAnswerDictionary.SEMANTIC_RELATEDNESS_ANSWERS.get(pageCount - 1);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    //TODO: Consider adding lifecycle events here
}
