package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

public class EducationFragment extends BasicQuestionFragment {

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

        educationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).viewOnTouch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ((MainActivity)getActivity()).viewOnTouch();
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

    @Override
    public boolean isQuestionAnswered() {
        return !educationSpinner.getSelectedItem().toString().equals("");
    }
}
