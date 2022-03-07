package org.echowear.rimcatbeta.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;
import org.echowear.rimcatbeta.data_log.CorrectAnswerDictionary;

import java.util.ArrayList;

public class SemanticChoiceFragment extends QuestionFragment {
    private static final String TAG = "SemanticChoiceFragment";
    private ConstraintLayout    layout1, layout2;
    private TableLayout         semanticGrid;
    private TextView            semanticChoicePrompt, semanticCountdownText;
    private ArrayList<String>   choiceList;
    private Button              readyButton;
    private static final String[] COUNTDOWN_TEXT = { "Ready", "Set", "Go!" };
    private Button[]            choiceButtons;
    private View.OnClickListener choiceListener;
    private CountDownTimer      readyCountdown, selectionCountdown;
    private String[][]          semanticChoices;
    private String[]            semanticPrompts;
    private int                 pageCount, timerIndex = 0;
    private boolean             inSelectionState = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semantic_choice, container, false);
        // Layout initialization
        cardView = view.findViewById(R.id.semantic_choice_page);
        layout1 = view.findViewById(R.id.semantic_layout1);
        layout2 = view.findViewById(R.id.semantic_layout2);
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.INVISIBLE);

        choiceList = new ArrayList<>();
        choiceButtons = new Button[] {
                view.findViewById(R.id.scb1), view.findViewById(R.id.scb2), view.findViewById(R.id.scb3),
                view.findViewById(R.id.scb4), view.findViewById(R.id.scb5), view.findViewById(R.id.scb6),
                view.findViewById(R.id.scb7), view.findViewById(R.id.scb8), view.findViewById(R.id.scb9),
                view.findViewById(R.id.scb10), view.findViewById(R.id.scb11), view.findViewById(R.id.scb12)
        };
        semanticChoices = new String[][] {
                getResources().getStringArray(R.array.semantic_choices_1),
                getResources().getStringArray(R.array.semantic_choices_2),
                getResources().getStringArray(R.array.semantic_choices_3),
                getResources().getStringArray(R.array.semantic_choices_4),
                getResources().getStringArray(R.array.semantic_choices_5),
                getResources().getStringArray(R.array.semantic_choices_6),
                getResources().getStringArray(R.array.semantic_choices_7),
                getResources().getStringArray(R.array.semantic_choices_8),
                getResources().getStringArray(R.array.semantic_choices_9)
        };
        semanticGrid = view.findViewById(R.id.semantic_grid);
        semanticGrid.setVisibility(View.INVISIBLE);
        initializeGrid();

        semanticCountdownText = view.findViewById(R.id.semantic_countdown);
        semanticChoicePrompt = view.findViewById(R.id.semantic_prompt);
        semanticPrompts = getResources().getStringArray(R.array.semantic_choice_prompts);
        changeHeaderText();
        semanticChoicePrompt.setTypeface(null, Typeface.BOLD);

        readyButton = view.findViewById(R.id.semantic_ready_btn);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);
                timerIndex = 0;
                readyCountdown.start();
            }
        });

        readyCountdown = new CountDownTimer(3000, 980) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
//                if (timerIndex > 0)
//                    semanticCountdownText.setText("" + timerIndex);
                if (timerIndex < 3) {
//                    reactionCountdownText.setText("" + timerIndex);
                    semanticCountdownText.setText(COUNTDOWN_TEXT[timerIndex]);
                    timerIndex++;
                } else {
                    Log.d(TAG, "onTick: ready-set-go went out of range");
                }
//                timerIndex--;
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                semanticCountdownText.setVisibility(View.INVISIBLE);
                semanticGrid.setVisibility(View.VISIBLE);
                inSelectionState = true;
                selectionCountdown.start();
                logStartTime();
            }
        };

        selectionCountdown = new CountDownTimer(5000, 980) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                inSelectionState = false;
                pageCount++;
                for (String choice : choiceList) {
                    logEndTimeAndData(getActivity(), "semantic_choice_" + pageCount + "," + choice);
                }
                choiceList.clear();
                if (pageCount < semanticChoices.length) {
                    prepareNextGrid();
                } else {
                    ((MainActivity)getActivity()).getFragmentData(null);
                }

            }
        };

        startAnimation(true);
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
                if (inSelectionState) {
                    if (currentText.contains("\u2611")) {
                        choiceList.remove(currentText.replace("   \u2611",""));
                        b.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
                        b.setText(currentText.replace("\u2611", "\u2610"));
                        Log.d(TAG, String.valueOf(choiceList));
                    } else {
                        choiceList.add(currentText.replace("   \u2610", ""));
                        b.getBackground().setTint(getResources().getColor(R.color.colorAccent));
                        b.setText(currentText.replace("\u2610","\u2611"));
                        Log.d(TAG, String.valueOf(choiceList));
                    }
                }
            }
        };
        changeButtonText();
    }

    private void changeButtonText() {
        String[] currentChoices = semanticChoices[pageCount];
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setText(currentChoices[i] + "   \u2610");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                choiceButtons[i].getBackground().setTint(getResources().getColor(R.color.backgroundColor));
            }
            if (choiceListener != null)
                choiceButtons[i].setOnClickListener(choiceListener);
        }
    }

    private void prepareNextGrid() {
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        changeButtonText();
        // Change category text
        changeHeaderText();
        semanticCountdownText.setVisibility(View.VISIBLE);
        semanticGrid.setVisibility(View.INVISIBLE);
    }

    private void changeHeaderText() {
        String headerText = "Category: " + semanticPrompts[pageCount];
        SpannableString ss = new SpannableString(headerText);

        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        ss.setSpan(fcs, 9, headerText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        semanticChoicePrompt.setText(ss);
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
        return CorrectAnswerDictionary.SEMANTIC_CHOICE_ANSWERS[pageCount - 1];
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public void onDestroy() {
        if (readyCountdown != null) {
            readyCountdown.cancel();
        }
        super.onDestroy();
    }
}
