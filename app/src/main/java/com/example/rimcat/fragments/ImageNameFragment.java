package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rimcat.R;

public class ImageNameFragment extends QuestionFragment {

    private static final String TAG = "ImageNameFragment";
    private static final int NUM_CHOICES = 3;
    private String[] imageNames;
    private int count = 0;
    private Button btn1, btn2, btn3;
    private View.OnClickListener recordImageChoice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_name, container, false);

        imageNames = getResources().getStringArray(R.array.image_name_array);
        btn1 = view.findViewById(R.id.image_nm_btn_1);
        btn2 = view.findViewById(R.id.image_nm_btn_2);
        btn3 = view.findViewById(R.id.image_nm_btn_3);

        recordImageChoice = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Button value: " + view.getTag());
            }
        };

        btn1.setOnClickListener(recordImageChoice);
        btn2.setOnClickListener(recordImageChoice);
        btn3.setOnClickListener(recordImageChoice);

        btn1.setText(imageNames[0]);
        btn2.setText(imageNames[1]);
        btn3.setText(imageNames[2]);

        cardView = view.findViewById(R.id.card);

        startAnimation(true);

        return view;
    }


    @Override
    public boolean loadDataModel() {
        return false;
    }

    @Override
    public void moveToNextPage() {

    }
}
