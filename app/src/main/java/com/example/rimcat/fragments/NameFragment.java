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

public class NameFragment extends QuestionFragment {
    private static final String TAG = "NameFragment";
    private EditText firstNameInput, lastNameInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name, container, false);

        firstNameInput = view.findViewById(R.id.input_firstName);
        lastNameInput = view.findViewById(R.id.input_lastName);

        //TODO: Start fade-in animation

        return view;
    }

    @Override
    public boolean loadDataModel(DataLogModel dataLogModel) {
//        if (firstNameInput.getText().toString().equals("") || lastNameInput.getText().toString().equals("") || dataLogModel == null)
//            return false;
//        dataLogModel.firstName = firstNameInput.getText().toString();
//        dataLogModel.lastName = lastNameInput.getText().toString();
        return true;
    }

    @Override
    public void startAnimation() {
        ((MainActivity)getActivity()).addFragment(new AgeFragment(), "AgeFragment");
    }
}
