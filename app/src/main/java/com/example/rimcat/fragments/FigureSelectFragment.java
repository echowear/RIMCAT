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
import com.example.rimcat.ActivitiesModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;
import java.util.ArrayList;

public class FigureSelectFragment extends QuestionFragment {

    private static final String TAG = "FigureSelectFragment";
    private static final int[] IMAGE_RESOURCES_1 = new int[] {
            R.drawable.figure_a_2, R.drawable.figure_a_1, R.drawable.figure_a_3, R.drawable.figure_a_4,
            R.drawable.figure_b_2, R.drawable.figure_b_3, R.drawable.figure_b_4, R.drawable.figure_b_1,
            R.drawable.figure_c_4, R.drawable.figure_c_2, R.drawable.figure_c_3, R.drawable.figure_c_1,
            R.drawable.figure_d_2, R.drawable.figure_d_3, R.drawable.figure_d_1, R.drawable.figure_d_4,
            R.drawable.figure_e_2, R.drawable.figure_e_1, R.drawable.figure_e_3, R.drawable.figure_e_4,
            R.drawable.figure_f_2, R.drawable.figure_f_3, R.drawable.figure_f_1, R.drawable.figure_f_4
    };
    private static final int[] IMAGE_RESOURCES_2 = new int[] {
            R.drawable.figure_a_3, R.drawable.figure_a_2, R.drawable.figure_a_1, R.drawable.figure_a_4,
            R.drawable.figure_b_1, R.drawable.figure_b_4, R.drawable.figure_b_3, R.drawable.figure_b_2,
            R.drawable.figure_c_4, R.drawable.figure_c_2, R.drawable.figure_c_1, R.drawable.figure_c_3,
            R.drawable.figure_d_4, R.drawable.figure_d_1, R.drawable.figure_d_2, R.drawable.figure_d_3,
            R.drawable.figure_e_2, R.drawable.figure_e_3, R.drawable.figure_e_4, R.drawable.figure_e_1,
            R.drawable.figure_f_1, R.drawable.figure_f_2, R.drawable.figure_f_3, R.drawable.figure_f_4
    };
    private int[] currentImageSrcList;
    private ImageView figure1, figure2, figure3, figure4, selectedImage;
    private ArrayList<ImageView> images;
    private Button nextButton;
    private int figureCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_figure_select, container, false);

        // Set the current word list to the correct trial list words
        int currentView = ((MainActivity)getActivity()).getViewNumber();
        if (currentView == ActivitiesModel.FIGURE_SELECT_SCREEN)
            currentImageSrcList = IMAGE_RESOURCES_1;
        else
            currentImageSrcList = IMAGE_RESOURCES_2;

        cardView = view.findViewById(R.id.figure_select_page);
        figure1 = view.findViewById(R.id.figure_choice1);
        figure2 = view.findViewById(R.id.figure_choice2);
        figure3 = view.findViewById(R.id.figure_choice3);
        figure4 = view.findViewById(R.id.figure_choice4);
        figure1.setImageResource(currentImageSrcList[0]);
        figure2.setImageResource(currentImageSrcList[1]);
        figure3.setImageResource(currentImageSrcList[2]);
        figure4.setImageResource(currentImageSrcList[3]);
        figure1.setTag(getResources().getResourceName(currentImageSrcList[0]));
        figure2.setTag(getResources().getResourceName(currentImageSrcList[1]));
        figure3.setTag(getResources().getResourceName(currentImageSrcList[2]));
        figure4.setTag(getResources().getResourceName(currentImageSrcList[3]));

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
        nextButtonReady();
        return view;
    }

    private void recordDataAndPrepareImages() {
        if (selectedImage != null) {
            Log.d(TAG, "recordDataAndPrepareImages: Image - " + selectedImage.getTag());
            selectedImage.setBackgroundColor(getResources().getColor(R.color.white));
            String imageTag = selectedImage.getTag().toString();
            logEndTimeAndData(getActivity().getApplicationContext(), "figure_select," + imageTag.substring(imageTag.lastIndexOf("/") + 1));
            selectedImage = null;
            figureCount += 4;
            if (figureCount < currentImageSrcList.length) {
                int i = 0;
                for (ImageView image : images) {
                    image.setImageResource(currentImageSrcList[figureCount + i]);
                    image.setTag(getResources().getResourceName(currentImageSrcList[figureCount + i]));
                    i++;
                }
                logStartTime();
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
