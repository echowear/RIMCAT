package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rimcat.R;

public class FigureSelectFragment extends QuestionFragment {

    private static final String TAG = "FigureSelectFragment";
    private ImageView figure1, figure2, figure3, figure4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_figure_select, container, false);

        cardView = view.findViewById(R.id.figure_select_page);
        figure1 = view.findViewById(R.id.figure_choice1);
        figure2 = view.findViewById(R.id.figure_choice2);
        figure3 = view.findViewById(R.id.figure_choice3);
        figure4 = view.findViewById(R.id.figure_choice4);
        figure1.setAlpha(0.8f);
        figure2.setAlpha(0.8f);
        figure3.setAlpha(0.8f);
        figure4.setAlpha(0.8f);

        View.OnClickListener onSelectFigure = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView selectedImage =  (ImageView) v;
                figure1.setAlpha(0.8f);
                figure2.setAlpha(0.8f);
                figure3.setAlpha(0.8f);
                figure4.setAlpha(0.8f);
                selectedImage.setAlpha(1f);
            }
        };

        figure1.setOnClickListener(onSelectFigure);
        figure2.setOnClickListener(onSelectFigure);
        figure3.setOnClickListener(onSelectFigure);
        figure4.setOnClickListener(onSelectFigure);

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
