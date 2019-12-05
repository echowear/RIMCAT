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

public class BirthdayFragment extends QuestionFragment {

    private static final String TAG = "BirthdayFragment";
    private EditText inputMonth, inputDay, inputYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birthday, container, false);
        inputDay = view.findViewById(R.id.input_day);
        inputMonth = view.findViewById(R.id.input_month);
        inputYear = view.findViewById(R.id.input_year);
        //TODO: Start fade-in animation

        return view;
    }

    @Override
    public boolean loadDataModel(DataLogModel dataLogModel) {
//        if (    inputDay.getText().toString().equals("") ||
//                inputMonth.getText().toString().equals("") ||
//                inputYear.getText().toString().equals("") ||
//                dataLogModel == null)
//            return false;
//        dataLogModel.birthDay = Integer.parseInt(inputDay.getText().toString());
//        dataLogModel.birthMonth = Integer.parseInt(inputMonth.getText().toString());
//        dataLogModel.birthYear = Integer.parseInt(inputYear.getText().toString());
        return true;
    }

    @Override
    public void startAnimation() {
        ((MainActivity)getActivity()).addFragment(new EducationFragment(), "EducationFragment");
    }
}
