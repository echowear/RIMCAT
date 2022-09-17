package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

public class EducationFragment extends BasicQuestionFragment {

    private Spinner educationSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        educationSpinner = view.findViewById(R.id.education_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.education_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        educationSpinner.setAdapter(adapter);

        educationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) requireActivity()).viewOnTouch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ((MainActivity) requireActivity()).viewOnTouch();
            }
        });

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        if (!isQuestionAnswered())
            return false;
        logEndTimeAndData(requireActivity().getApplicationContext(), "education_level," + educationSpinner.getSelectedItem().toString());
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) requireActivity()).addFragment(new TodayDateFragment(), "TodayDateFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return "N/A";
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public boolean isQuestionAnswered() {
        return !educationSpinner.getSelectedItem().toString().equals("");
    }
}
