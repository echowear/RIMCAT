package com.example.rimcat.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class InstructionsFragment extends QuestionFragment {
    private static final String TAG = "InstructionsFragment";
    private int currentView;
    private TextView instructionsText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instructions, container, false);

        cardView = view.findViewById(R.id.card);
        instructionsText = view.findViewById(R.id.plain_inst1);

        currentView = ((MainActivity)getActivity()).getViewNumber();
        switch (currentView) {
            case DataLogModel.INSTRUCTIONS_SCREEN_1:
                instructionsText.setText(R.string.instructions_appInst);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rimcat_inst1_test);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_2:
                instructionsText.setText(R.string.instructions_verbalInst);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_3:
            case DataLogModel.INSTRUCTIONS_SCREEN_4:
                instructionsText.setText(R.string.instructions_verbalInst2);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_5:
                instructionsText.setText(R.string.instructions_verbalInst3);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_6:
                instructionsText.setText(R.string.instructions_verbalInst4);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_7:
                instructionsText.setText(R.string.instructions_imageInst);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_8:
                instructionsText.setText(R.string.instructions_figureStudy);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_9:
                instructionsText.setText(R.string.instructions_reading_comp);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_10:
                instructionsText.setText(R.string.instructions_semanticChoice);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_11:
                instructionsText.setText(R.string.instructions_verbalInst5);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_12:
                instructionsText.setText(R.string.instructions_figureSelect2);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_13:
                instructionsText.setText(R.string.instructions_semantic_relatedness);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                break;
            default:
                instructionsText.setText(R.string.error);
                break;
        }
        Log.d(TAG, "onCreateView: Current number: " + ((MainActivity)getActivity()).getViewNumber());

        startAnimation(true);

        return view;
    }

    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        switch (currentView) {
            case DataLogModel.INSTRUCTIONS_SCREEN_1:
                ((MainActivity)getActivity()).addFragment(new EducationFragment(), "NameFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_2:
            case DataLogModel.INSTRUCTIONS_SCREEN_3:
            case DataLogModel.INSTRUCTIONS_SCREEN_4:
            case DataLogModel.INSTRUCTIONS_SCREEN_5:
            case DataLogModel.INSTRUCTIONS_SCREEN_6:
            case DataLogModel.INSTRUCTIONS_SCREEN_11:
                ((MainActivity)getActivity()).addFragment(new VerbalLearningFragment(), "VerbalRecallFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_7:
                ((MainActivity)getActivity()).addFragment(new ImageNameFragment(), "ImageNameFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_8:
                ((MainActivity)getActivity()).addFragment(new FigureStudyFragment(), "FigureStudyFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_9:
                ((MainActivity)getActivity()).addFragment(new ReadingCompFragment(), "ReadingCompFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_10:
                ((MainActivity)getActivity()).addFragment(new SemanticChoiceFragment(), "SemanticChoiceFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_12:
                ((MainActivity)getActivity()).addFragment(new FigureSelectFragment(), "FigureSelectFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_13:
                ((MainActivity)getActivity()).addFragment(new SemanticRelatedness(), "SemanticRelatedness");
                break;
            default:
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }

    }
}
