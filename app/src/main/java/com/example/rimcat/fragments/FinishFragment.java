package com.example.rimcat.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import com.example.rimcat.R;
import java.util.Objects;

public class FinishFragment extends QuestionFragment {

    private static final String TAG = "FinishFragment";
    private Animation.AnimationListener animationListener;
    private CardView feedbackCard, endCard;
    private EditText feedbackText;
    private Button quitBtn, nextBtn;
    private boolean isFadingOut = true;
    private boolean isAnimationActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, container, false);

        feedbackCard = view.findViewById(R.id.feedback_card);
        endCard = view.findViewById(R.id.end_card);
        feedbackText = view.findViewById(R.id.finish_feedback_edit_text);
        nextBtn = view.findViewById(R.id.finish_next_btn);
        quitBtn = view.findViewById(R.id.quit_btn);

        feedbackCard.setVisibility(View.VISIBLE);
        endCard.setVisibility(View.INVISIBLE);

        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFadingOut) {
                    feedbackCard.setVisibility(View.INVISIBLE);
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    fadeInAnimation.setAnimationListener(animationListener);
                    endCard.startAnimation(fadeInAnimation);
                }
                else {
                    endCard.setVisibility(View.VISIBLE);
                    isAnimationActive = false;
                }
                isFadingOut = !isFadingOut;
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        };

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!feedbackText.getText().toString().equals(""))
                    logEndTimeAndData(getActivity().getApplicationContext(), "finish_feedback," + feedbackText.getText().toString());
                isAnimationActive = true;
                Animation fadeOutAnimation =  AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                fadeOutAnimation.setAnimationListener(animationListener);
                feedbackCard.startAnimation(fadeOutAnimation);
            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!isAnimationActive)
                    Objects.requireNonNull(getActivity()).finishAndRemoveTask();
            }
        });

        cardView = view.findViewById(R.id.finish_page);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() { return false; }

    @Override
    public void moveToNextPage() { }

    @Override
    public String getCorrectAnswer() {
        return null;
    }
}
