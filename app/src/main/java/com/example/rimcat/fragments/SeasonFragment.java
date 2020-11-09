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

import java.util.Calendar;
import java.util.Date;

public class SeasonFragment extends QuestionFragment {

    private static final String TAG = "TodayDateFragment";
    private Spinner seasonSpinner;
    private ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season, container, false);

        seasonSpinner = view.findViewById(R.id.season_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.season_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        seasonSpinner.setAdapter(adapter);

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        if (seasonSpinner.getSelectedItem().toString().equals(""))
            return false;
        logEndTimeAndData(getActivity().getApplicationContext(), "season," + seasonSpinner.getSelectedItem().toString(), getCorrectAnswer());
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month == 12 || month < 3) {
            return "Winter";
        } else if (month >= 3 && month < 6) {
            return "Spring";
        } else if (month >= 6 && month < 9) {
            return "Summer";
        }
        return "Fall";
    }
}
