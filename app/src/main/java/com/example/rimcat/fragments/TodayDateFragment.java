package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class TodayDateFragment extends QuestionFragment {

    private static final String TAG = "TodayDateFragment";
    private EditText inputMonth, inputDay, inputYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_date, container, false);
        inputDay = view.findViewById(R.id.input_today_day);
        inputMonth = view.findViewById(R.id.input_today_month);
        inputYear = view.findViewById(R.id.input_today_year);

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();

        return view;
    }

    @Override
    public boolean loadDataModel() {
//        if (    inputDay.getText().toString().equals("") ||
//                inputMonth.getText().toString().equals("") ||
//                inputYear.getText().toString().equals(""))
//            return false;
//        String todayDateResult =    Integer.parseInt(inputMonth.getText().toString()) + "/" +
//                                    Integer.parseInt(inputDay.getText().toString()) + "/" +
//                                    Integer.parseInt(inputYear.getText().toString());
//        logEndTimeAndData(getActivity().getApplicationContext(), "todays_date," + todayDateResult);
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new DayOfWeekFragment(), "DayOfWeekFragment");
    }
}
