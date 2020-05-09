package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;

//  TODO: Fix the logged name for each image.
public class FigureSelectFragment extends QuestionFragment {

    private static final String TAG = "FigureSelectFragment";
    private static final int[] IMAGE_RESOURCES = new int[] {
        R.drawable.fig1_select1,
        R.drawable.fig1_select2,
        R.drawable.fig1_select3,
        R.drawable.fig1_select4,
        R.drawable.fig2_select1,
        R.drawable.fig2_select2,
        R.drawable.fig2_select3,
        R.drawable.fig2_select4
    };
    private ImageView figure1, figure2, figure3, figure4, selectedImage;
    private ArrayList<ImageView> images;
    private Button nextButton;
    private int figureCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_figure_select, container, false);

        cardView = view.findViewById(R.id.figure_select_page);
        figure1 = view.findViewById(R.id.figure_choice1);
        figure2 = view.findViewById(R.id.figure_choice2);
        figure3 = view.findViewById(R.id.figure_choice3);
        figure4 = view.findViewById(R.id.figure_choice4);
        figure1.setTag("fig1");
        figure2.setTag("fig2");
        figure3.setTag("fig3");
        figure4.setTag("fig4");

        View.OnClickListener onSelectFigure = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage =  (ImageView) v;
                for (ImageView image : images)
                    image.setBackgroundColor(getResources().getColor(R.color.white));
                selectedImage.setBackgroundColor(getResources().getColor(R.color.colorConfirm));
            }
        };

        figure1.setOnClickListener(onSelectFigure);
        figure2.setOnClickListener(onSelectFigure);
        figure3.setOnClickListener(onSelectFigure);
        figure4.setOnClickListener(onSelectFigure);

        images = new ArrayList<>();
        images.add(figure1);
        images.add(figure2);
        images.add(figure3);
        images.add(figure4);

        nextButton = view.findViewById(R.id.figure_select_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDataAndPrepareImages();
            }
        });

        startAnimation(true);
        logStartTime();

        return view;
    }

    private void recordDataAndPrepareImages() {
        if (selectedImage != null) {
            Log.d(TAG, "recordDataAndPrepareImages: Image - " + selectedImage.getTag());
            selectedImage.setBackgroundColor(getResources().getColor(R.color.white));
//        logEndTimeAndData(getActivity().getApplicationContext(), "figure_select,image" + selectedImage.getBackground().toString());
            selectedImage = null;
            figureCount += 4;
            if (figureCount < IMAGE_RESOURCES.length) {
                int i = 0;
                for (ImageView image : images) {
                    image.setImageResource(IMAGE_RESOURCES[figureCount + i]);
                    image.setTag("fig" + (figureCount + i));
                    i++;
                }
            } else {
                ((MainActivity)getActivity()).getFragmentData(null);
            }
        }
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
