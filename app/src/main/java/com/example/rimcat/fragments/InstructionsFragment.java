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
                instructionsText.setText(R.string.instructions_next);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst1);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_2:
                instructionsText.setText(R.string.instructions_appInst);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst2);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_3:
                instructionsText.setText(R.string.instructions_reaction);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst3);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_4:
                instructionsText.setText(R.string.instructions_imageInst);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst3);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_5:
                instructionsText.setText(R.string.instructions_verbalInst);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst4);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_6:
            case DataLogModel.INSTRUCTIONS_SCREEN_7:
                instructionsText.setText(R.string.instructions_verbalInst2);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst5_6);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_8:
                instructionsText.setText(R.string.instructions_verbalInst3);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst7);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_9:
                instructionsText.setText(R.string.instructions_verbalInst4);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst8);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_10:
                instructionsText.setText(R.string.instructions_figure_study);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst9);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_11:
                instructionsText.setText(R.string.instructions_digit_span);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst10);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_12:
                instructionsText.setText(R.string.instructions_read_comp_story);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst11);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_13:
                instructionsText.setText(R.string.instructions_computation);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst12);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_14:
                instructionsText.setText(R.string.instructions_verbalInst5);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst13);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_15:
                instructionsText.setText(R.string.instructions_verbal_rec);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst14);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_16:
                instructionsText.setText(R.string.instructions_semanticChoice);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst15);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_17:
                instructionsText.setText(R.string.instructions_figureSelect2);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst16);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_18:
                instructionsText.setText(R.string.instructions_read_comp_test);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst17);
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_19:
                instructionsText.setText(R.string.instructions_semantic_relatedness);
                instructionsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.inst18);
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
                ((MainActivity)getActivity()).addFragment(new VideoFragment(), "VideoFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_2:
                ((MainActivity)getActivity()).addFragment(new EducationFragment(), "EducationFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_3:
                ((MainActivity)getActivity()).addFragment(new ReactionFragment(), "ReactionFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_4:
                ((MainActivity)getActivity()).addFragment(new ImageNameFragment(), "ImageNameFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_5:
            case DataLogModel.INSTRUCTIONS_SCREEN_6:
            case DataLogModel.INSTRUCTIONS_SCREEN_7:
            case DataLogModel.INSTRUCTIONS_SCREEN_8:
                ((MainActivity)getActivity()).addFragment(new VerbalLearningFragment(), "VerbalLearningFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_9:
            case DataLogModel.INSTRUCTIONS_SCREEN_14:
                ((MainActivity)getActivity()).addFragment(new VerbalRecallFragment(), "VerbalRecallFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_10:
                ((MainActivity)getActivity()).addFragment(new FigureStudyFragment(), "FigureStudyFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_11:
                ((MainActivity)getActivity()).addFragment(new DigitSpanFragment(), "DigitSpanFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_12:
                ((MainActivity)getActivity()).addFragment(new ReadCompStoryFragment(), "ReadCompStoryFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_13:
                ((MainActivity)getActivity()).addFragment(new ComputationFragment(), "ComputationFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_15:
                ((MainActivity)getActivity()).addFragment(new VerbalRecognitionFragment(), "VerbalRecognitionFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_16:
                ((MainActivity)getActivity()).addFragment(new SemanticChoiceFragment(), "SemanticChoiceFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_17:
                ((MainActivity)getActivity()).addFragment(new FigureSelectFragment(), "FigureSelectFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_18:
                ((MainActivity)getActivity()).addFragment(new ReadCompTestFragment(), "ReadCompTestFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_19:
                ((MainActivity)getActivity()).addFragment(new SemanticRelatednessFragment(), "SemanticRelatedness");
                break;
            default:
                Toast t = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                ViewGroup group = (ViewGroup) t.getView();
                TextView toastTV = (TextView) group.getChildAt(0);
                toastTV.setTextSize(20);
                t.show();
        }

    }
}
