package org.echowear.rimcatbeta.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;
import org.echowear.rimcatbeta.data_log.DataLogService;
import org.echowear.rimcatbeta.data_log.GenerateDirectory;

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
    protected long startMillis;
    protected long endMillis;
    protected MediaPlayer mediaPlayer;
    protected Context mContext;
    protected Vibrator mVibrator;

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
                    // TODO: This isn't good design. Look into this later
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(mp -> {
                            releaseMediaPlayer();
                            nextButtonReady();
                        });
                    }
                }
                else {
                    moveToNextPage();
                    ((MainActivity) requireActivity()).incrementViewNumber();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (cardView != null) {
            cardView.startAnimation(fadeOutAnimation);
        }
    }

    public void logStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.US);
        Date today = Calendar.getInstance().getTime();
        startMillis = today.getTime();
        startTime = dateFormat.format(today);
        Log.d(TAG, "logStartTime: " + startTime);
    }

    public void logEndTimeAndData(Context context, String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2, Locale.US);
        Date today = Calendar.getInstance().getTime();
        endMillis = today.getTime();
        endTime = dateFormat.format(today);
        String resultString;
        //TODO: Simplify this to only have one string
        if (QuestionFragment.PATIENT_ID != null)
            resultString = QuestionFragment.PATIENT_ID + "," + data + "," + getCorrectAnswer() + "," + getTriedMicrophone() + "," + calculateResponseTime() + "," + startTime + "," + endTime;
        else
            resultString = "null" + "," + data + "," + getCorrectAnswer()  + "," + getTriedMicrophone() + ","  + calculateResponseTime() + ',' + startTime + "," + endTime;


        Log.d(TAG, "logEndTimeAndData: Logging the following result... \n" + resultString);
        String dateOfSurvey = endTime.substring(endTime.lastIndexOf(',') + 1);
        DataLogService.log(context, new File(GenerateDirectory.getRootFile(), QuestionFragment.PATIENT_ID + '_' + dateOfSurvey + ".csv"), resultString);
//        JSONDataLogService.log(context, new File(GenerateDirectory.getRootFile(context), QuestionFragment.PATIENT_ID + '_' + dateOfSurvey + "coords.txt"), ((MainActivity)getActivity()).coordsJson());

    }

    protected long calculateResponseTime() {
        return endMillis - startMillis;
    }


    public void nextButtonReady() {
        ((MainActivity) requireActivity()).nextButtonReady();
    }

    protected void toastAtTopOfScreen(String message, int duration) {
        Toast.makeText(getActivity(), message, duration).setGravity(Gravity.TOP, 0, 5);
//        ViewGroup group = (ViewGroup) t.getView();
//        //TextView toastTV = (TextView) Objects.requireNonNull(group).getChildAt(0);
//        //toastTV.setTextSize(20);
//        t.setGravity(Gravity.TOP, 0, 5);
//        t.show();
    }

    protected void vibrateToastAndExecuteSound(String submitText) {
        // Vibrate the device
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mVibrator != null) {
//            mVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//        } else if (mVibrator != null) {
//            //deprecated in API 26
//            mVibrator.vibrate(500);
//        }

        // Toast affirmative message
        toastAtTopOfScreen("'" + submitText + "' submitted! Keep going!", Toast.LENGTH_SHORT);

        // Execute sound
//        if (shouldExecuteSound) {
//            try {
//                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                Ringtone r = RingtoneManager.getRingtone(mContext, notification);
//                r.play();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    protected void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() || mediaPlayer.isLooping()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public abstract boolean loadDataModel();

    public abstract void moveToNextPage();

    public abstract String getCorrectAnswer();

    public abstract String getTriedMicrophone();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return Objects.requireNonNull(container).findFocus();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

}
