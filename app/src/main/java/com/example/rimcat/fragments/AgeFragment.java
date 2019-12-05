package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class AgeFragment extends  QuestionFragment {
    private static final String TAG = "AgeFragment";
    private EditText inputAge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age, container, false);
        inputAge = view.findViewById(R.id.input_age);
        inputAge.setInputType(InputType.TYPE_CLASS_NUMBER);

        //TODO: Start fade-in animation

        return view;
    }

    @Override
    public boolean loadDataModel(DataLogModel dataLogModel) {
//        if (inputAge.getText().toString().equals("") || dataLogModel == null)
//            return false;
//        dataLogModel.age = Integer.parseInt(inputAge.getText().toString());
        return true;
    }

    @Override
    public void startAnimation() {
        ((MainActivity)getActivity()).addFragment(new BirthdayFragment(), "BirthdayFragment");
    }
}
