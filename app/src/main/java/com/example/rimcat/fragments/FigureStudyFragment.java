package com.example.rimcat.fragments;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class FigureStudyFragment extends QuestionFragment {

    public static final String TAG = "FigureStudyFragment";
    private static final int STUDY_TIME_MILLIS = 6000;
    private static final int[] FIGURE_LIST = new int[] {
            R.drawable.figure_a_1, R.drawable.figure_b_1,
            R.drawable.figure_c_1, R.drawable.figure_d_1,
            R.drawable.figure_e_1, R.drawable.figure_f_1
    };
    private CountDownTimer countDownTimer, figureListCounter;
    private String[] figurePrompts;
    private ImageView figureImage;
    private TextView figureText;
    private View promptCard, imageCard;
    private Button figureReadyBtn;
    private int figCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_figure_study, container, false);

        figurePrompts = getResources().getStringArray(R.array.figure_prompt_array);
        figureImage = view.findViewById(R.id.figure_image);
        figureText = view.findViewById(R.id.figure_ready_text);
        figureReadyBtn = view.findViewById(R.id.figure_readyBtn);
        promptCard = view.findViewById(R.id.card);
        imageCard = view.findViewById(R.id.image_card);

        imageCard.setVisibility(View.INVISIBLE);
        figureText.setText(figurePrompts[figCount]);

        figureReadyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReadyClick();
            }
        });

        // Creates the timer that counts down the verbal recall section
        countDownTimer = new CountDownTimer(3000, 990) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() { showFigure(); }
        };

        // Creates the timer that handles the word changing event during the verbal recall section
        figureListCounter = new CountDownTimer(STUDY_TIME_MILLIS, STUDY_TIME_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() { showPromptOrFinish(); }
        };

        cardView = view.findViewById(R.id.figure_main_page);

        startAnimation(true);
        nextButtonReady();
        return view;
    }

    private void onReadyClick() {
        figureReadyBtn.setVisibility(View.INVISIBLE);
        showFigure();
        // Push figure text down
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) figureText.getLayoutParams();
        params.topMargin = 90;
        figureText.setLayoutParams(params);
    }

    private void showFigure() {
        promptCard.setVisibility(View.INVISIBLE);
        imageCard.setVisibility(View.VISIBLE);
        figureText.setTextSize(35);
        figureListCounter.start();
    }

    private void showPromptOrFinish() {
        // Increment and change view values
        figCount++;
        if (figCount < FIGURE_LIST.length) {
            imageCard.setVisibility(View.INVISIBLE);
            // Swap image and prompt text
            figureImage.setImageResource(FIGURE_LIST[figCount]);

            figureText.setText(figurePrompts[figCount]);
            // Make prompt visible again
            promptCard.setVisibility(View.VISIBLE);
            // Play affirmative sound
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Give the user 3 seconds to prepare
            countDownTimer.start();
        } else {
            ((MainActivity)getActivity()).getFragmentData(null);
        }
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new FigureSelectFragment(), "FigureSelectFragment");
    }
}
