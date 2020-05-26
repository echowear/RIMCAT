package com.example.rimcat.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private static final int[] FIGURE_LIST = new int[] {
            R.drawable.figure_a_1, R.drawable.figure_b_1,
            R.drawable.figure_c_4, R.drawable.figure_d_1,
            R.drawable.figure_e_1, R.drawable.figure_f_1
    };
    private CountDownTimer countDownTimer, figureListCounter;
    private String[] figurePrompts;
    private ImageView figureImage;
    private TextView figureText;
    private View promptCard;
    private Button figureReadyBtn;
    private int figCount = 0;
    private int timerIndex = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_figure_study, container, false);


        figurePrompts = getResources().getStringArray(R.array.figure_prompt_array);
        figureImage = view.findViewById(R.id.figure_image);
        figureText = view.findViewById(R.id.figure_ready_text);
        figureReadyBtn = view.findViewById(R.id.figure_readyBtn);
        promptCard = view.findViewById(R.id.card);

        figureImage.setVisibility(View.INVISIBLE);
        figureText.setText(figurePrompts[figCount]);
        figureReadyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (figCount < FIGURE_LIST.length) {
                    figureReadyBtn.setVisibility(View.INVISIBLE);
                    figureText.setText("");
                    figureText.setTextSize(55);
                    countDownTimer.start();
                }
            }
        });

        // Creates the timer that counts down the verbal recall section
        countDownTimer = new CountDownTimer(3000, 980) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
                if (timerIndex > 0)
                    figureText.setText("" + timerIndex);
                timerIndex--;
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                promptCard.setVisibility(View.INVISIBLE);
                figureImage.setVisibility(View.VISIBLE);
                figureText.setTextSize(35);
                figureListCounter.start();
            }
        };

        // Creates the timer that handles the word changing event during the verbal recall section
        figureListCounter = new CountDownTimer(10000, 10000) {
            int count = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                count++;
            }

            @Override
            public void onFinish() {
                // Increment and change view values
                figCount++;
                if (figCount < FIGURE_LIST.length) {
                    // Hide Image
                    figureImage.setVisibility(View.INVISIBLE);
                    figureImage.setImageResource(FIGURE_LIST[figCount]);
                    figureText.setText(figurePrompts[figCount]);
                    timerIndex = 3;
                    // Make prompt visible again
                    figureReadyBtn.setVisibility(View.VISIBLE);
                    promptCard.setVisibility(View.VISIBLE);
                } else {
                    ((MainActivity)getActivity()).getFragmentData(null);
                }
            }
        };

        cardView = view.findViewById(R.id.figure_main_page);

        startAnimation(true);

        return view;
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
