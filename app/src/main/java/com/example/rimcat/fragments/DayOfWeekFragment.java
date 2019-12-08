package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class DayOfWeekFragment extends QuestionFragment {

    private static final String TAG = "DayOfWeekFragment";
    private Spinner dayOfWeekSpinner;
    private ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_of_week, container, false);
        dayOfWeekSpinner = view.findViewById(R.id.day_of_week_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.day_of_week_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(adapter);

        cardView = view.findViewById(R.id.card);
        startAnimation(true);

        return view;
    }

    @Override
    public boolean loadDataModel(DataLogModel dataLogModel) {
        if (dayOfWeekSpinner.getSelectedItem().toString().equals("") || dataLogModel == null)
            return false;
        dataLogModel.dayOfTheWeek = dayOfWeekSpinner.getSelectedItem().toString();
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new SeasonFragment(), "SeasonFragment");
    }
}
