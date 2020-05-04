package com.example.rimcat.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.rimcat.R;

import java.util.ArrayList;
import java.util.List;

public class SemanticChoiceFragment extends QuestionFragment {
    private static final String TAG = "SemanticChoiceFragment";
    private ConstraintLayout    layout1, layout2;
    private TableLayout         semanticGrid;
    private TextView            semanticChoicePrompt, semanticCountdownText;
    private ArrayList<String>   choiceList;
    private ArrayList<Button>   buttonList;
    private Button              readyButton;
    private Button[]            choiceButtons;
    private CountDownTimer      readyCountdown;
    private String[][]          semanticChoices;
    private String[]            semanticPrompts;
    private int                 pageCount, timerIndex = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semantic_choice, container, false);
        cardView = view.findViewById(R.id.semantic_choice_page);
        layout1 = view.findViewById(R.id.semantic_layout1);
        layout2 = view.findViewById(R.id.semantic_layout2);
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.INVISIBLE);

        choiceList = new ArrayList<>();
        choiceButtons = new Button[] {
                view.findViewById(R.id.scb1), view.findViewById(R.id.scb2), view.findViewById(R.id.scb3),
                view.findViewById(R.id.scb4), view.findViewById(R.id.scb5), view.findViewById(R.id.scb6),
                view.findViewById(R.id.scb7), view.findViewById(R.id.scb8), view.findViewById(R.id.scb9)
        };
        semanticChoices = new String[][] {
                getResources().getStringArray(R.array.semantic_choices_1),
                getResources().getStringArray(R.array.semantic_choices_2)
        };
        semanticGrid = view.findViewById(R.id.semantic_grid);
        semanticGrid.setVisibility(View.INVISIBLE);
        initializeGrid();

        semanticCountdownText = view.findViewById(R.id.semantic_countdown);
        semanticChoicePrompt = view.findViewById(R.id.semantic_prompt);
        semanticPrompts = getResources().getStringArray(R.array.semantic_choice_prompts);
        semanticChoicePrompt.setText("Category: " + semanticPrompts[pageCount]);
        semanticChoicePrompt.setTypeface(null, Typeface.BOLD);

        readyButton = view.findViewById(R.id.semantic_ready_btn);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);
                readyCountdown.start();
            }
        });

        readyCountdown = new CountDownTimer(3000, 980) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
                if (timerIndex > 0)
                    semanticCountdownText.setText("" + timerIndex);
                timerIndex--;
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                semanticCountdownText.setVisibility(View.INVISIBLE);
                semanticGrid.setVisibility(View.VISIBLE);
            }
        };

        startAnimation(true);
        logStartTime();
        return view;
    }

    private void initializeGrid() {
        String[] currentChoices = semanticChoices[pageCount];
        View.OnClickListener choiceListener = new View.OnClickListener() {
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
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setText(currentChoices[i]);
            choiceButtons[i].setOnClickListener(choiceListener);
        }
    }


    @Override
    public boolean loadDataModel() {
        return false;
    }

    @Override
    public void moveToNextPage() {

    }

    @Override
    public void onDestroy() {
        if (readyCountdown != null) {
            readyCountdown.cancel();
        }
        super.onDestroy();
    }
}
