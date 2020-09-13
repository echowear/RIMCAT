package com.example.rimcat.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class StoryMemoryFragment extends QuestionFragment {
    private static final String TAG = "ReadCompTestFragment";
    /**
     * 0 = Yes or No (Two Question Group)
     * 1 = Four Question Group
     * 2 = Fill in the Blank
     */
    private static final int[] QUESTION_TYPE = new int[] {
            1, 0, 0, 1, 0, 1, 1, 1, 1
    };
    private CardView card2;
    private RadioGroup twoQuestionGrp, fourQuestionGrp;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private TextView    questionsText;
    private Button      nextBtn;
    private String[]    questionsArray, answersArray;
    private int fourAnswerCount, questionCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_memory, container, false);
        card2 = view.findViewById(R.id.card2);
        questionsText = view.findViewById(R.id.reading_comp_question);
        questionsText.setTypeface(null, Typeface.BOLD);
        twoQuestionGrp = view.findViewById(R.id.read_two_ans);
        fourQuestionGrp = view.findViewById(R.id.read_four_ans);
        radioButton1 = view.findViewById(R.id.read_ans1);
        radioButton2 = view.findViewById(R.id.read_ans2);
        radioButton3 = view.findViewById(R.id.read_ans3);
        radioButton4 = view.findViewById(R.id.read_ans4);
        nextBtn = view.findViewById(R.id.read_next_btn);
        questionsArray = getResources().getStringArray(R.array.reading_comp_questions);
        answersArray = getResources().getStringArray(R.array.reading_comp_answers);

        // Set those views with onClick listeners and initial values
        questionsText.setText(questionsArray[questionCount]);

        radioButton1.setText(answersArray[(fourAnswerCount * 4)]);
        radioButton2.setText(answersArray[(fourAnswerCount * 4) + 1]);
        radioButton3.setText(answersArray[(fourAnswerCount * 4) + 2]);
        radioButton4.setText(answersArray[(fourAnswerCount * 4) + 3]);
        twoQuestionGrp.setVisibility(View.INVISIBLE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record the data based on current question
                if (QUESTION_TYPE[questionCount] == 0 && twoQuestionGrp.getCheckedRadioButtonId() != -1) {
                    resetRadioGroup(twoQuestionGrp);
                    moveToNextQuestion();
                }
                else if (QUESTION_TYPE[questionCount] == 1 && fourQuestionGrp.getCheckedRadioButtonId() != -1) {
                    resetRadioGroup(fourQuestionGrp);
                    moveToNextQuestion();
                }
            }
        });

        cardView = view.findViewById(R.id.read_test_main);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void resetRadioGroup(RadioGroup group) {
        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int idx = group.indexOfChild(radioButton);
        RadioButton r = (RadioButton) group.getChildAt(idx);
        logEndTimeAndData(getActivity().getApplicationContext(), "reading_comp," + r.getText().toString());
        group.clearCheck();
    }

    private void moveToNextQuestion() {
        questionCount++;
        if (questionCount < QUESTION_TYPE.length) {
            questionsText.setText(questionsArray[questionCount]);
            // Prepare view for next question
            if (QUESTION_TYPE[questionCount] == 0) {
                fourQuestionGrp.setVisibility(View.INVISIBLE);
                twoQuestionGrp.setVisibility(View.VISIBLE);
            } else if (QUESTION_TYPE[questionCount] == 1) {
                twoQuestionGrp.setVisibility(View.INVISIBLE);
                fourQuestionGrp.setVisibility(View.VISIBLE);
                fourAnswerCount++;
                radioButton1.setText(answersArray[(fourAnswerCount * 4)]);
                radioButton2.setText(answersArray[(fourAnswerCount * 4) + 1]);
                radioButton3.setText(answersArray[(fourAnswerCount * 4) + 2]);
                radioButton4.setText(answersArray[(fourAnswerCount * 4) + 3]);

            }
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
