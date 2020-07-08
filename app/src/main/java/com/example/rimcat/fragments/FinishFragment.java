package com.example.rimcat.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rimcat.R;

import java.util.Objects;

public class FinishFragment extends QuestionFragment {

    private static final String TAG = "FinishFragment";
    private Button quitBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, container, false);

        quitBtn = view.findViewById(R.id.quit_btn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finishAndRemoveTask();
            }
        });

        cardView = view.findViewById(R.id.card);
        startAnimation(true);

        return view;
    }

    @Override
    public boolean loadDataModel() {
        return false;
    }

    @Override
    public void moveToNextPage() { }
}
