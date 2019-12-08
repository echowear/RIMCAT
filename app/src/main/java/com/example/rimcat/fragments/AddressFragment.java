package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class AddressFragment extends QuestionFragment {

    private static final String TAG = "AddressFragment";
    private EditText inputStreet, inputCity, inputState, inputZipCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        inputStreet = view.findViewById(R.id.input_street);
        inputCity = view.findViewById(R.id.input_city);
        inputState = view.findViewById(R.id.input_state);
        inputZipCode = view.findViewById(R.id.input_zip);

        inputStreet.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputCity.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputState.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputZipCode.setImeOptions(EditorInfo.IME_ACTION_DONE);

        cardView = view.findViewById(R.id.card);
        startAnimation(true);

        return view;
    }

    @Override
    public boolean loadDataModel(DataLogModel dataLogModel) {
//        if (    inputStreet.getText().toString().equals("") ||
//                inputCity.getText().toString().equals("") ||
//                inputState.getText().toString().equals("") ||
//                inputZipCode.getText().toString().equals("") ||
//                dataLogModel == null)
//            return false;
//        dataLogModel.street = inputStreet.getText().toString();
//        dataLogModel.city = inputCity.getText().toString();
//        dataLogModel.state = inputState.getText().toString();
//        dataLogModel.zipCode = Integer.parseInt(inputZipCode.getText().toString());
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new TodayDateFragment(), "TodayDateFragment");
    }
}
