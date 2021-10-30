package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

public class HomeFragment extends BasicQuestionFragment {
    private static final String TAG = "HomeFragment";
    private EditText inputPatientID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        inputPatientID = view.findViewById(R.id.input_patientID);
        inputPatientID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((MainActivity)getActivity()).viewOnTouch();
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        if (!isQuestionAnswered())
            return false;
        QuestionFragment.PATIENT_ID = inputPatientID.getText().toString();
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return null;
    }

    @Override
    public String getTriedMicrophone() {
        return null;
    }

    @Override
    public boolean isQuestionAnswered() {
        return !inputPatientID.getText().toString().equals("");
    }
}
