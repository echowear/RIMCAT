package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class EducationFragment extends QuestionFragment {

    private static final String TAG = "EducationFragment";
    private Spinner educationSpinner;
    private ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        educationSpinner = view.findViewById(R.id.education_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.education_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        educationSpinner.setAdapter(adapter);

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        if (educationSpinner.getSelectedItem().toString().equals(""))
            return false;
        logEndTimeAndData(getActivity().getApplicationContext(), "education_level," + educationSpinner.getSelectedItem().toString());
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new TodayDateFragment(), "TodayDateFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return "N/A";
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
