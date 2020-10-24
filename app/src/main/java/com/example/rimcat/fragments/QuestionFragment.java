package com.example.rimcat.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimcat.data_log.DataLogService;
import com.example.rimcat.data_log.GenerateDirectory;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    protected long startMilis;
    protected long endMilis;
    protected MediaPlayer mediaPlayer;
    protected Context mContext;
    protected Vibrator mVibrator;
    protected TextToSpeech textToSpeech;
    protected boolean isTTSInitialized;

    public void startAnimation(final boolean isFadeIn) {
        Animation fadeOutAnimation;
        if (isFadeIn)
            fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        else
            fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

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
        startMilis = today.getTime();
        startTime = dateFormat.format(today);
        Log.d(TAG, "logStartTime: " + startTime);
    }

    public void logEndTimeAndData(Context context, String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        Date today = Calendar.getInstance().getTime();
        endMilis = today.getTime();
        endTime = dateFormat.format(today);
        String resultString;
        if (QuestionFragment.PATIENT_ID != null)
            resultString = QuestionFragment.PATIENT_ID + "," + data + "," + calculateResponseTime() + ',' + startTime + "," + endTime;
        else
            resultString = "null" + "," + data  + ","  + calculateResponseTime() + ',' + startTime + "," + endTime;


        Log.d(TAG, "logEndTimeAndData: Logging the following result... \n" + resultString);

        String dateOfSurvey = endTime.substring(endTime.lastIndexOf(',') + 1);
        DataLogService.log(context, new File(GenerateDirectory.getRootFile(context), QuestionFragment.PATIENT_ID + '_' + dateOfSurvey + ".csv"), resultString);
    }

    private long calculateResponseTime() {
        return endMilis - startMilis;
    }


    public void nextButtonReady() {
        ((MainActivity) Objects.requireNonNull(getActivity())).nextButtonReady();
    }

    protected void vibrateToastAndExecuteSound(String submitText, boolean shouldExecuteSound) {
        // Vibrate the device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mVibrator != null) {
            mVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else if (mVibrator != null) {
            //deprecated in API 26
            mVibrator.vibrate(500);
        }

        // Toast affirmative message
        Toast t = Toast.makeText(getActivity(), "'" + submitText + "' submitted! Keep going!", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) t.getView();
        TextView toastTV = (TextView) group.getChildAt(0);
        toastTV.setTextSize(20);
        t.setGravity(Gravity.TOP, 0, 5);
        t.show();

        // Execute sound
        if (shouldExecuteSound) {
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(mContext, notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setUpTextToSpeech() {
        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    isTTSInitialized = true;
                } else {
                    Log.e(TAG, "TTS Initialization Failed!");
                    isTTSInitialized = false;
                }
            }
        });
    }


    public abstract boolean loadDataModel();

    public abstract void moveToNextPage();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
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
