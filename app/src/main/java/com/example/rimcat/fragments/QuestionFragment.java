package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public abstract class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";

    protected View cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public abstract boolean loadDataModel(DataLogModel dataLogModel);

    public abstract void moveToNextPage();

    public void startAnimation(final boolean isFadeIn) {
        Animation fadeOutAnimation;
        if (isFadeIn)
            fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        else
            fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFadeIn) {
                    moveToNextPage();
                    ((MainActivity)getActivity()).incrementViewNumber();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (cardView != null) {
            cardView.startAnimation(fadeOutAnimation);
        } else {

        }
    }
}
