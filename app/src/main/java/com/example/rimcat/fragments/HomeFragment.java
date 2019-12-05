package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class HomeFragment extends QuestionFragment {
    private static final String TAG = "HomeFragment";
    private EditText inputPatientID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        inputPatientID = view.findViewById(R.id.input_patientID);

        //TODO: Start fade-in animation

        return view;
    }

    @Override
    public boolean loadDataModel(DataLogModel dataLogModel) {
//        if (inputPatientID.getText().toString().equals("") || dataLogModel == null)
//            return false;
//        dataLogModel.patientID = inputPatientID.getText().toString();
        return true;
    }
    
    public void startAnimation() {
        //TODO: Start fade-out animation and add this code to animation callback
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
