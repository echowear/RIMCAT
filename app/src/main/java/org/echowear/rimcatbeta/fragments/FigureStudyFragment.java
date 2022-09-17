package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

public class FigureStudyFragment extends QuestionFragment {

    public static final String TAG = "FigureStudyFragment";
    private static final int STUDY_TIME_MILLIS = 6000;
    private static final int[] FIGURE_LIST = new int[] {
            R.drawable.figure_a_1, R.drawable.figure_b_1,
            R.drawable.figure_c_1, R.drawable.figure_d_1,
            R.drawable.figure_e_1, R.drawable.figure_f_1
    };
    private CountDownTimer countDownTimer, figureListCounter;
    private ImageView figureImage;
    private TextView figureText;
    private View promptCard, imageCard;
    private Button figureReadyBtn;
    private int countdown = 0;
    private static final String[] COUNTDOWN_TEXT = { "Ready", "Set", "Go!" };
    private int figCount = 0;
    private boolean showingFigures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_figure_study, container, false);

//        figurePrompts = getResources().getStringArray(R.array.figure_prompt_array);
        figureImage = view.findViewById(R.id.figure_image);
        figureText = view.findViewById(R.id.figure_ready_text);
        figureReadyBtn = view.findViewById(R.id.figure_readyBtn);
        promptCard = view.findViewById(R.id.card);
        imageCard = view.findViewById(R.id.image_card);

        imageCard.setVisibility(View.INVISIBLE);
        figureText.setText(getResources().getString(R.string.verbal_readyPrompt));

        figureReadyBtn.setOnClickListener(v -> onReadyClick());

        // Creates the timer that counts down the verbal recall section
        countDownTimer = new CountDownTimer(3000, 990) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (countdown < 3) {
//                    reactionCountdownText.setText("" + timerIndex);
                    figureText.setText(COUNTDOWN_TEXT[countdown]);
                    countdown++;
                } else {
                    Log.d(TAG, "onTick: ready-set-go went out of range");
                }

//                countdown++;
            }

            @Override
            public void onFinish() { showFigure(); }
        };

        // Creates the timer that handles the word changing event during the verbal recall section
        figureListCounter = new CountDownTimer(STUDY_TIME_MILLIS, STUDY_TIME_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                countdown = 0;
                showPromptOrFinish(); }
        };

        cardView = view.findViewById(R.id.figure_main_page);

        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    private void onReadyClick() {
        figureReadyBtn.setVisibility(View.INVISIBLE);
        figureText.setVisibility(View.INVISIBLE);
//        showFigure();
        // Push figure text down
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) figureText.getLayoutParams();
        params.topMargin = 90;
        figureText.setLayoutParams(params);
        figureText.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }

    private void showFigure() {
        promptCard.setVisibility(View.INVISIBLE);
        imageCard.setVisibility(View.VISIBLE);
        figureText.setTextSize(35);
        figureListCounter.start();
        showingFigures = true;
    }

    private void showPromptOrFinish() {
        // Increment and change view values
        figCount++;
        if (figCount < FIGURE_LIST.length) {
            // Swap image and prompt text
            figureImage.setImageResource(FIGURE_LIST[figCount]);
            // Play affirmative sound
//            try {
//                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
//                r.play();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            // Give the user 3 seconds to prepare
            figureListCounter.start();
        } else {
            logEndTimeAndData(requireActivity().getApplicationContext(), "figure_study,null");
            ((MainActivity) requireActivity()).getFragmentData(null);
        }
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) requireActivity()).addFragment(new FigureSelectFragment(), "FigureSelectFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return "N/A";
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public void onPause() {
        super.onPause();
        if (showingFigures) {
            figureListCounter.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (showingFigures) {
            figureListCounter.start();
        }
    }
}
