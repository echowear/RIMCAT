package com.example.rimcat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;

public class RecallResponseFragment extends QuestionFragment {

    private Context                 mContext;
    private EditText                responseText;
    private Button                  addBtn, doneRecallingBtn;
    private ArrayList<String>       responses;
    private boolean                 firstWordRecalled, firstFinish;
    private CountDownTimer          doneRecallingTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recall_response, container, false);
        mContext = view.getContext();

        // Initialize views
        responseText = view.findViewById(R.id.vresponse_recall_word);
        addBtn = view.findViewById(R.id.vresponse_addBtn);
        doneRecallingBtn = view.findViewById(R.id.done_recalling_btn);
        responses = new ArrayList<String>();

        // Set add button on click
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!responseText.getText().toString().equals("")) {
                    responses.add(responseText.getText().toString());
                    responseText.setText("");
                    if (!firstWordRecalled || doneRecallingBtn.getVisibility() == View.INVISIBLE) {
                        firstWordRecalled = true;
                        doneRecallingTimer.start();
                    }
                }
            }
        });

        doneRecallingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doneRecallingBtn.getVisibility() == View.VISIBLE) {
                    if (!firstFinish) {
                        Toast.makeText(mContext, "Keep trying! You can do it!", Toast.LENGTH_LONG).show();
                        firstFinish = true;
                        doneRecallingBtn.setVisibility(View.INVISIBLE);
                        doneRecallingTimer.start();
                    } else {
                        ((MainActivity)getActivity()).getFragmentData(null);
                    }
                }
            }
        });

        doneRecallingBtn.setVisibility(View.INVISIBLE);
        // Creates the timer that counts down the verbal recall section
        doneRecallingTimer = new CountDownTimer(15000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                doneRecallingBtn.setVisibility(View.VISIBLE);
            }
        };

        cardView = view.findViewById(R.id.vresponse_layout);
        startAnimation(true);
        logStartTime();

        return view;
    }

    @Override
    public boolean loadDataModel() {
        int currentView = ((MainActivity)getActivity()).getViewNumber();
        for (int i = 0; i < responses.size(); i++) {
            if (currentView == DataLogModel.RECALL_RESPONSE_SCREEN_1)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_1," + responses.get(i));
            else if (currentView == DataLogModel.RECALL_RESPONSE_SCREEN_2)
                logEndTimeAndData(getActivity().getApplicationContext(), "word_recall_2," + responses.get(i));
        }

        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
