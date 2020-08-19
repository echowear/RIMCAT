package com.example.rimcat.fragments;

import android.content.Context;
import android.media.MediaPlayer;
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
import com.example.rimcat.DataLogService;
import com.example.rimcat.GenerateDirectory;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public abstract class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";
    private static final String DATE_FORMAT_1 = "HH:mm:ss";
    private static final String DATE_FORMAT_2 = "HH:mm:ss,MM-dd-yyyy";
    protected static final int  RESULT_SPEECH = 140;
    public static String PATIENT_ID;

    protected View cardView;
    protected String startTime;
    protected String endTime;
    protected MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public abstract boolean loadDataModel();

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
                if (isFadeIn) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                                nextButtonReady();
                            }
                        });
                    }
                }
                else {
                    moveToNextPage();
                    ((MainActivity) Objects.requireNonNull(getActivity())).incrementViewNumber();
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

    public void logStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        Date today = Calendar.getInstance().getTime();
        startTime = dateFormat.format(today);
        Log.d(TAG, "logStartTime: " + startTime);
    }

    public void logEndTimeAndData(Context context, String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        Date today = Calendar.getInstance().getTime();
        endTime = dateFormat.format(today);
        String resultString;
        if (QuestionFragment.PATIENT_ID != null)
            resultString = QuestionFragment.PATIENT_ID + "," + data + "," + startTime + "," + endTime;
        else
            resultString = "null" + "," + data + "," + startTime + "," + endTime;


        Log.d(TAG, "logEndTimeAndData: Logging the following result... \n" + resultString);

        String dateOfSurvey = endTime.substring(endTime.lastIndexOf(',') + 1);
        DataLogService.log(context, new File(GenerateDirectory.getRootFile(context), QuestionFragment.PATIENT_ID + '_' + dateOfSurvey + ".csv"),resultString);
    }

    public void nextButtonReady() {
        ((MainActivity) Objects.requireNonNull(getActivity())).nextButtonReady();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
